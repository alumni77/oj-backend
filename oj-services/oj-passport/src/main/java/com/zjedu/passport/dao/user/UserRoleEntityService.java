package com.zjedu.passport.dao.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserRole;
import com.zjedu.pojo.vo.UserRolesVO;

import java.util.List;

/**
 * @Author Zhong
 * @Create 2025/3/18 16:09
 * @Version 1.0
 * @Description 服务类
 */

public interface UserRoleEntityService extends IService<UserRole>
{
    UserRolesVO getUserRoles(String uid, String username);

    List<Role> getRolesByUid(String uid);

    IPage<UserRolesVO> getUserList(int limit, int currentPage, String keyword, Boolean onlyAdmin);

    void deleteCache(String uid, boolean isRemoveSession);
}
