package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.MedicalEquipment;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MedicalEquipmentService {
    GraphData getMedicalEquipmentGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getMedicalEquipmentRelationTablesById(Long id);
    MedicalEquipment getMedicalEquipmentInfoById(Long id);
    PageData<MedicalEquipment> getMedicalEquipmentPageByFuzzySearch(MedicalEquipment medicalEquipment, PageRequest pageRequest);
    Long getCount();

    void add(MedicalEquipment medicalEquipment);

    MedicalEquipment getMedicalEquipmentWithAllRelationshipById(Long id);
}
