package com.bupt.kg.model.relation;

import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.dto.transferable.RelationTransferable;
import com.bupt.kg.model.entity.NodeAbstract;
import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.auth.In;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
@AllArgsConstructor
@Builder @Jacksonized
@RelationshipEntity(RelationConstant.INCLUDE)
public class IncludeRelation<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E> {
    @Property
//    @JsonProperty("数量")
    private String amount = "";
    @Property
//    @JsonProperty("出价")
    private String price = "";

    @JsonIgnore
    public IncludeRelation getIncludeRelation(){
        return this;
    }

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto<>(RelationConstant.INCLUDE, startNode.getId(), endNode.getId(), this);
    }
    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
        translateDtoList.add(new TranslateDto("数量", "amount"));
        translateDtoList.add(new TranslateDto("出价", "price"));
        translator = new Translator("包含", RelationConstant.INCLUDE, translateDtoList);
    }

    public boolean equalsByProperties(IncludeRelation includeRelation) {
        return true;
    }
}
