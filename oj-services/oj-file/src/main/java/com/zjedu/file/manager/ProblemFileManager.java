package com.zjedu.file.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjedu.common.exception.ProblemIDRepeatException;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.file.dao.LanguageEntityService;
import com.zjedu.file.dao.TagEntityService;
import com.zjedu.file.feign.PassportFeignClient;
import com.zjedu.file.feign.ProblemFeignClient;
import com.zjedu.pojo.dto.ProblemDTO;
import com.zjedu.pojo.entity.problem.*;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.ImportProblemVO;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author Zhong
 * @Create 2025/3/31 10:46
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class ProblemFileManager
{
    @Resource
    private LanguageEntityService languageEntityService;

    @Resource
    private TagEntityService tagEntityService;

    @Resource
    private ProblemFeignClient problemFeignClient;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    /**
     * zip文件导入题目 仅超级管理员可操作
     *
     * @param file
     */
    public void importProblem(MultipartFile file) throws StatusFailException, StatusSystemErrorException
    {

        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"zip".toUpperCase().contains(suffix.toUpperCase()))
        {
            throw new StatusFailException("请上传zip格式的题目文件压缩包！");
        }

        String fileDirId = IdUtil.simpleUUID();
        String fileDir = Constants.File.TESTCASE_TMP_FOLDER.getPath() + File.separator + fileDirId;
        String filePath = fileDir + File.separator + file.getOriginalFilename();
        // 文件夹不存在就新建
        FileUtil.mkdir(fileDir);
        try
        {
            file.transferTo(new File(filePath));
        } catch (IOException e)
        {
            FileUtil.del(fileDir);
            throw new StatusSystemErrorException("服务器异常：评测数据上传失败！");
        }

        // 将压缩包压缩到指定文件夹
        ZipUtil.unzip(filePath, fileDir);

        // 删除zip文件
        FileUtil.del(filePath);


        // 检查文件是否存在
        File testCaseFileList = new File(fileDir);
        File[] files = testCaseFileList.listFiles();
        if (files == null || files.length == 0)
        {
            FileUtil.del(fileDir);
            throw new StatusFailException("评测数据压缩包里文件不能为空！");
        }


        HashMap<String, File> problemInfo = new HashMap<>();
        HashMap<String, File> testcaseInfo = new HashMap<>();

        for (File tmp : files)
        {
            if (tmp.isFile())
            {
                // 检查文件是否时json文件
                if (!tmp.getName().endsWith("json"))
                {
                    FileUtil.del(fileDir);
                    throw new StatusFailException("编号为：" + tmp.getName() + "的文件格式错误，请使用json文件！");
                }
                String tmpPreName = tmp.getName().substring(0, tmp.getName().lastIndexOf("."));
                problemInfo.put(tmpPreName, tmp);
            }
            if (tmp.isDirectory())
            {
                testcaseInfo.put(tmp.getName(), tmp);
            }
        }

        // 读取json文件生成对象
        HashMap<String, ImportProblemVO> problemVoMap = new HashMap<>();
        for (String key : problemInfo.keySet())
        {
            // 若有名字不对应，直接返回失败
            if (testcaseInfo.getOrDefault(key, null) == null)
            {
                FileUtil.del(fileDir);
                throw new StatusFailException("请检查编号为：" + key + "的题目数据文件与测试数据文件夹是否一一对应！");
            }
            try
            {
                FileReader fileReader = new FileReader(problemInfo.get(key));
                ImportProblemVO importProblemVo = JSONUtil.toBean(fileReader.readString(), ImportProblemVO.class);
                problemVoMap.put(key, importProblemVo);
            } catch (Exception e)
            {
                FileUtil.del(fileDir);
                throw new StatusFailException("请检查编号为：" + key + "的题目json文件的格式：" + e.getLocalizedMessage());
            }
        }

        QueryWrapper<Language> languageQueryWrapper = new QueryWrapper<>();
        languageQueryWrapper.eq("oj", "ME");
        List<Language> languageList = languageEntityService.list(languageQueryWrapper);

        HashMap<String, Long> languageMap = new HashMap<>();
        for (Language language : languageList)
        {
            languageMap.put(language.getName(), language.getId());
        }

        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        List<ProblemDTO> problemDTOS = new LinkedList<>();
        List<Tag> tagList = tagEntityService.list(new QueryWrapper<Tag>().eq("oj", "ME"));
        HashMap<String, Tag> tagMap = new HashMap<>();
        for (Tag tag : tagList)
        {
            tagMap.put(tag.getName().toUpperCase(), tag);
        }
        for (String key : problemInfo.keySet())
        {
            ImportProblemVO importProblemVo = problemVoMap.get(key);
            // 格式化题目语言
            List<Language> languages = new LinkedList<>();
            for (String lang : importProblemVo.getLanguages())
            {
                Long lid = languageMap.getOrDefault(lang, null);
                if (lid != null)
                {
                    languages.add(new Language().setId(lid).setName(lang));
                }
            }

            if (languages.isEmpty())
            {
                languages = languageList;
            }

            // 格式化题目代码模板
            List<CodeTemplate> codeTemplates = new LinkedList<>();
            for (Map<String, String> tmp : importProblemVo.getCodeTemplates())
            {
                String language = tmp.getOrDefault("language", null);
                String code = tmp.getOrDefault("code", null);
                Long lid = languageMap.getOrDefault(language, null);
                if (language == null || code == null || lid == null)
                {
//                    FileUtil.del(fileDir);
//                    throw new StatusFailException("请检查编号为：" + key + "的题目的代码模板列表是否有错，不要添加不支持的语言！");
                    continue;
                }
                codeTemplates.add(new CodeTemplate().setCode(code).setStatus(true).setLid(lid));
            }

            // 格式化标签
            List<Tag> tags = new LinkedList<>();
            for (String tagStr : importProblemVo.getTags())
            {
                Tag tag = tagMap.getOrDefault(tagStr.toUpperCase(), null);
                if (tag == null)
                {
                    tags.add(new Tag().setName(tagStr).setOj("ME"));
                } else
                {
                    tags.add(tag);
                }
            }

            Problem problem = BeanUtil.mapToBean(importProblemVo.getProblem(), Problem.class, true);
            if (problem.getAuthor() == null)
            {
                problem.setAuthor(userRolesVo.getUsername());
            }
            List<ProblemCase> problemCaseList = new LinkedList<>();
            for (Map<String, Object> tmp : importProblemVo.getSamples())
            {
                problemCaseList.add(BeanUtil.mapToBean(tmp, ProblemCase.class, true));
            }

            // 格式化用户额外文件和判题额外文件
            if (importProblemVo.getUserExtraFile() != null)
            {
                JSONObject userExtraFileJson = JSONUtil.parseObj(importProblemVo.getUserExtraFile());
                problem.setUserExtraFile(userExtraFileJson.toString());
            }
            if (importProblemVo.getJudgeExtraFile() != null)
            {
                JSONObject judgeExtraFileJson = JSONUtil.parseObj(importProblemVo.getJudgeExtraFile());
                problem.setJudgeExtraFile(judgeExtraFileJson.toString());
            }


            ProblemDTO problemDto = new ProblemDTO();
            problemDto.setProblem(problem)
                    .setCodeTemplates(codeTemplates)
                    .setTags(tags)
                    .setLanguages(languages)
                    .setUploadTestcaseDir(fileDir + File.separator + key)
                    .setIsUploadTestCase(true)
                    .setSamples(problemCaseList);

            Constants.JudgeMode judgeMode = Constants.JudgeMode.getJudgeMode(importProblemVo.getJudgeMode());
            if (judgeMode == null)
            {
                problemDto.setJudgeMode(Constants.JudgeMode.DEFAULT.getMode());
            } else
            {
                problemDto.setJudgeMode(judgeMode.getMode());
            }
            problemDTOS.add(problemDto);
        }

        if (problemDTOS.isEmpty())
        {
            throw new StatusFailException("警告：未成功导入一道以上的题目，请检查文件格式是否正确！");
        } else
        {
            HashSet<String> repeatProblemIDSet = new HashSet<>();
            HashSet<String> failedProblemIDSet = new HashSet<>();
            int failedCount = 0;
            for (ProblemDTO problemDto : problemDTOS)
            {
                try
                {

                    boolean isOk = problemFeignClient.adminAddProblem(problemDto);
                    if (!isOk)
                    {
                        failedCount++;
                    }
                } catch (ProblemIDRepeatException e)
                {
                    repeatProblemIDSet.add(problemDto.getProblem().getProblemId());
                    failedCount++;
                } catch (Exception e)
                {
                    log.error("", e);
                    failedProblemIDSet.add(problemDto.getProblem().getProblemId());
                    failedCount++;
                }
            }
            if (failedCount > 0)
            {
                int successCount = problemDTOS.size() - failedCount;
                String errMsg = "[导入结果] 成功数：" + successCount + ",  失败数：" + failedCount +
                        ",  重复失败的题目ID：" + repeatProblemIDSet;
                if (failedProblemIDSet.size() > 0)
                {
                    errMsg = errMsg + "<br/>未知失败的题目ID：" + failedProblemIDSet;
                }
                throw new StatusFailException(errMsg);
            }
        }
    }
}
