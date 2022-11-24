package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.dto.transferable.NodeTransferable;
import com.bupt.kg.model.relation.*;

import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Data @AllArgsConstructor @NoArgsConstructor
@Builder @Jacksonized
@NodeEntity
@ApiModel("人物")
public class Person extends NodeAbstract{

    @ApiModelProperty("名称")
    private String name = "";

    @ApiModelProperty("性别")
    private String gender = "";

    @ApiModelProperty("名族")
    private String nation = "";

    @ApiModelProperty("籍贯")
    private String nativePlace = "";

    @ApiModelProperty("出生日期")
    private String bornDate = "";

    @ApiModelProperty("政治面貌")
    private String politicsStatus = "";

    @ApiModelProperty("最高学历")
    private String highestEdu = "";

    @ApiModelProperty("网址")
    private String url = "";

    @ApiModelProperty("简历")
    private String resume = "";

    private String phoneNum = "";

    private String email = "";


    // 就职单位
    @Relationship(type = RelationConstant.TAKE_OFFICE,direction = Relationship.OUTGOING)
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    private List<TakeOfficeRelation<Person, Company>> takeOfficeInCompany;

    // 政府的任职关系
    @Relationship(type = RelationConstant.TAKE_OFFICE,direction = Relationship.OUTGOING)
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    private List<TakeOfficeRelation<Person, Government>> takeOfficeInGovernment;
    // 股东关系
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.STOCKHOLDER,direction = Relationship.OUTGOING)
    private List<StockholderRelation<Person, Company>> stockHolderCompany;
    // 就读关系
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.ATTEND,direction = Relationship.OUTGOING)
    private List<AttendRelation<Person, School>> attendSchool;
    // 评标关系
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.EVALUATE_BID, direction = Relationship.OUTGOING)
    private List<EvaluateBid<Person, BidWinningAnnouncement>> EvaluatedBidWinningAnnouncements;
    // 朋友关系
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.IS_FRIEND_OF, direction = Relationship.OUTGOING)
    private List<IsFriendOf> myFriends;

    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.IS_FRIEND_OF, direction = Relationship.INCOMING)
    private List<IsFriendOf> beFriedOf;


    public static final Translator translator;
    static {
        List<TranslateDto> personProperties = new ArrayList<>();
        personProperties.add(new TranslateDto("id","id"));
        personProperties.add(new TranslateDto("名称","name"));
        personProperties.add(new TranslateDto("职位", "jobName"));
        personProperties.add(new TranslateDto("单位", "officeName"));
        personProperties.add(new TranslateDto("性别","gender"));
        personProperties.add(new TranslateDto("名族","nation"));
        personProperties.add(new TranslateDto("籍贯","nativePlace"));
        personProperties.add(new TranslateDto("出生日期","bornDate"));
        personProperties.add(new TranslateDto("政治面貌","politicsStatus"));
        personProperties.add(new TranslateDto("最高学历","highestEdu"));
        personProperties.add(new TranslateDto("网址","url"));
        personProperties.add(new TranslateDto("简历","resume"));
        personProperties.add(new TranslateDto("电话", "phoneNum"));
        translator = new Translator("人物", EntityConstant.PERSON, personProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.PERSON, this);
    }
}
