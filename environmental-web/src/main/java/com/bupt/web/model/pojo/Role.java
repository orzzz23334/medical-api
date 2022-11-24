package com.bupt.web.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bupt.web.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("角色表")
//@ToString(callSuper = true)
public class Role extends BaseModel<Role> {

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("角色字符")
    @TableField("`key`")
    private String key;

    @ApiModelProperty(hidden = true)
    private Boolean isDeleted;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Set<String> operation;

    /** 菜单组 */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<Long> menuIds;
}
