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
@NodeEntity(EntityConstant.MEDICAL_EQUIPMENT)
@ApiModel("医疗器械")
public class MedicalEquipment extends NodeAbstract{
    @Property
    @ApiModelProperty("名称")
    private String name;
    @Property
    @ApiModelProperty("品牌")
    private String brand;
    @Property
    @ApiModelProperty("型号规格")
    private String specs;
    @Property
    @ApiModelProperty("分类")
    private String category;
    @Property
    @ApiModelProperty("参数")
    private String param;
    @Property
    @ApiModelProperty("制造商")
    private String manufacturer;

    // 包含于的中标实例
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.INCLUDE,direction = Relationship.INCOMING)
    private List<IncludeRelation<Bid, MedicalEquipment>> includedBid;


    public static final Translator translator;
    static {
        ArrayList<TranslateDto> medicalEquipmentProperties = new ArrayList<>();
        medicalEquipmentProperties.add(new TranslateDto("id", "id"));
        medicalEquipmentProperties.add(new TranslateDto("名称", "name"));
        medicalEquipmentProperties.add(new TranslateDto("品牌", "brand"));
        medicalEquipmentProperties.add(new TranslateDto("型号规格", "specs"));
        medicalEquipmentProperties.add(new TranslateDto("分类", "category"));
        medicalEquipmentProperties.add(new TranslateDto("参数", "param"));
        medicalEquipmentProperties.add(new TranslateDto("制造商", "manufacturer"));

        translator = new Translator("医疗器械", EntityConstant.MEDICAL_EQUIPMENT, medicalEquipmentProperties);
    }

    @Override
    public NodeDto buildNodeDto() {
        return new NodeDto<>(EntityConstant.MEDICAL_EQUIPMENT, this);
    }
}
