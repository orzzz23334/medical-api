package com.bupt.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.common.enums.ResultCodeEnum;
import com.bupt.web.common.response.ResponseData;
import com.bupt.web.model.pojo.Role;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.vo.PageData;
import com.bupt.web.model.vo.RoleVo;
import com.bupt.web.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/web/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ApiOperation(value="获取角色列表")
    @PostMapping("/list")
    public ResponseData list(@RequestBody(required = false) Role role,
                             @ApiParam("页数") @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                             @ApiParam("每页的数据数量") @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize){
        PageData<Role> rolePageData = roleService.selectRoleList(new Page<>(pageNum, pageSize), role);
        return ResponseData.success(rolePageData);
    }


    @ApiOperation(value="获取角色权限")
    @GetMapping("/user")
    public ResponseData getRolePermission(){
        Long userId = BaseController.getUserId();
        if (userId == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        List<RoleVo> roleList = roleService.getRolePermissionByUserId(userId);
        return ResponseData.success(roleList);
    }

    @ApiOperation(value="添加角色")
    @PostMapping
    public ResponseData add(@Validated @RequestBody Role role) {
        if (role.getName() == null || role.getKey() == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        if (!roleService.checkRoleNameUnique(role.getName())) {
            return ResponseData.setResult(ResultCodeEnum.ROLE_NAME_HAS_EXIST);
        }
        if (!roleService.checkRoleKeyUnique(role.getKey())) {
            return ResponseData.setResult(ResultCodeEnum.ROLE_KEY_HAS_EXIST);
        }
        // 设置被创建者
        role.setCreatedBy(BaseController.getUserName());
        int result = roleService.insertRole(role);
        if (result > 0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @ApiOperation(value="修改角色")
    @PutMapping()
    public ResponseData update(@RequestBody Role role) {
        if (role.getId() == null) {
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        // 检查有没有修改权限
        // 检查没有重复
//        if (role.getName()!= null && !roleService.checkRoleNameUnique(role.getName())) {
//            return ResponseData.setResult(ResultCodeEnum.ROLE_NAME_HAS_EXIST);
//        }
//        if (role.getKey()!= null && !roleService.checkRoleKeyUnique(role.getKey())) {
//            return ResponseData.setResult(ResultCodeEnum.ROLE_KEY_HAS_EXIST);
//        }
        // 修改角色
        role.setUpdatedBy(BaseController.getUserName());
        int result = roleService.updateRole(role);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }

    @ApiOperation(value="删除角色")
    @DeleteMapping("/{roleId}")
    public ResponseData delete(@PathVariable("roleId") Long roleId) {
        if (roleId == null) {
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        int result = roleService.deleteRoleById(roleId);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_DELETE_FAILURE);
        }

    }

}
