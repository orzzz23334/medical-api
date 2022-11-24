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
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@RelationshipEntity(RelationConstant.WINBID)
public class WinBid<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E> {
    @Property
    private String bidAmount = "";
    @Property
    private String contact = "";

    @Override
    public RelationDto buildRelationDto() {
        return new RelationDto(RelationConstant.WINBID, startNode.getId(), endNode.getId(), this);
    }

    public static final Translator translator;
    static {
        List<TranslateDto> translateDtoList = new ArrayList<>();
        translateDtoList.add(new TranslateDto("id", "id"));
        translateDtoList.add(new TranslateDto("中标金额", "bidAmount"));
        translateDtoList.add(new TranslateDto("联系方式", "contact"));
        translator = new Translator("中标", RelationConstant.WINBID, translateDtoList);
    }

    public boolean equalsByProperties(WinBid winBid) {
        if(this.bidAmount.equals(winBid.getBidAmount()) &&
                this.contact.equals(winBid.getContact())){
            return true;
        }
        return false;
    }
}
