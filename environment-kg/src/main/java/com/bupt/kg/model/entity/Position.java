package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.dto.transferable.NodeTransferable;
import com.bupt.kg.model.relation.IncludeRelation;

import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
@NodeEntity
@ApiModel("地区")
public class Position extends NodeAbstract{

    @ApiModelProperty("名称")
    private String name;

    // 地区下包含的政府
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.OUTGOING)
    private List<IncludeRelation<Position, Government>> includeGovernment;
    // 地区下包含的公司
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.OUTGOING)
    private List<IncludeRelation<Position, Company>> includeCompany;
    // 地区下包含的学校
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.OUTGOING)
    private List<IncludeRelation<Position, School>> includeSchool;

    // 地区下包含的地区
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.OUTGOING)
    private List<IncludeRelation<Position, Position>> beIncludedPosition;
    // 包含这个地区的地区
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.INCOMING)
    private List<IncludeRelation<Position, Position>> includePosition;

    public static final Translator translator;
    static {
        List<TranslateDto> positionProperties = new ArrayList<>();
        positionProperties.add(new TranslateDto("id","id"));
        positionProperties.add(new TranslateDto("名称","name"));

        translator = new Translator("地区", EntityConstant.POSITION, positionProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.POSITION, this);
    }
}
