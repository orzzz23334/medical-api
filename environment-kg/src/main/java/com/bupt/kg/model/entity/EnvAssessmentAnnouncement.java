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
        envAAProperties.add(new TranslateDto("????????????", "name"));
        envAAProperties.add(new TranslateDto("????????????", "constructLocation"));
        envAAProperties.add(new TranslateDto("????????????", "releaseTime"));
        envAAProperties.add(new TranslateDto("????????????", "projectPhase"));
        envAAProperties.add(new TranslateDto("????????????", "annexName"));
        envAAProperties.add(new TranslateDto("??????", "relatedFields"));
        envAAProperties.add(new TranslateDto("????????????", "ifApproved"));
        envAAProperties.add(new TranslateDto("????????????", "originUrl"));
        envAAProperties.add(new TranslateDto("????????????", "projectAddress"));
        envAAProperties.add(new TranslateDto("?????????", "relatedProvinceCityArea"));
        envAAProperties.add(new TranslateDto("????????????", "announcementType"));
        envAAProperties.add(new TranslateDto("????????????", "annexLink"));
        envAAProperties.add(new TranslateDto("????????????", "biddingPhase"));

        translator = new Translator("????????????", EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT, envAAProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT, this);
    }
}
