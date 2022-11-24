package com.bupt.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.common.enums.ResultCodeEnum;
import com.bupt.web.common.response.ResponseData;
import com.bupt.web.model.pojo.Menu;
import com.bupt.web.model.pojo.Role;
import com.bupt.web.model.vo.MenuVo;
import com.bupt.web.model.vo.PageData;
import com.bupt.web.model.vo.TreeSelect;
import com.bupt.web.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Api(tags = "菜单接口")
@RestController
@RequestMapping("/web/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @ApiOperation(value="获取当前用户的路由菜单",response = ResponseData.class)
    @GetMapping("/router")
    public ResponseData getMenuByUserId(){
        Long userId = BaseController.getUserId();
        List<MenuVo> menus = menuService.selectMenuTreeByUserId(userId);
        return ResponseData.success(menus);
    }


    @ApiOperation(value="获取当前用户的菜单列表")
    @PostMapping("/list")
    public ResponseData menuList(@RequestBody(required = false) Menu menu){
        Long userId = BaseController.getUserId();
        List<Menu> menus = menuService.selectMenuList(menu, userId);
        return ResponseData.success(menus);
    }

    @ApiOperation(value="获取当前用户的菜单树")
    @PostMapping("/tree")
    public ResponseData menuTree(@RequestBody(required = false) Menu menu){
        Long userId = BaseController.getUserId();
        List<Menu> menus = menuService.selectMenuList(menu, userId);
        List<Menu> menuTree = menuService.buildMenuTree(menus);
        return ResponseData.success(menuTree);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @ApiOperation(value="获取菜单详情")
    @GetMapping(value = "/{menuId}")
    public ResponseData getInfo(@PathVariable Long menuId) {
        // 根据菜单编号获取详细信息
        Menu menu = menuService.selectMenuById(menuId);
        return ResponseData.success(menu);
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation(value="获取用户菜单下拉树列表")
    @PostMapping("/treeselect")
    public ResponseData treeselect(@RequestBody(required = false) Menu menu) {
        Long userId = BaseController.getUserId();
        // 查询当前用户能看到的菜单列表
        List<Menu> menus = menuService.selectMenuList(menu, userId);
        // 转化为树结构
        List<TreeSelect> treeSelects = menuService.buildMenuTreeSelect(menus);
        return ResponseData.success(treeSelects);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation(value="加载角色对应角色菜单列表树")
    @GetMapping(value = "/treeselect/{roleId}")
    public ResponseData roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        Long userId = BaseController.getUserId();
        // 查询当前用户能看到的菜单列表
        List<Menu> menus = menuService.selectMenuList(userId);
        HashMap<String, Object> data = new HashMap<>();
        // 查询 当前角色拥有的 菜单Id
        Set<Long> menuIds = menuService.selectMenuListByRoleId(roleId);
        data.put("checkedKeys",menuIds);
        // 将菜单转为树结构
        List<TreeSelect> treeSelects = menuService.buildMenuTreeSelect(menus);
        data.put("menus",treeSelects);
        return ResponseData.success(data);
    }


    @ApiOperation(value="添加菜单")
    @PostMapping
    public ResponseData add(@RequestBody Menu menu) {
        if (menu.getName() == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        // 检查菜单名称
        if (!menuService.checkMenuNameUnique(menu)) {
            return ResponseData.setResult(ResultCodeEnum.MENU_HAS_EXIST);
        }
        // 添加菜单
        menu.setCreatedBy(BaseController.getUserName());
        int result = menuService.insertMenu(menu);
        if (result > 0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @ApiOperation(value="修改菜单")
    @PutMapping()
    public ResponseData update(@RequestBody Menu menu) {
        if (menu.getId() == null || menu.getId().equals(menu.getPId())) {
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        // 检查菜单名称
//        if (menu.getName() != null && !menuService.checkMenuNameUnique(menu)) {
//            return ResponseData.setResult(ResultCodeEnum.MENU_HAS_EXIST);
//        }
        // 检查有没有修改权限

        // 修改菜单
        menu.setUpdatedBy(BaseController.getUserName());
        int result = menuService.updateMenu(menu);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }

    @ApiOperation(value="删除菜单")
    @DeleteMapping("/{menuId}")
    public ResponseData delete(@PathVariable("menuId") Long menuId) {
        if (menuId == null) {
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        if (menuService.hasChildByMenuId(menuId)) {
            return ResponseData.setResult(ResultCodeEnum.SUB_MENU_HAS_EXIST);
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        // 删除菜单
        int result = menuService.deleteMenuById(menuId);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.MENU_ROLE_HAS_EXIST);
        }

    }
}
