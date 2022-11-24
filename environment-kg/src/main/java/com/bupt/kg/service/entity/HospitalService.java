package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.Hospital;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface HospitalService {
    GraphData getHospitalGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getHospitalRelationTablesById(Long id);
    Hospital getHospitalInfoById(Long id);
    PageData<Hospital> getHospitalPageByFuzzySearch(Hospital hospital, PageRequest pageRequest);
    Long getCount();

    void add(Hospital hospital);

    Hospital getHospitalWithAllRelationshipById(Long id);
}
