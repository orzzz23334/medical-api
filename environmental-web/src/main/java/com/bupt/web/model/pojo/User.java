package com.bupt.web.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bupt.web.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("用户表")
public class User extends BaseModel<User> {

    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("密码")
    @JsonIgnoreProperties
    private String password;
    @ApiModelProperty("性别")
    private Integer gender;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(hidden = true)
    private Boolean isDeleted;

    @TableField(exist = false)
    private List<Long> roleIds;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<String> roles;
}
