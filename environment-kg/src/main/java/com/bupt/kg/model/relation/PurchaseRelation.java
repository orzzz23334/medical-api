package com.bupt.kg.model.relation;

import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.entity.NodeAbstract;
import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@Jacksonized
@RelationshipEntity(RelationConstant.PURCHASE)
public class PurchaseRelation<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E> {
//    @Property
////    @JsonProperty("开始时间")
//    private String startTime = "";
//    @Property
////    @JsonProperty("结束时间")
//    private String endTime = "";
//    @Property
////    @JsonProperty("采购占比")
//    private String ratio = "";
//    @Property
////    @JsonProperty("采购金额")
//    private String amount = "";
//    @Property
////    @JsonProperty("关联关系")
//    private String relation = "";


    @JsonIgnore
    public PurchaseRelation getPurchaseRelation() {
        return this;
    }

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto<>(RelationConstant.PURCHASE, startNode.getId(), endNode.getId(), this);
    }

    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
//        translateDtoList.add(new TranslateDto("开始时间","startTime"));
//        translateDtoList.add(new TranslateDto("结束时间","endTime"));
//        translateDtoList.add(new TranslateDto("采购占比","ratio"));
//        translateDtoList.add(new TranslateDto("采购金额","amount"));
//        translateDtoList.add(new TranslateDto("关联关系","relation"));
        translator = new Translator("采购", RelationConstant.PURCHASE, translateDtoList);
    }

    public boolean equalsByProperties(PurchaseRelation purchaseRelation) {
//        if(this.startTime.equals(purchaseRelation.getStartTime()) &&
//                this.endTime.equals(purchaseRelation.getEndTime()) &&
//                this.ratio.equals(purchaseRelation.getRatio()) &&
//                this.amount.equals(purchaseRelation.getAmount()) &&
//                this.relation.equals(purchaseRelation.getRelation())){
//            return true;
//        }
        return false;
    }
}
