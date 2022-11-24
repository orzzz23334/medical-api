package com.bupt.kg.model.entity;


import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.Construct;
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
@NodeEntity(EntityConstant.BIDDING_ANNOUNCEMENT)
@ApiModel("招标公告")
public class BiddingAnnouncement extends NodeAbstract {
    @Property
    @ApiModelProperty("项目名称")
    private String name;
    @Property
    private String releaseTime;
    @Property
    private String projectPhase;
    @Property
    private String biddingType;
    @Property
    private String annexName;
    @Property
    private String relatedFields;
    @Property
    private String originURL;
    @Property
    private String projectDescription;
    @Property
    private String projectAddress;
    @Property
    private String relatedProvinceCityArea;
    @Property
    private String announcementType;
    @Property
    private String annexLink;
    @Property
    private String biddingPhase;

//    // 建设关系
//    @JsonIgnore
//    @ToString.Exclude
//    @ApiModelProperty(hidden = true)
//    @Relationship(type = RelationConstant.CONSTRUCT, direction = Relationship.INCOMING)
//    List<Construct<Company, BiddingAnnouncement>> constructs;
//
//    // 代理关系
//    @JsonIgnore
//    @ToString.Exclude
//    @ApiModelProperty(hidden = true)
//    @Relationship(type = RelationConstant.AGENT, direction = Relationship.INCOMING)
//    List<Agent<Company, BiddingAnnouncement>> agents;
//
//    // 被中标公告包含
//    @JsonIgnore
//    @ToString.Exclude
//    @ApiModelProperty(hidden = true)
//    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.INCOMING)
//    List<IncludeRelation<Bid, BiddingAnnouncement>> bidIncludes;

    // 包含的项目
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.OUTGOING)
    private List<IncludeRelation<BiddingAnnouncement, Project>> beIncludedProject;


    public static final Translator translator;
    static {
        ArrayList<TranslateDto> biddingAnnouncementProperties = new ArrayList<>();
        biddingAnnouncementProperties.add(new TranslateDto("id", "id"));
        biddingAnnouncementProperties.add(new TranslateDto("项目名称", "name"));
        biddingAnnouncementProperties.add(new TranslateDto("发布时间", "releaseTime"));
        biddingAnnouncementProperties.add(new TranslateDto("项目阶段", "projectPhase"));
        biddingAnnouncementProperties.add(new TranslateDto("招标类型", "biddingType"));
        biddingAnnouncementProperties.add(new TranslateDto("附件名称", "annexName"));
        biddingAnnouncementProperties.add(new TranslateDto("领域", "relatedFields"));
        biddingAnnouncementProperties.add(new TranslateDto("原文链接", "originURL"));
        biddingAnnouncementProperties.add(new TranslateDto("建设内容描述", "projectDescription"));
        biddingAnnouncementProperties.add(new TranslateDto("项目地址", "projectAddress"));
        biddingAnnouncementProperties.add(new TranslateDto("省市区", "relatedProvinceCityArea"));
        biddingAnnouncementProperties.add(new TranslateDto("公告类型", "announcementType"));
        biddingAnnouncementProperties.add(new TranslateDto("附件链接", "annexLink"));
        biddingAnnouncementProperties.add(new TranslateDto("招标阶段", "biddingPhase"));

        translator = new Translator("招标公告", EntityConstant.BIDDING_ANNOUNCEMENT, biddingAnnouncementProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.BIDDING_ANNOUNCEMENT, this);
    }
}
