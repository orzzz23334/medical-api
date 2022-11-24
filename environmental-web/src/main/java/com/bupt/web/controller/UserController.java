package com.bupt.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.common.enums.ResultCodeEnum;
import com.bupt.web.common.response.ResponseData;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.vo.PageData;
import com.bupt.web.model.vo.RoleVo;
import com.bupt.web.service.RoleService;
import com.bupt.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/web/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @ApiOperation(value="获取用户列表")
    @PostMapping("/list")
    public ResponseData list(@RequestBody(required = false) User user,
                                    @ApiParam("页数") @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                    @ApiParam("每页的数据数量") @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize){
        PageData<User> userList = userService.selectUserList(new Page<>(pageNum, pageSize), user);
        return ResponseData.success(userList);
    }

    @ApiOperation(value="获取用户详情")
    @GetMapping(value = {  "/","{userId}" })
    public ResponseData getUserInfo(@PathVariable(value = "userId",required = false) Long userId){
        // 1. 检查当前用户是否有访问权限？略
        HashMap<String,Object> data = new HashMap<>();
        // 3. 查询所有角色
        List<RoleVo> roles = roleService.selectRoleAll();
        // 2. 查询用户当前拥有的角色
        if (userId != null) {
            List<Long> roleIds = roleService.selectRoleListByUserId(userId);
            data.put("roleIds",roleIds);
            if (!roleIds.contains(1L)) {// 如果当前用户不是超级管理员，则其看不到超级管理员的角色
                roles = roles.stream().filter(r->r.getId()!=1L).collect(Collectors.toList());
            }
        } else {
            System.out.println(BaseController.getRoles());
            if (!BaseController.getRoles().contains("SUPER_ADMIN")) {// 如果当前用户不是超级管理员，则其看不到超级管理员的角色
                roles = roles.stream().filter(r->r.getId()!=1L).collect(Collectors.toList());
            }
        }
        data.put("roles",roles);
        return ResponseData.success(data);
    }

    @ApiOperation(value="注册用户")
    @PostMapping("/register")
    public ResponseData register(@RequestBody User user){
        if (user.getName() == null || user.getPassword()== null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        // 判断用户名是否唯一
        if (!userService.checkUserNameUnique(user.getName())) {
            return ResponseData.setResult(ResultCodeEnum.USER_HAS_EXIST);
        }
        List<Long> roleIds = new ArrayList<>(); // 默认注册用户都是普通用户
        roleIds.add(2L);// 添加普通用户权限
        user.setRoleIds(roleIds);
        user.setCreatedBy(user.getName()); // 自己注册
        // 添加用户
        int result = userService.register(user);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }


    @ApiOperation(value="添加用户")
    @PostMapping("")
    public ResponseData add(@RequestBody User user){
        if (user.getName() == null || user.getPassword()== null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        // 判断用户名是否唯一
        if (!userService.checkUserNameUnique(user.getName())) {
            return ResponseData.setResult(ResultCodeEnum.USER_HAS_EXIST);
        }
        // 设置当前用户的被添加人
        user.setCreatedBy(BaseController.getUserName());
        // 添加用户
        int result = userService.register(user);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @ApiOperation(value="修改用户")
    @PutMapping()
    public ResponseData update(@RequestBody User user){
        if (user.getId() == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        user.setPassword(null);
//        if (user.getName() != null && !userService.checkUserNameUnique(user.getName())) {
//            return ResponseData.setResult(ResultCodeEnum.USER_HAS_EXIST);
//        }
        // 检查修改权限，略
        user.setUpdatedBy(BaseController.getUserName());
        int result = userService.update(user);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }
    @ApiOperation(value="删除用户")
    @DeleteMapping("/{id}")
    public ResponseData delete(@PathVariable("id") Long id){
        if (id == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        if (id.equals(BaseController.getUserId())) {
            return ResponseData.setResult("201","当前用户不允许删除");
        }
        // 判断，不能删除自己
        int result = userService.delete(id);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_DELETE_FAILURE);
        }
    }
    @ApiOperation(value="重置密码")
    @PutMapping("/resetPwd")
    public ResponseData resetPwd(@RequestBody User user)
    {
        if (user.getId() == null || user.getPassword() == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
//        if (user.getName() != null && !userService.checkUserNameUnique(user.getName())) {
//            return ResponseData.setResult(ResultCodeEnum.USER_HAS_EXIST);
//        }
        // 检查修改权限，略
        user.setUpdatedBy(BaseController.getUserName());
        int result = userService.update(user);
        if (result>0) {
            return ResponseData.success();
        } else {
            return ResponseData.setResult(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }
}
