package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.relation.PurchaseRelation;
import com.bupt.kg.model.relation.SupplierRelation;
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
@NodeEntity(EntityConstant.BID)
@ApiModel("中标实例")
public class Bid extends NodeAbstract{
    @Property
    @ApiModelProperty("名称")
    private String name;
    @Property
    @ApiModelProperty("时间")
    private String time;
    @Property
    @ApiModelProperty("信息来源")
    private String source;

    // 供应商
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.SUPPLY,direction = Relationship.INCOMING)
    private List<SupplierRelation<Supplier, Bid>> supplier;

    // 采购医院
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.PURCHASE,direction = Relationship.INCOMING)
    private List<PurchaseRelation<Hospital, Bid>> purchaserHospital;

    // 包含的医疗器械
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.OUTGOING)
    private List<IncludeRelation<Bid, MedicalEquipment>> includeMedicalEquipment;

    // 包含于的标段包组
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.INCOMING)
    private List<IncludeRelation<Package, Bid>> beIncludedPackage;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> bidProperties = new ArrayList<>();
        bidProperties.add(new TranslateDto("id", "id"));
        bidProperties.add(new TranslateDto("时间", "time"));
        bidProperties.add(new TranslateDto("信息来源", "source"));

        translator = new Translator("标", EntityConstant.BID, bidProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.BID, this);
    }
}
