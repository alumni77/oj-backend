package com.zjedu.file.manager;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.file.dao.FileEntityService;
import com.zjedu.file.feign.PassportFeignClient;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Zhong
 * @Create 2025/3/30 21:57
 * @Version 1.0
 * @Description
 */

@Component
@Slf4j
public class MarkDownFileManager
{
    @Resource
    private FileEntityService fileEntityService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;


    public Map<Object, Object> uploadMDImg(MultipartFile image) throws StatusFailException, StatusSystemErrorException, StatusForbiddenException
    {
        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, null);
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为admin
        boolean isAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "admin".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        if (!isRoot && !isProblemAdmin && !isAdmin)
        {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (image == null)
        {
            throw new StatusFailException("上传的图片不能为空！");
        }
        if (image.getSize() > 1024 * 1024 * 4)
        {
            throw new StatusFailException("上传的图片文件大小不能大于4M！");
        }
        //获取文件后缀
        String suffix = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp".toUpperCase().contains(suffix.toUpperCase()))
        {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp格式的图片！");
        }

        //若不存在该目录，则创建目录
        FileUtil.mkdir(Constants.File.MARKDOWN_FILE_FOLDER.getPath());

        //通过UUID生成唯一文件名
        String filename = IdUtil.simpleUUID() + "." + suffix;
        try
        {
            //将文件保存指定目录
            image.transferTo(FileUtil.file(Constants.File.MARKDOWN_FILE_FOLDER.getPath() + File.separator + filename));
        } catch (Exception e)
        {
            log.error("图片文件上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：图片文件上传失败！");
        }

        com.zjedu.pojo.entity.common.File file = new com.zjedu.pojo.entity.common.File();
        file.setFolderPath(Constants.File.MARKDOWN_FILE_FOLDER.getPath())
                .setName(filename)
                .setFilePath(Constants.File.MARKDOWN_FILE_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("md")
                .setUid(userRolesVo.getUuid());
        fileEntityService.save(file);

        return MapUtil.builder()
                .put("link", Constants.File.IMG_API.getPath() + filename)
                .put("fileId", file.getId()).map();

    }

    public void deleteMDImg(Long fileId) throws StatusFailException, StatusForbiddenException
    {
        com.zjedu.pojo.entity.common.File file = fileEntityService.getById(fileId);

        if (file == null)
        {
            throw new StatusFailException("错误：文件不存在！");
        }

        if (!file.getType().equals("md"))
        {
            throw new StatusForbiddenException("错误：不支持删除！");
        }

        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        UserInfo userRolesVo = passportFeignClient.getByUid(userId);

        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, null);
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));

        if (!file.getUid().equals(userRolesVo.getUuid()) && !isRoot && !isProblemAdmin)
        {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        boolean isOk = FileUtil.del(file.getFilePath());
        if (isOk)
        {
            fileEntityService.removeById(fileId);
        } else
        {
            throw new StatusFailException("删除失败");
        }
    }

    public Map<Object, Object> uploadMd(MultipartFile file) throws StatusFailException, StatusSystemErrorException, StatusForbiddenException
    {
        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");

        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, null);
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为admin
        boolean isAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "admin".equals(role.getRole()));
        // 是否为problem_admin
        boolean isProblemAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "problem_admin".equals(role.getRole()));


        if (!isRoot && !isProblemAdmin && !isAdmin)
        {
            throw new StatusForbiddenException("对不起，您无权限操作！");
        }

        if (file == null)
        {
            throw new StatusFailException("上传的文件不能为空！");
        }
        if (file.getSize() >= 1024 * 1024 * 128)
        {
            throw new StatusFailException("上传的文件大小不能大于128M！");
        }
        //获取文件后缀
        String suffix = "";
        String filename = "";
        if (file.getOriginalFilename() != null && file.getOriginalFilename().contains("."))
        {
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            //通过UUID生成唯一文件名
            filename = IdUtil.simpleUUID() + "." + suffix;
        } else
        {
            filename = IdUtil.simpleUUID();
        }
        //若不存在该目录，则创建目录
        FileUtil.mkdir(Constants.File.MARKDOWN_FILE_FOLDER.getPath());

        try
        {
            //将文件保存指定目录
            file.transferTo(FileUtil.file(Constants.File.MARKDOWN_FILE_FOLDER.getPath() + File.separator + filename));
        } catch (Exception e)
        {
            log.error("文件上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：文件上传失败！");
        }

        return MapUtil.builder()
                .put("link", Constants.File.FILE_API.getPath() + filename)
                .map();
    }

}
