package com.bupt.kg.model.entity;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.TranslateDto;
import com.bupt.kg.model.relation.AttendRelation;
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
@NodeEntity(EntityConstant.SUPPLIER)
@ApiModel("供应商")
public class Supplier extends NodeAbstract{
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

    // 供货的中标实例
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.SUPPLY,direction = Relationship.OUTGOING)
    private List<SupplierRelation<Supplier, Bid>> supplyBid;

    public static final Translator translator;
    static {
        ArrayList<TranslateDto> supplierProperties = new ArrayList<>();
        supplierProperties.add(new TranslateDto("id", "id"));
        supplierProperties.add(new TranslateDto("名称", "name"));
        supplierProperties.add(new TranslateDto("地址", "address"));
        supplierProperties.add(new TranslateDto("负责人", "principal"));
        supplierProperties.add(new TranslateDto("联系方式", "contactInfo"));

        translator = new Translator("供应商", EntityConstant.SUPPLIER, supplierProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.SUPPLIER, this);
    }
}
