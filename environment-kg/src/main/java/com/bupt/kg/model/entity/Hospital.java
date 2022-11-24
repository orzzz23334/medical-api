package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.PurchaseRelation;
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
@NodeEntity(EntityConstant.HOSPITAL)
@ApiModel("医院")
public class Hospital extends NodeAbstract{
    @Property
    @ApiModelProperty("名称")
    private String name;
    @Property
    @ApiModelProperty("地址")
    private String address;
    @Property
    @ApiModelProperty("负责人")
    private String principal;
    @Property
    @ApiModelProperty("联系方式")
    private String contactInfo;

    // 采购的中标实例
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.PURCHASE,direction = Relationship.OUTGOING)
    private List<PurchaseRelation<Hospital, Bid>> purchaseBid;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> hospitalProperties = new ArrayList<>();
        hospitalProperties.add(new TranslateDto("id", "id"));
        hospitalProperties.add(new TranslateDto("名称", "name"));
        hospitalProperties.add(new TranslateDto("地址", "address"));
        hospitalProperties.add(new TranslateDto("负责人", "principal"));
        hospitalProperties.add(new TranslateDto("联系方式", "contactInfo"));

        translator = new Translator("医院", EntityConstant.HOSPITAL, hospitalProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.HOSPITAL, this);
    }
}
