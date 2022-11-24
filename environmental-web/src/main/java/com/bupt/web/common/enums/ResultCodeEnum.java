package com.bupt.web.common.enums;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum ResultCodeEnum {

    SERVER_ERROR("500","服务器内部错误"),
    INVALID_API("404", "没有找到对应的API"),
    ARGUMENT_NOT_VALID("400", "参数校验失败"),
    OK("200","OK"),

    USER_HAS_EXIST("201","用户已存在"),
    ROLE_NAME_HAS_EXIST("201","角色名称已存在"),
    ROLE_KEY_HAS_EXIST("201","角色权限已存在"),
    MENU_HAS_EXIST("201","菜单已存在"),
    SUB_MENU_HAS_EXIST("201","存在子菜单,不允许删除"),
    MENU_ROLE_HAS_EXIST("201","菜单已分配,不允许删除"),
    //增
    DB_ADD_FAILURE("202","添加失败"),
    //删
    DB_DELETE_FAILURE("210","删除失败"),
    //改
    DB_UPDATE_ERROR("209","修改失败"),
    //查
    DB_FIND_FAILURE("209","查找失败，没有该条记录"),
    //请求参数
    PARA_WORNING_NULL("301","必要请求参数为空"),
    PARA_FORMAT_ERROR("302","请求的参数格式错误"),
    PARA_NUM_ERROR("303","请求的参数个数错误");

;

    private String code;
    private String desc;

    ResultCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}