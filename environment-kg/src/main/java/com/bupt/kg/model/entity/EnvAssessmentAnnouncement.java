package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.transferable.NodeTransferable;
import com.bupt.kg.model.relation.Construct;
import com.bupt.kg.model.relation.Evaluate;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@NodeEntity(EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT)
public class EnvAssessmentAnnouncement extends NodeAbstract{
    @Property
    private String name;
    @Property
    private String constructLocation;
    @Property
    private String releaseTime;
    @Property
    private String projectPhase;
    @Property
    private String annexName;
    @Property
    private String relatedFields;
    @Property
    private String ifApproved;
    @Property
    private String originUrl;
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

    @JsonIgnore
    @ToString.Exclude
    @Relationship(type = RelationConstant.EVALUATE, direction = Relationship.INCOMING)
    private List<Evaluate<Company, EnvAssessmentAnnouncement>> evaluates;
    @JsonIgnore
    @ToString.Exclude
    @Relationship(type = RelationConstant.CONSTRUCT, direction = Relationship.INCOMING)
    private List<Construct<Company, EnvAssessmentAnnouncement>> constructs;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> envAAProperties = new ArrayList<>();
        envAAProperties.add(new TranslateDto("id", "id"));
        envAAProperties.add(new TranslateDto("项目名称", "name"));
        envAAProperties.add(new TranslateDto("建设地点", "constructLocation"));
        envAAProperties.add(new TranslateDto("发布时间", "releaseTime"));
        envAAProperties.add(new TranslateDto("项目阶段", "projectPhase"));
        envAAProperties.add(new TranslateDto("附件名称", "annexName"));
        envAAProperties.add(new TranslateDto("领域", "relatedFields"));
        envAAProperties.add(new TranslateDto("是否批准", "ifApproved"));
        envAAProperties.add(new TranslateDto("原文链接", "originUrl"));
        envAAProperties.add(new TranslateDto("项目地址", "projectAddress"));
        envAAProperties.add(new TranslateDto("省市区", "relatedProvinceCityArea"));
        envAAProperties.add(new TranslateDto("公告类型", "announcementType"));
        envAAProperties.add(new TranslateDto("附件链接", "annexLink"));
        envAAProperties.add(new TranslateDto("招标阶段", "biddingPhase"));

        translator = new Translator("环评公告", EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT, envAAProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT, this);
    }
}
