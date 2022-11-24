package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.relation.*;
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
@NodeEntity(EntityConstant.BID_WINNING_ANNOUNCEMENT)
@ApiModel("中标公告")
public class BidWinningAnnouncement extends NodeAbstract{

    @Property
    private String name;
    @Property
    private String releaseTime;
    @Property
    private String relatedFields;
    @Property
    private String projectPhase;
    @Property
    private String biddingType;
    @Property
    private String originURL;
    @Property
    private String annexName;
    @Property
    private String projectAddress;
    @Property
    private String relatedProvinceCityArea;
    @Property
    private String annexLink;
    @Property
    private String announcementType;
    @Property
    private String biddingPhase;

    // 包含的标段包组
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.OUTGOING)
    private List<IncludeRelation<BidWinningAnnouncement, Package>> beIncludedPackage;

    // 包含的中标公告
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.INCOMING)
    private List<IncludeRelation<Project, BidWinningAnnouncement>> includedProject;

//    // 建设关系
//    @JsonIgnore
//    @ToString.Exclude
//    @ApiModelProperty(hidden = true)
//    @Relationship(type = RelationConstant.CONSTRUCT, direction = Relationship.INCOMING)
//    private List<Construct<Company, BidWinningAnnouncement>> constructs;
//
//    // 代理关系
//    @JsonIgnore
//    @ToString.Exclude
//    @ApiModelProperty(hidden = true)
//    @Relationship(type = RelationConstant.AGENT, direction = Relationship.INCOMING)
//    private List<Agent<Company, BidWinningAnnouncement>> agents;
//
//    // 标包含关系
//    @JsonIgnore
//    @ToString.Exclude
//    @ApiModelProperty(hidden = true)
//    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.INCOMING)
//    private List<IncludeRelation<Bid, BidWinningAnnouncement>> bidIncludes;
//
//    // 中标关系
//    @JsonIgnore
//    @ToString.Exclude
//    @ApiModelProperty(hidden = true)
//    @Relationship(type = RelationConstant.WINBID, direction = Relationship.INCOMING)
//    private List<WinBid<Company, BidWinningAnnouncement>> winBids;
//
//    // 评标人关系
//    @JsonIgnore
//    @ToString.Exclude
//    @ApiModelProperty(hidden = true)
//    @Relationship(type = RelationConstant.EVALUATE_BID, direction = Relationship.INCOMING)
//    private List<EvaluateBid<Person, BidWinningAnnouncement>> evaluateBidPeople;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> bidWinningAnnouncementProperties = new ArrayList<>();
        bidWinningAnnouncementProperties.add(new TranslateDto("id", "id"));
        bidWinningAnnouncementProperties.add(new TranslateDto("项目名称", "name"));
        bidWinningAnnouncementProperties.add(new TranslateDto("发布时间", "releaseTime"));
        bidWinningAnnouncementProperties.add(new TranslateDto("项目阶段", "projectPhase"));
        bidWinningAnnouncementProperties.add(new TranslateDto("招标类型", "biddingType"));
        bidWinningAnnouncementProperties.add(new TranslateDto("附件名称", "annexName"));
        bidWinningAnnouncementProperties.add(new TranslateDto("领域", "relatedFields"));
        bidWinningAnnouncementProperties.add(new TranslateDto("原文链接", "originURL"));
        bidWinningAnnouncementProperties.add(new TranslateDto("项目地址", "projectAddress"));
        bidWinningAnnouncementProperties.add(new TranslateDto("省市区", "relatedProvinceCityArea"));
        bidWinningAnnouncementProperties.add(new TranslateDto("公告类型", "announcementType"));
        bidWinningAnnouncementProperties.add(new TranslateDto("附件链接", "annexLink"));
        bidWinningAnnouncementProperties.add(new TranslateDto("招标阶段", "biddingPhase"));

        translator = new Translator("中标公告", EntityConstant.BID_WINNING_ANNOUNCEMENT, bidWinningAnnouncementProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.BID_WINNING_ANNOUNCEMENT, this);
    }
}
