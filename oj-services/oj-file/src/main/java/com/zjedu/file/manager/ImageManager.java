package com.zjedu.file.manager;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusSystemErrorException;
import com.zjedu.file.dao.FileEntityService;
import com.zjedu.file.dao.UserInfoEntityService;
import com.zjedu.file.feign.PassportFeignClient;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Zhong
 * @Create 2025/3/30 20:07
 * @Version 1.0
 * @Description
 */

@Component
@Slf4j
public class ImageManager
{
    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private UserInfoEntityService userInfoEntityService;

    @Resource
    private FileEntityService fileEntityService;


    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> uploadAvatar(MultipartFile image) throws StatusFailException, StatusSystemErrorException
    {
        if (image == null)
        {
            throw new StatusFailException("上传的头像图片文件不能为空！");
        }
        if (image.getSize() > 1024 * 1024 * 2)
        {
            throw new StatusFailException("上传的头像图片文件大小不能大于2M！");
        }
        //获取文件后缀
        String suffix = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp".toUpperCase().contains(suffix.toUpperCase()))
        {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp格式的头像图片！");
        }
        //若不存在该目录，则创建目录
        FileUtil.mkdir(Constants.File.USER_AVATAR_FOLDER.getPath());
        //通过UUID生成唯一文件名
        String filename = IdUtil.simpleUUID() + "." + suffix;
        try
        {
            //将文件保存指定目录
            image.transferTo(FileUtil.file(Constants.File.USER_AVATAR_FOLDER.getPath() + File.separator + filename));
        } catch (Exception e)
        {
            log.error("头像文件上传异常-------------->", e);
            throw new StatusSystemErrorException("服务器异常：头像上传失败！");
        }

        // 获取当前登录用户
        //从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", userId);
        UserInfo accountProfile = userInfoEntityService.getOne(queryWrapper);

        // 将当前用户所属的file表中avatar类型的实体的delete设置为1；
        fileEntityService.updateFileToDeleteByUidAndType(accountProfile.getUuid(), "avatar");

        //更新user_info里面的avatar
        UpdateWrapper<UserInfo> userInfoUpdateWrapper = new UpdateWrapper<>();
        userInfoUpdateWrapper.set("avatar", Constants.File.IMG_API.getPath() + filename)
                .eq("uuid", accountProfile.getUuid());
        userInfoEntityService.update(userInfoUpdateWrapper);

        // 插入file表记录
        com.zjedu.pojo.entity.common.File imgFile = new com.zjedu.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.USER_AVATAR_FOLDER.getPath())
                .setFilePath(Constants.File.USER_AVATAR_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("avatar")
                .setUid(accountProfile.getUuid());
        fileEntityService.saveOrUpdate(imgFile);
        // 更新session
        accountProfile.setAvatar(Constants.File.IMG_API.getPath() + filename);

        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(accountProfile.getUuid(), "");

        return MapUtil.builder()
                .put("uid", userRolesVo.getUid())
                .put("username", userRolesVo.getUsername())
                .put("nickname", userRolesVo.getNickname())
                .put("avatar", Constants.File.IMG_API.getPath() + filename)
                .put("email", userRolesVo.getEmail())
                .put("number", userRolesVo.getNumber())
                .put("school", userRolesVo.getSchool())
                .put("course", userRolesVo.getCourse())
                .put("signature", userRolesVo.getSignature())
                .put("realname", userRolesVo.getRealname())
                .put("github", userRolesVo.getGithub())
                .put("blog", userRolesVo.getBlog())
                .put("cfUsername", userRolesVo.getCfUsername())
                .put("roleList", userRolesVo.getRoles().stream().map(Role::getRole))
                .map();
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> uploadCarouselImg(MultipartFile image) throws StatusFailException, StatusSystemErrorException
    {
        // 需要获取一下该token对应用户的数据
        //从请求头获取用户ID
        // 获取当前登录用户
        String userId = request.getHeader("X-User-Id");
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", userId);
        UserInfo userInfo = userInfoEntityService.getOne(queryWrapper);
        // 是否为超级管理员
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
        boolean isRoot = userRolesVo.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        if (!isRoot)
        {
            throw new StatusFailException("您没有权限上传轮播图！");
        }

        if (image == null)
        {
            throw new StatusFailException("上传的图片文件不能为空！");
        }
        if (image.getSize() > 1024 * 1024 * 5)
        {
            throw new StatusFailException("上传的轮播图文件大小不能大于5M！");
        }

        //获取文件后缀
        String suffix = Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png,webp,jfif,svg".toUpperCase().contains(suffix.toUpperCase()))
        {
            throw new StatusFailException("请选择jpg,jpeg,gif,png,webp,jfif,svg格式的头像图片！");
        }
        //若不存在该目录，则创建目录
        FileUtil.mkdir(Constants.File.HOME_CAROUSEL_FOLDER.getPath());
        //通过UUID生成唯一文件名
        String filename = IdUtil.simpleUUID() + "." + suffix;
        try
        {
            //将文件保存指定目录
            image.transferTo(FileUtil.file(Constants.File.HOME_CAROUSEL_FOLDER.getPath() + File.separator + filename));
        } catch (Exception e)
        {
            log.error("图片文件上传异常-------------->{}", e.getMessage());
            throw new StatusSystemErrorException("服务器异常：图片上传失败！");
        }

        // 插入file表记录
        com.zjedu.pojo.entity.common.File imgFile = new com.zjedu.pojo.entity.common.File();
        imgFile.setName(filename).setFolderPath(Constants.File.HOME_CAROUSEL_FOLDER.getPath())
                .setFilePath(Constants.File.HOME_CAROUSEL_FOLDER.getPath() + File.separator + filename)
                .setSuffix(suffix)
                .setType("carousel")
                .setUid(userInfo.getUuid());
        fileEntityService.saveOrUpdate(imgFile);

        return MapUtil.builder()
                .put("id", imgFile.getId())
                .put("url", Constants.File.IMG_API.getPath() + filename)
                .map();
    }
}
