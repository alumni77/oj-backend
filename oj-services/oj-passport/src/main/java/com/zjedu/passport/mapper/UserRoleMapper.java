package com.zjedu.passport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.user.Role;
import com.zjedu.pojo.entity.user.UserRole;
import com.zjedu.pojo.vo.UserRolesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author Zhong
 * @Create 2025/3/18 16:16
 * @Version 1.0
 * @Description
 */

@Mapper
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

    UserRolesVO getUserRoles(@Param("uid") String uid, @Param("username") String username);

    List<Role> getRolesByUid(@Param("uid") String uid);

}
