package com.bupt.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.dao.MenuDao;
import com.bupt.web.dao.RoleDao;
import com.bupt.web.dao.RoleMenuDao;
import com.bupt.web.model.pojo.Role;
import com.bupt.web.model.pojo.RoleMenu;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.pojo.UserRole;
import com.bupt.web.model.vo.PageData;
import com.bupt.web.model.vo.RoleVo;
import com.bupt.web.service.MenuService;
import com.bupt.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuDao roleMenuDao;

    @Override
    public List<RoleVo> getRolePermissionByUserId(Long userId) {
        // 查询用户角色
        List<Role> roleList = roleDao.selectRolePermissionByUserId(userId);
        // 给用户角色添加权限
        roleList.forEach(role -> role.setOperation(menuService.selectMenuPermsByRoleId(role.getId())));
        return roleList.stream().map(RoleVo::new).collect(Collectors.toList());
    }

    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return roleDao.selectRoleListByUserId(userId);
    }

    @Override
    public List<RoleVo> selectRoleAll() {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("is_deleted",false);
        List<Role> roleList = roleDao.selectList(roleQueryWrapper);
        return roleList.stream().map(RoleVo::new).collect(Collectors.toList());
    }

    @Override
    public PageData<Role> selectRoleList(Page<Role> page, Role role) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        if (role != null) {
            roleQueryWrapper.eq(role.getId() != null,"id",role.getId());
            roleQueryWrapper.eq(role.getKey() != null,"`key`",role.getKey());
            roleQueryWrapper.eq(role.getName() != null,"name",role.getName());
            roleQueryWrapper.eq(role.getIsDeleted() != null,"is_deleted",role.getIsDeleted());
        }
        Page<Role> rolePage = roleDao.selectPage(page, roleQueryWrapper);
        return new PageData<>(rolePage);
    }

    @Override
    public Boolean checkRoleNameUnique(String roleName) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq(roleName != null ,"name",roleName);
        List<Role> roleList = roleDao.selectList(roleQueryWrapper);
        return roleList.size() < 1;
    }

    @Override
    public Boolean checkRoleKeyUnique(String roleKey) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq(roleKey != null ,"`key`",roleKey);
        List<Role> roleList = roleDao.selectList(roleQueryWrapper);
        return roleList.size() < 1;
    }

    @Override
    public int insertRole(Role role) {
        // 添加角色
        int insert = roleDao.insert(role);
        // 添加菜单
        insertRoleMenu(role);
        return insert;
    }

    @Override
    public int updateRole(Role role) {
        // 修改角色信息
        int insert = 0;
        if (role.getName() != null || role.getKey() != null || role.getIsDeleted() != null) {
            insert = roleDao.updateById(role);
        }
        if (role.getMenuIds() != null) {
            // 删除角色与菜单关联
            insert = deleteRoleMenuByRoleId(role.getId());
            // 重新添加角色菜单权限
            insert = insertRoleMenu(role);
        }
        return insert;
    }

    @Override
    public int deleteRoleById(Long roleId) {
        // 删除角色相关菜单
        int i = deleteRoleMenuByRoleId(roleId);
        // 删除角色
        return roleDao.deleteRoleById(roleId);
    }

    public int deleteRoleMenuByRoleId(Long roleId) {
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("role_id",roleId);
        return roleMenuDao.delete(roleMenuQueryWrapper);
    }

    public int insertRoleMenu(Role role) {
        // 新增用户与角色管理
        if (role.getMenuIds() != null && role.getId() != null) {
            role.getMenuIds().forEach(menuId -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuId(menuId);
                roleMenu.setRoleId(role.getId());
                roleMenuDao.insert(roleMenu);
            });
        }
        return role.getMenuIds().size();
    }

}
