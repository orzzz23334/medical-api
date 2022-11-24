package com.bupt.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.model.pojo.Role;
import com.bupt.web.model.vo.PageData;
import com.bupt.web.model.vo.RoleVo;

import java.util.List;
import java.util.Set;

public interface RoleService {
    /**
     * 获取当前用户拥有的角色
     * @param userId userId
     * @return List<Role>
     */
    List<RoleVo> getRolePermissionByUserId(Long userId);

    List<Long> selectRoleListByUserId(Long userId);

    List<RoleVo> selectRoleAll();

    PageData<Role> selectRoleList(Page<Role> page, Role role);

    Boolean checkRoleNameUnique(String roleName);

    Boolean checkRoleKeyUnique(String roleKey);

    int insertRole(Role role);

    int updateRole(Role role);

    int deleteRoleById(Long roleId);
}
