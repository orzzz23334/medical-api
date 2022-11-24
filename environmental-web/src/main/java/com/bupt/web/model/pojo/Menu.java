package com.bupt.web.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bupt.web.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Jacksonized
@ApiModel("菜单表")
public class Menu extends BaseModel<Menu> {

    @ApiModelProperty("pId")
    @JsonProperty("pId")
    private Long pId;

    @ApiModelProperty("菜单名")
    private String name;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("icon")
    private String icon;
    @ApiModelProperty("重定向")
    private String redirect;
    @ApiModelProperty("是否可见")
    private Boolean invisible;
    @ApiModelProperty("顺序")
    private Integer orderNum;
    @ApiModelProperty("菜单类型")
    private String type;
    @ApiModelProperty("是否启用")
    private Boolean isDeleted;
    @ApiModelProperty("菜单权限")
    private String permission;
    @ApiModelProperty("菜单组件")
    private String component;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<Menu> children = new ArrayList<>();
}
