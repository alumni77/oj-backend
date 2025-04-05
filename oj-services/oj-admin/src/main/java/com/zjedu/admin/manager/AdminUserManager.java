package com.zjedu.admin.manager;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjedu.admin.dao.UserInfoEntityService;
import com.zjedu.admin.dao.UserRecordEntityService;
import com.zjedu.admin.dao.UserRoleEntityService;
import com.zjedu.admin.feign.PassportFeignClient;
import com.zjedu.common.exception.StatusFailException;
import com.zjedu.common.exception.StatusForbiddenException;
import com.zjedu.pojo.dto.AdminEditUserDTO;
import com.zjedu.pojo.entity.user.UserInfo;
import com.zjedu.pojo.entity.user.UserRecord;
import com.zjedu.pojo.entity.user.UserRole;
import com.zjedu.pojo.vo.UserRolesVO;
import com.zjedu.utils.Constants;
import com.zjedu.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author Zhong
 * @Create 2025/4/5 15:45
 * @Version 1.0
 * @Description
 */

@Slf4j
@Component
public class AdminUserManager
{
    @Resource
    private HttpServletRequest request;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private UserRoleEntityService userRoleEntityService;

    @Resource
    private UserInfoEntityService userInfoEntityService;

    @Resource
    private UserRecordEntityService userRecordEntityService;

    @Resource
    private RedisUtils redisUtils;

    public IPage<UserRolesVO> getUserList(Integer limit, Integer currentPage, Boolean onlyAdmin, String keyword) throws StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("用户没有权限操作");
        }

        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;
        if (keyword != null)
        {
            keyword = keyword.trim();
        }
        return passportFeignClient.getUserList(limit, currentPage, keyword, onlyAdmin);
    }

    public void editUser(AdminEditUserDTO adminEditUserDto) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("用户没有权限操作");
        }

        String username = adminEditUserDto.getUsername();
        String uid = adminEditUserDto.getUid();
        String realname = adminEditUserDto.getRealname();
        String email = adminEditUserDto.getEmail();
        String password = adminEditUserDto.getPassword();
        int type = adminEditUserDto.getType();
        int status = adminEditUserDto.getStatus();
        boolean setNewPwd = adminEditUserDto.getSetNewPwd();

        String titleName = adminEditUserDto.getTitleName();
        String titleColor = adminEditUserDto.getTitleColor();

        if (StringUtils.hasText(realname) && realname.length() > 50)
        {
            throw new StatusFailException("真实姓名的长度不能超过50位");
        }
        if (StringUtils.hasText(titleName) && titleName.length() > 20)
        {
            throw new StatusFailException("头衔的长度建议不要超过20位");
        }
        if (StringUtils.hasText(password) && (password.length() < 6 || password.length() > 20))
        {
            throw new StatusFailException("密码长度建议为6~20位！");
        }
        if (username.length() > 20)
        {
            throw new StatusFailException("用户名长度建议不能超过20位!");
        }

        if (StrUtil.isBlank(email))
        {
            email = null;
        } else
        {
            QueryWrapper<UserInfo> emailUserInfoQueryWrapper = new QueryWrapper<>();
            emailUserInfoQueryWrapper.select("uuid", "email")
                    .eq("email", email);
            UserInfo userInfo = userInfoEntityService.getOne(emailUserInfoQueryWrapper, false);
            if (userInfo != null && !Objects.equals(userInfo.getUuid(), adminEditUserDto.getUid()))
            {
                throw new StatusFailException("修改失败，邮箱已被使用，请重新设置其他邮箱！");
            }
        }

        UpdateWrapper<UserInfo> userInfoUpdateWrapper = new UpdateWrapper<>();
        userInfoUpdateWrapper.eq("uuid", uid)
                .set("username", username)
                .set("realname", realname)
                .set("email", email)
                .set(setNewPwd, "password", SecureUtil.md5(password))
                .set("title_name", titleName)
                .set("title_color", titleColor)
                .set("status", status);
        boolean updateUserInfo = userInfoEntityService.update(userInfoUpdateWrapper);

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("uid", uid);
        UserRole userRole = userRoleEntityService.getOne(userRoleQueryWrapper, false);
        boolean changeUserRole = false;
        int oldType = userRole.getRoleId().intValue();
        if (userRole.getRoleId().intValue() != type)
        {
            userRole.setRoleId((long) type);
            changeUserRole = userRoleEntityService.updateById(userRole);
            if (type == 1000 || oldType == 1000)
            {
                // 新增或者去除超级管理员需要删除缓存
                String cacheKey = Constants.Account.SUPER_ADMIN_UID_LIST_CACHE.getCode();
                redisUtils.del(cacheKey);
            }
        }
        if (updateUserInfo && setNewPwd)
        {
            // 需要重新登录
            passportFeignClient.deleteCache(uid, true);
        } else if (changeUserRole)
        {
            // 需要重新授权
            passportFeignClient.deleteCache(uid, false);
        }
    }

    public void deleteUser(List<String> deleteUserIdList) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("用户没有权限操作");
        }

        boolean isOk = userInfoEntityService.removeByIds(deleteUserIdList);
        if (!isOk)
        {
            throw new StatusFailException("删除失败！");
        }
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRolesVo = passportFeignClient.getUserRoles(userId, "");
        log.info("[{}],[{}],uidList:[{}],operatorUid:[{}],operatorUsername:[{}]",
                "Admin_User", "Delete", deleteUserIdList, userRolesVo.getUid(), userRolesVo.getUsername());
    }

    public void insertBatchUser(List<List<String>> users) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("用户没有权限操作");
        }

        List<String> successUidList = new LinkedList<>();
        if (users != null)
        {
            HashSet<String> failedUserNameSet = new HashSet<>();
            for (List<String> user : users)
            {
                try
                {
                    String uuid = addNewUser(user);
                    if (uuid != null)
                    {
                        successUidList.add(uuid);
                    } else
                    {
                        failedUserNameSet.add(user.get(0));
                    }
                } catch (Exception e)
                {
                    failedUserNameSet.add(user.get(0));
                }
            }
            if (failedUserNameSet.size() > 0)
            {
                int failedCount = failedUserNameSet.size();
                int successCount = users.size() - failedCount;
                String errMsg = "[导入结果] 成功数：" + successCount + ",  失败数：" + failedCount +
                        ",  失败的用户名：" + failedUserNameSet;
                throw new StatusFailException(errMsg);
            }
        } else
        {
            throw new StatusFailException("插入的用户数据不能为空！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String addNewUser(List<String> user) throws StatusFailException
    {
        String uuid = IdUtil.simpleUUID();
        UserInfo userInfo = new UserInfo()
                .setUuid(uuid)
                .setUsername(user.get(0))
                .setPassword(SecureUtil.md5(user.get(1)))
                .setEmail(user.size() <= 2 || StringUtils.isEmpty(user.get(2)) ? null : user.get(2));

        if (user.size() >= 4)
        {
            String realname = user.get(3);
            if (StringUtils.hasText(realname))
            {
                userInfo.setRealname(user.get(3));
            }
        }

        if (user.size() >= 5)
        {
            String gender = user.get(4);
            if ("male".equalsIgnoreCase(gender) || "0".equals(gender))
            {
                userInfo.setGender("male");
            } else if ("female".equalsIgnoreCase(gender) || "1".equals(gender))
            {
                userInfo.setGender("female");
            }
        }

        if (user.size() >= 6)
        {
            String nickname = user.get(5);
            if (StringUtils.hasText(nickname))
            {
                userInfo.setNickname(nickname);
            }
        }

        if (user.size() >= 7)
        {
            String school = user.get(6);
            if (StringUtils.hasText(school))
            {
                userInfo.setSchool(school);
            }
        }


        boolean result1 = userInfoEntityService.save(userInfo);
        UserRole userRole = new UserRole()
                .setRoleId(1002L)
                .setUid(uuid);
        boolean result2 = userRoleEntityService.save(userRole);
        UserRecord userRecord = new UserRecord().setUid(uuid);
        boolean result3 = userRecordEntityService.save(userRecord);
        if (!result1 || !result2 || !result3)
        {
            throw new StatusFailException("生成用户失败");
        }
        return uuid;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> generateUser(Map<String, Object> params) throws StatusFailException, StatusForbiddenException
    {
        boolean b = checkAuthority();
        if (!b)
        {
            throw new StatusForbiddenException("用户没有权限操作");
        }

        String prefix = (String) params.getOrDefault("prefix", "");
        String suffix = (String) params.getOrDefault("suffix", "");
        int numberFrom = (int) params.getOrDefault("number_from", 1);
        int numberTo = (int) params.getOrDefault("number_to", 10);
        int passwordLength = (int) params.getOrDefault("password_length", 6);

        List<UserInfo> userInfoList = new LinkedList<>();
        List<UserRole> userRoleList = new LinkedList<>();
        List<UserRecord> userRecordList = new LinkedList<>();

        HashMap<String, Object> userInfo = new HashMap<>(); // 存储账号密码放入redis中，等待导出excel
        for (int num = numberFrom; num <= numberTo; num++)
        {
            String uuid = IdUtil.simpleUUID();
            String password = RandomUtil.randomString(passwordLength);
            String username = prefix + num + suffix;
            userInfoList.add(new UserInfo()
                    .setUuid(uuid)
                    .setUsername(username)
                    .setPassword(SecureUtil.md5(password)));
            userInfo.put(username, password);
            userRoleList.add(new UserRole()
                    .setRoleId(1002L)
                    .setUid(uuid));
            userRecordList.add(new UserRecord().setUid(uuid));
        }
        boolean result1 = userInfoEntityService.saveBatch(userInfoList);
        boolean result2 = userRoleEntityService.saveBatch(userRoleList);
        boolean result3 = userRecordEntityService.saveBatch(userRecordList);
        if (result1 && result2 && result3)
        {
            String key = IdUtil.simpleUUID();
            redisUtils.hmset(key, userInfo, 1800); // 存储半小时
            return MapUtil.builder().put("key", key).map();
        } else
        {
            throw new StatusFailException("生成指定用户失败！注意查看组合生成的用户名是否已有存在的！");
        }
    }

    private boolean checkAuthority()
    {
        String userId = request.getHeader("X-User-ID");
        UserRolesVO userRoles = passportFeignClient.getUserRoles(userId, "");
        // 是否为超级管理员
        boolean isRoot = userRoles.getRoles().stream()
                .anyMatch(role -> "root".equals(role.getRole()));
        // 是否为admin
        boolean isAdmin = userRoles.getRoles().stream()
                .anyMatch(role -> "admin".equals(role.getRole()));

        return isRoot || isAdmin;
    }
}
