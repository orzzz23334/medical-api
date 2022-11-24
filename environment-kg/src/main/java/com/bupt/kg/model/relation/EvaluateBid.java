package com.bupt.kg.model.relation;

import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.entity.NodeAbstract;
import com.bupt.kg.model.vo.translate.Translator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Jacksonized
@RelationshipEntity(RelationConstant.EVALUATE_BID)
public class EvaluateBid<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E> {

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto(RelationConstant.EVALUATE_BID, startNode.getId(), endNode.getId(), this);
    }

    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
        translator = new Translator("评标", RelationConstant.EVALUATE_BID, translateDtoList);
    }

    public boolean equalsByProperties(EvaluateBid evaluateBid) {return true;}
}
