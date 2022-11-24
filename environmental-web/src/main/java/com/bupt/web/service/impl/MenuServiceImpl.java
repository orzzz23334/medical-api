package com.bupt.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.web.dao.MenuDao;
import com.bupt.web.dao.RoleDao;
import com.bupt.web.dao.UserDao;
import com.bupt.web.model.pojo.Menu;
import com.bupt.web.model.pojo.Role;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.vo.MenuVo;
import com.bupt.web.model.vo.TreeSelect;
import com.bupt.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 根据用户id查询路由菜单
     * @param userId
     * @return
     */
    @Override
    public List<MenuVo> selectMenuTreeByUserId(Long userId) {
//         首先判断用户是不是超级管理员，如果是超级管理员，则显示所有的菜单，如果不是超级管理员，则根据用户id筛选菜单
        List<User> users = userDao.selectUserById(userId);
        List<Menu> menus;
        if (!users.isEmpty() && users.get(0).getRoleIds().contains(1L)) { // 如果是超级管理员
            menus = menuDao.selectMenuTreeAll();
        } else {
            menus = menuDao.selectMenuTreeByUserId(userId);
        }
        // 返回Tree结构的数据
        List<Menu> menuTree = new ArrayList<>();
        getMenuTree(menuTree, menus, 0L);
        return menuTree.stream().map(MenuVo::new).collect(Collectors.toList());
    }

    /**
     * 根据RoleId 获取 role的menu权限
     * @param roleId roleId
     * @return menu权限
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        Set<String> perms = new HashSet<>();
        if (roleId.equals(1L)) {
            perms.add("*:*:*"); // 管理员拥有所有权限
        } else {
            perms.addAll(menuDao.selectMenuPermsByRoleId(roleId));
        }
        return perms;
    }

    @Override
    public List<Menu> selectMenuList(Long userId) {
        return selectMenuList(new Menu(), userId);
    }

    @Override
    public List<Menu> selectMenuList(Menu menu, Long userId) {
        List<User> users = userDao.selectUserById(userId);
        List<Menu> menus;
        if (users.get(0).getRoleIds().contains(1L)) { // 如果是超级管理员
            QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
            if (menu != null) {
                menuQueryWrapper.eq(menu.getId() != null,"id",menu.getId());
                menuQueryWrapper.eq(menu.getInvisible() != null,"invisible",menu.getInvisible());
                menuQueryWrapper.eq(menu.getName() != null,"name",menu.getName());
                menuQueryWrapper.eq(menu.getIsDeleted() != null,"is_deleted",menu.getIsDeleted());
            }
            menus = menuDao.selectList(menuQueryWrapper);
        } else {
            menus = menuDao.selectMenuListByUserId(menu, userId);
        }
        return menus;
    }

    @Override
    public Menu selectMenuById(Long menuId) {
        return menuDao.selectById(menuId);
    }

    @Override
    public Set<Long> selectMenuListByRoleId(Long roleId) {
        Set<Long> menuIds;
        Role role = roleDao.selectById(roleId);
        if (roleId.equals(1L)) {
            menuIds = menuDao.selectMenuListByRoleId(null);
        } else {
            menuIds = menuDao.selectMenuListByRoleId(roleId);
        }
        return menuIds;
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<Menu> menus) {
        List<Menu> menuTree = buildMenuTree(menus);
        return menuTree.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        // 找出 menus 中的 root Id
        List<Long> menuIds = menus.stream().map(Menu::getId).collect(Collectors.toList());
        List<Menu> menuTree = new ArrayList<>();
        for (Menu menu : menus) {
            // 如果，当前节点的父节点，不在menuIds中，说明其是根节点的第一代子节点
            if (! menuIds.contains(menu.getPId())) {
                // 给menu获取后辈
                getMenuTree(menu.getChildren(), menus, menu.getId()); // 把当前节点，加到menuTree中
                menuTree.add(menu); // 说明其是根节点的子节点
            }
        }
        // 返回Tree结构的数据
        if (menuTree.isEmpty()) {
            menuTree = menus;
        }
        return menuTree;
    }

    @Override
    public int insertMenu(Menu menu) {
        return menuDao.insert(menu);
    }

    @Override
    public int updateMenu(Menu menu) {
        return menuDao.updateById(menu);
    }

    @Override
    public int deleteMenuById(Long menuId) {
        return menuDao.deleteById(menuId);
    }

    @Override
    public Boolean checkMenuNameUnique(Menu menu) {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq(menu != null && menu.getPId() != null,"p_id",menu.getPId());
        menuQueryWrapper.eq(menu != null && menu.getName() != null,"name",menu.getName());
        List<Menu> menus = menuDao.selectList(menuQueryWrapper);
        return menus.size() <= 0;
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuDao.hasChildByMenuId(menuId);
        return result > 0;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = menuDao.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * 递归遍历菜单，构建菜单树
     * @param menuTree 构建的菜单树
     * @param menuList 菜单列表
     * @param pId 父id
     */
    public void getMenuTree(List<Menu> menuTree, List<Menu> menuList, Long pId) {
        // 获取当前pid的子菜单
        menuList.stream().filter(menu -> menu.getPId().equals(pId))
            .forEach(menu -> { // 构建结果树 & 递归遍历子菜单
                menuTree.add(menu);
                getMenuTree(menu.getChildren(), menuList, menu.getId());
            });
    }

}
