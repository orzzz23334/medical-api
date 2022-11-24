package com.bupt.web.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("人与人之间可能得关系")
public class PeopleRelationType {
    @ApiModelProperty("关系代码")
    private int code;
    @ApiModelProperty("关系描述")
    private String describe;
    @ApiModelProperty("默认熟识度")
    private String defaultDegree;
}
