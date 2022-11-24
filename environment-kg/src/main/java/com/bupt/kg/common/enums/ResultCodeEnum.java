package com.bupt.kg.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 枚举类 列举各种可能的状态码
 */
@NoArgsConstructor
public enum ResultCodeEnum {

    SERVER_ERROR("500","服务器内部错误"),
    SERVER_REFLACT_CLASSNOTFOUND_ERROR("501", "反射错误，未找到指定类"),
    SERVER_REFLACT_FIELDNOTFOUND_ERROR("501", "反射错误，未找到指定字段"),
    SERVER_REFLACT_FIELDACCESS_ERROR("501", "反射错误，访问字段失败"),

    INVALID_API("404", "没有找到对应的API"),
    PARA_WORNING_NULL("401","必要请求参数为空"),
    PARA_FORMAT_ERROR("401","请求的参数格式错误"),
    ARGUMENT_NOT_VALID("401", "参数校验失败"),
    API_NOT_FINISH("401","接口暂未实现"),

    OK("200","OK"),

    ENTITY_LABEL_NOT_EXIST("202","实体标签不存在"),
    ENTITY_TRANSLATOR_NOT_EXIST("202","翻译不存在"),
    RELATION_LABEL_NOT_EXIST("202","关系标签不存在"),
    ENTITY_NOT_EXIST("202","实体不存在"),
    RELATION_NOT_EXIST("202","关系不存在"),
    //增
    DB_ADD_FAILURE("201","添加失败"),
    AUTO_REGRESSION("201", "添加失败，不允许自回归关系"),
    //删
    DB_DELETE_FAILURE("204","删除失败"),
    //改
    DB_UPDATE_ERROR("203","修改失败"),


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