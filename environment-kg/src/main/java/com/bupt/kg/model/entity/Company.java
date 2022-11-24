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

@Data @NoArgsConstructor @AllArgsConstructor
@Builder @Jacksonized
@NodeEntity
@ApiModel("公司")
public class Company extends NodeAbstract{

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("英文名称")
    private String englishName;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("别称")
    private String otherName;

    @ApiModelProperty("统一社会信用代码")
    @Property("USCC")
    private String uscc;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("网址")
    private String url;

    @ApiModelProperty("经营范围")
    private String businessScope;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("企业类型")
    private String businessForm;

    @ApiModelProperty("成立日期")
    private String bornDate;

    @ApiModelProperty("所属行业")
    private String industry;

    @ApiModelProperty("行业代码")
    private String industryCode;

    @ApiModelProperty("组织机构代码")
    private String organizationCode;

    @ApiModelProperty("注册资金")
    private String registerFund;

    @ApiModelProperty("电话")
    private String phoneNumber;

    @ApiModelProperty("传真")
    private String fax;

    @ApiModelProperty("邮编")
    private String postcode;

    @ApiModelProperty("公司简介")
    private String introduction;
    // 控股当前公司的公司
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.STOCKHOLDER, direction = Relationship.INCOMING)
    private List<StockholderRelation<Company, Company>> stockHolderCompany;// TODO:
    // 当前公司的控股的公司
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.STOCKHOLDER, direction = Relationship.OUTGOING)
    private List<StockholderRelation<Company, Company>> beStockedCompany;
    // 控股当前公司的人
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.STOCKHOLDER, direction = Relationship.INCOMING)
    private List<StockholderRelation<Person, Company>> stockHolderPerson; // TODO:
    // 任职到这个公司的人
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.TAKE_OFFICE, direction = Relationship.INCOMING)
    private List<TakeOfficeRelation<Person, Company>> takeOfficePerson;

    // 当前公司所属的地区
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ToString.Exclude
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.INCOMING)
    private List<IncludeRelation<Position, Company>> includePosition;

    // 当前公司评价的项目
    @JsonIgnore
    @ToString.Exclude
    @Relationship(type = RelationConstant.EVALUATE, direction = Relationship.OUTGOING)
    private List<Evaluate<Company, EnvAssessmentAnnouncement>> evaluates;

    // 当前公司建设的项目（环评）
    @JsonIgnore
    @ToString.Exclude
    @Relationship(type = RelationConstant.CONSTRUCT, direction = Relationship.OUTGOING)
    private List<Construct<Company, EnvAssessmentAnnouncement>> constructs;

    // 当前公司建设的项目(招标)
    @JsonIgnore
    @ToString.Exclude
    @Relationship(type = RelationConstant.CONSTRUCT, direction = Relationship.OUTGOING)
    private List<Construct<Company, BiddingAnnouncement>> constructsForBiddingAnnouncement;

    // 当前公司代理的项目招标（招标）
    @JsonIgnore
    @ToString.Exclude
    @Relationship(type = RelationConstant.AGENT, direction = Relationship.OUTGOING)
    private List<Agent<Company, BiddingAnnouncement>> agentsForBiddingAnnouncement;

    // 当前公司建设的项目（中标）
    @JsonIgnore
    @ToString.Exclude
    @Relationship(type = RelationConstant.CONSTRUCT, direction = Relationship.OUTGOING)
    private List<Construct<Company, BidWinningAnnouncement>> constructsForBidWinningAnnouncement;

    // 当前公司代理的项目（中标）
    @JsonIgnore
    @ToString.Exclude
    @Relationship(type = RelationConstant.AGENT, direction = Relationship.OUTGOING)
    private List<Agent<Company, BidWinningAnnouncement>> agentsForBidWinningAnnouncement;

    // 当前公司中标的项目
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type = RelationConstant.WINBID, direction = Relationship.OUTGOING)
    List<WinBid<Company, BidWinningAnnouncement>> winBidsForBidWinningAnnouncement;

    // 当前公司的子公司
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.OUTGOING)
    List<IncludeRelation<Company, Company>> childCompanys;

    // 当前公司的父公司
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type = RelationConstant.INCLUDE, direction = Relationship.INCOMING)
    List<IncludeRelation<Company, Company>> parentCompanys;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> companyProperties = new ArrayList<>();
        companyProperties.add(new TranslateDto("id","id"));
        companyProperties.add(new TranslateDto("名称","name"));
        companyProperties.add(new TranslateDto("英文名称","englishName"));
        companyProperties.add(new TranslateDto("地址","address"));
        companyProperties.add(new TranslateDto("别称","otherName"));
        companyProperties.add(new TranslateDto("统一社会信用代码","uscc"));
        companyProperties.add(new TranslateDto("邮箱","email"));
        companyProperties.add(new TranslateDto("网址","url"));
        companyProperties.add(new TranslateDto("经营范围","businessScope"));
        companyProperties.add(new TranslateDto("产品名称","productName"));
        companyProperties.add(new TranslateDto("企业类型","businessForm"));
        companyProperties.add(new TranslateDto("所属行业","industry"));
        companyProperties.add(new TranslateDto("行业代码","industryCode"));
        companyProperties.add(new TranslateDto("组织机构代码","organizationCode"));
        companyProperties.add(new TranslateDto("注册资金","registerFund"));
        companyProperties.add(new TranslateDto("电话","phoneNumber"));
        companyProperties.add(new TranslateDto("传真","fax"));
        companyProperties.add(new TranslateDto("邮编","postcode"));
        companyProperties.add(new TranslateDto("公司简介","introduction"));

        translator = new Translator("公司", EntityConstant.COMPANY, companyProperties);
    }


    /**
     * 构建本entity的DTO
     */
    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.COMPANY, this);
    }
}
