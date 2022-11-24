package com.bupt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.web.model.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface MenuDao extends BaseMapper<Menu> {
    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    List<Menu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 用户ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<Menu> selectMenuListByUserId(@Param("menu") Menu menu, @Param("userId") Long userId);

    /**
     * 根据角色ID查询菜单树信息
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public Set<Long> selectMenuListByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int checkMenuExistRole(Long menuId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int hasChildByMenuId(Long menuId);
}
