package com.bupt.kg.model.relation;

import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.dto.transferable.RelationTransferable;
import com.bupt.kg.model.entity.NodeAbstract;
import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Jacksonized
@RelationshipEntity(RelationConstant.EVALUATE)
public class Evaluate<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E> {
    @Property
    private String contact = "";


    @JsonIgnore
    public Evaluate getEvaluate(){
        return this;
    }

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto<>(RelationConstant.EVALUATE, startNode.getId(), endNode.getId(), this);
    }
    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
        translateDtoList.add(new TranslateDto("联系方式", "contact"));
        translator = new Translator("评价", RelationConstant.EVALUATE, translateDtoList);
    }

    public boolean equalsByProperties(Evaluate evaluate) {
        if(this.contact.equals(evaluate.getContact())){
            return true;
        }
        return false;
    }
}
