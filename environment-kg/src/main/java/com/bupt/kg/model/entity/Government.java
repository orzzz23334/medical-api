package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.dto.transferable.NodeTransferable;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.relation.TakeOfficeRelation;

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

@Data @NoArgsConstructor @AllArgsConstructor
@Builder @Jacksonized
@NodeEntity
@ApiModel("政府")
public class Government extends NodeAbstract{


    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("别称")
    private String otherName;

    @ApiModelProperty("级别")
    private String level;
    // 当前政府上设政府
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE, direction = Relationship.INCOMING)
    private List<IncludeRelation<Government, Government>> includeGovernment;
    // 当前政府的下设的政府
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE, direction = Relationship.OUTGOING)
    private List<IncludeRelation<Government, Government>> beIncludedGovernment;

    // 任职到这个政府的人
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.TAKE_OFFICE, direction = Relationship.INCOMING)
    private List<TakeOfficeRelation<Person, Government>> takeOfficePerson;

    // 这个政府所属的地区
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.INCOMING)
    private List<IncludeRelation<Position, Government>> includePosition;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> governmentProperties = new ArrayList<>();
        governmentProperties.add(new TranslateDto("id","id"));
        governmentProperties.add(new TranslateDto("名称","name"));
        governmentProperties.add(new TranslateDto("地址","address"));
        governmentProperties.add(new TranslateDto("别称","otherName"));
        governmentProperties.add(new TranslateDto("级别","level"));

        translator = new Translator("政府",EntityConstant.GOVERNMENT, governmentProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.GOVERNMENT, this);
    }
}
