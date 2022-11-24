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
@RelationshipEntity(RelationConstant.TAKE_OFFICE)
public class TakeOfficeRelation<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E> {
    @Property
//    @JsonProperty("开始时间")
    private String startTime = "";
    @Property
//    @JsonProperty("结束时间")
    private String endTime = "";
    @Property
//    @JsonProperty("职务名称")
    private String name = "";
    @Property
//    @JsonProperty("薪酬")
    private String payment = "";


    @JsonIgnore
    public TakeOfficeRelation getTakeOfficeRelation(){
        return this;
    }

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto<>(RelationConstant.TAKE_OFFICE, startNode.getId(), endNode.getId(), this);
    }
    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
        translateDtoList.add(new TranslateDto("开始时间","startTime"));
        translateDtoList.add(new TranslateDto("结束时间","endTime"));
        translateDtoList.add(new TranslateDto("职务名称","name"));
        translateDtoList.add(new TranslateDto("薪酬","payment"));
        translator = new Translator("就职", RelationConstant.TAKE_OFFICE, translateDtoList);
    }

    public boolean equalsByProperties(TakeOfficeRelation takeOfficeRelation) {
        if(this.startTime.equals(takeOfficeRelation.getStartTime()) &&
                this.endTime.equals(takeOfficeRelation.getEndTime()) &&
                this.name.equals(takeOfficeRelation.getName()) &&
                this.payment.equals(takeOfficeRelation.getPayment())){
            return true;
        }
        return false;
    }
}
