package com.bupt.kg.model.relation;

import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.entity.NodeAbstract;
import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Builder
@Jacksonized
@RelationshipEntity(RelationConstant.SUPPLY)
public class SupplierRelation<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E> {
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
    public SupplierRelation getSupplierRelation() {
        return this;
    }

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto<>(RelationConstant.SUPPLY, startNode.getId(), endNode.getId(), this);
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
        translator = new Translator("供应", RelationConstant.SUPPLY, translateDtoList);
    }

    public boolean equalsByProperties(SupplierRelation supplierRelation) {
//        if(this.startTime.equals(supplierRelation.getStartTime()) &&
//                this.endTime.equals(supplierRelation.getEndTime()) &&
//                this.ratio.equals(supplierRelation.getRatio()) &&
//                this.amount.equals(supplierRelation.getAmount()) &&
//                this.relation.equals(supplierRelation.getRelation())){
//            return true;
//        }
        return false;
    }
}
