package com.bupt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.web.model.pojo.Role;
import com.bupt.web.model.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Repository
public interface RoleDao extends BaseMapper<Role> {
    List<Role> selectRolePermissionByUserId(Long userId);

    List<Long> selectRoleListByUserId(Long userId);

    int deleteRoleById(Long roleId);
}
