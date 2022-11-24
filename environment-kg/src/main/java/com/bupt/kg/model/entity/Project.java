package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@NodeEntity(EntityConstant.PROJECT)
@ApiModel("项目要")
public class Project extends NodeAbstract{
    @Property
    @ApiModelProperty("名称")
    private String name;
    @Property
    @ApiModelProperty("编号")
    private String projectId;
    @Property
    @ApiModelProperty("领域")
    private String field;
    @Property
    @ApiModelProperty("所属省市区")
    private String belongArea;
    @Property
    @ApiModelProperty("描述")
    private String description;

    // 包含的中标公告
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.OUTGOING)
    private List<IncludeRelation<Project, BidWinningAnnouncement>> beIncludedBidWinningAnnouncement;

    // 包含于的招标公告
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.INCOMING)
    private List<IncludeRelation<BiddingAnnouncement, Project>> includedBiddingAnnouncement;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> projectProperties = new ArrayList<>();
        projectProperties.add(new TranslateDto("id", "id"));
        projectProperties.add(new TranslateDto("名称", "name"));
        projectProperties.add(new TranslateDto("编号", "projectId"));
        projectProperties.add(new TranslateDto("领域", "field"));
        projectProperties.add(new TranslateDto("所属省市区", "belongArea"));
        projectProperties.add(new TranslateDto("描述", "description"));

        translator = new Translator("项目", EntityConstant.PROJECT, projectProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.PROJECT, this);
    }
}
