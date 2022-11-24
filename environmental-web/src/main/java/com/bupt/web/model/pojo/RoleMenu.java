package com.bupt.web.model.pojo;

import com.bupt.web.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("角色权限表")
public class RoleMenu extends BaseModel<RoleMenu> {

    @ApiModelProperty("menu id")
    private Long menuId;

    @ApiModelProperty("role id")
    private Long roleId;

}
