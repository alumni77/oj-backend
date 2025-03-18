package com.zjedu.passport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjedu.pojo.entity.user.UserRole;
import com.zjedu.pojo.vo.UserRolesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


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
}
