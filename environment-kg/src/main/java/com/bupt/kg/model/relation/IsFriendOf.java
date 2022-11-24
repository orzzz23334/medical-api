package com.bupt.kg.model.relation;

import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.entity.NodeAbstract;
import com.bupt.kg.model.entity.Person;
import com.bupt.kg.model.vo.translate.Translator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@RelationshipEntity(RelationConstant.IS_FRIEND_OF)
public class IsFriendOf extends RelationAbstract<Person, Person>{
    @Property
    private String relationType = "";
    @Property
    private Integer degree = 0;
    @Property
    private String describe = "";
    @Property
    private String startTime = "";
    @Property
    private String InfoChannel = "";
    @Property
    private String dataSource = "";

    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
        translateDtoList.add(new TranslateDto("关系类型", "relationType"));
        translateDtoList.add(new TranslateDto("熟识度", "degree"));
        translateDtoList.add(new TranslateDto("关系描述", "describe"));
        translateDtoList.add(new TranslateDto("开始时间", "startTime"));

        translator = new Translator("朋友", RelationConstant.IS_FRIEND_OF, translateDtoList);

    }

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto(RelationConstant.IS_FRIEND_OF, this.startNode.id, this.endNode.id, this);
    }

    public boolean equalsByProperties(IsFriendOf isFriendOf) {
        if (this.relationType.equals(isFriendOf.getRelationType()) &&
                this.degree.equals(isFriendOf.getDegree()) &&
                this.describe.equals(isFriendOf.getDescribe()) &&
                this.startTime.equals(isFriendOf.getStartTime()) &&
                this.InfoChannel.equals(isFriendOf.getInfoChannel()) &&
                this.dataSource.equals(isFriendOf.getDataSource())) {
            return true;
        }
        return false;
    }
}
