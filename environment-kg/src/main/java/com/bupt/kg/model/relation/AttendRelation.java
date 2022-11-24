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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@RelationshipEntity(RelationConstant.ATTEND)
public class AttendRelation<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E>{
    @Property
//    @JsonProperty("开始时间")
    private String startTime = "";
    @Property
//    @JsonProperty("结束时间")
    private String endTime = "";
    @Property
//    @JsonProperty("学位")
    private String degree = "";
    @Property
//    @JsonProperty("专业")
    private String major = "";


    @JsonIgnore
    public AttendRelation getAttendRelation(){
        return this;
    }

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto<>(RelationConstant.ATTEND, startNode.getId(), endNode.getId(), this);
    }
    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
        translateDtoList.add(new TranslateDto("开始时间", "startTime"));
        translateDtoList.add(new TranslateDto("结束时间", "endTime"));
        translateDtoList.add(new TranslateDto("学位", "degree"));
        translateDtoList.add(new TranslateDto("专业", "major"));
        translator = new Translator("就读", RelationConstant.ATTEND, translateDtoList);
    }

    public boolean equalsByProperties(AttendRelation attendRelation) {
        if(this.startTime.equals(attendRelation.getStartTime()) &&
                this.endTime.equals(attendRelation.getEndTime()) &&
                this.degree.equals(attendRelation.getDegree()) &&
                this.major.equals(attendRelation.getMajor())){
            return true;
        }
        return false;
    }
}
