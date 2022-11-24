package com.bupt.kg.model.relation;

import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.dto.transferable.RelationTransferable;
import com.bupt.kg.model.entity.NodeAbstract;
import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Builder @Jacksonized
@RelationshipEntity(RelationConstant.STOCKHOLDER)
public class StockholderRelation<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E>{
    @Property
//    @JsonProperty("认缴出资日期")
    private String startTime = "";
    @Property
//    @JsonProperty("撤资日期")
    private String endTime = "";
    @Property
//    @JsonProperty("持股比例")
    private String shareholdingRatio = "";
    @Property
//    @JsonProperty("最终受益股份")
    private String ultimateBeneficialShare = "";
    @Property
//    @JsonProperty("持股数")
    private String numberOfShare = "";
    @Property
//    @JsonProperty("认缴出资金额")
    private String investmentAmount = "";
    @Property
//    @JsonProperty("股份类型")
    private String shareType = "";
    @Property
//    @JsonProperty("参控关系")
    private String holdingRelationship = "";

    @JsonIgnore
    public StockholderRelation getStockholderRelation(){
        return this;
    }

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto<>(RelationConstant.STOCKHOLDER, startNode.getId(), endNode.getId(), this);
    }

    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
        translateDtoList.add(new TranslateDto("认缴出资日期", "startTime"));
        translateDtoList.add(new TranslateDto("撤资日期", "endTime"));
        translateDtoList.add(new TranslateDto("持股比例", "shareholdingRatio"));
        translateDtoList.add(new TranslateDto("最终受益股份", "ultimateBeneficialShare"));
        translateDtoList.add(new TranslateDto("持股数", "numberOfShare"));
        translateDtoList.add(new TranslateDto("认缴出资金额", "investmentAmount"));
        translateDtoList.add(new TranslateDto("股份类型", "shareType"));
        translateDtoList.add(new TranslateDto("参控关系", "holdingRelationship"));
        translator = new Translator("参股", RelationConstant.STOCKHOLDER, translateDtoList);
    }

    public boolean equalsByProperties(StockholderRelation stockholderRelation) {
        if(this.startTime.equals(stockholderRelation.getStartTime()) &&
                this.endTime.equals(stockholderRelation.getEndTime()) &&
                this.shareholdingRatio.equals(stockholderRelation.getShareholdingRatio()) &&
                this.ultimateBeneficialShare.equals(stockholderRelation.getUltimateBeneficialShare()) &&
                this.numberOfShare.equals(stockholderRelation.getNumberOfShare()) &&
                this.investmentAmount.equals(stockholderRelation.getInvestmentAmount()) &&
                this.shareType.equals(stockholderRelation.getShareType()) &&
                this.holdingRelationship.equals(stockholderRelation.getHoldingRelationship())){
            return true;
        }
        return false;
    }
}
