package com.bupt.kg.service.entity;

import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.entity.School;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SchoolService {
    GraphData getSchoolGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getSchoolRelationTablesById(Long id);

    School getSchoolInfoById(Long id);
    PageData<School> getSchoolPage(PageRequest pageRequest);
    PageData<School> getSchoolPageByFuzzySearch(School school, PageRequest pageRequest);
    Long getCount();

    void add(School school);
    void update(School school);
    void deleteById(Long id);

    School getSchoolWithAllRelationshipById(Long id);
}
