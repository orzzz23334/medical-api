package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.Project;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProjectService {
    GraphData getProjectGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getProjectRelationTablesById(Long id);
    Project getProjectInfoById(Long id);
    PageData<Project> getProjectPageByFuzzySearch(Project project, PageRequest pageRequest);
    Long getCount();

    void add(Project project);

    Project getProjectWithAllRelationshipById(Long id);
}
