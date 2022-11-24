package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.dto.transferable.NodeTransferable;
import com.bupt.kg.model.relation.AttendRelation;
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
@ApiModel("学校")
public class School extends NodeAbstract{

    @ApiModelProperty("名称")
    private String name;
    // 地区下包含的学校
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.INCOMING)
    private List<IncludeRelation<Position, School>> includePosition;


    // 当前学校包含的学校
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.OUTGOING)
    private List<IncludeRelation<School, School>> beIncludedSchool;

    // 当前学校被包含的学校
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.INCOMING)
    private List<IncludeRelation<School, School>> includedSchool;
    // 就读这个学校的人
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.ATTEND, direction = Relationship.INCOMING)
    private List<AttendRelation<Person, School>> attendPerson;

    public static final Translator translator;
    static {
        List<TranslateDto> schoolProperties = new ArrayList<>();
        schoolProperties.add(new TranslateDto("id","id"));
        schoolProperties.add(new TranslateDto("名称","name"));

        translator = new Translator("学校", EntityConstant.SCHOOL, schoolProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.SCHOOL, this);
    }
}
