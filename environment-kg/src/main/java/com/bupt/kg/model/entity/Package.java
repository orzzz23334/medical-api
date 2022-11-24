package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.vo.translate.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@NodeEntity(EntityConstant.PACKAGE)
@ApiModel("标段包组")
public class Package extends NodeAbstract{
    @Property
    @ApiModelProperty("名称")
    private String name;

    // 包含的中标实例
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.OUTGOING)
    private List<IncludeRelation<Package, Bid>> beIncludedBid;

    // 包含于的中标公告
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.INCOMING)
    private List<IncludeRelation<BidWinningAnnouncement, Package>> includedBidWinningAnnouncement;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> packageProperties = new ArrayList<>();
        packageProperties.add(new TranslateDto("id", "id"));
        packageProperties.add(new TranslateDto("名称", "name"));

        translator = new Translator("标段包组", EntityConstant.PACKAGE, packageProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.PACKAGE, this);
    }
}
