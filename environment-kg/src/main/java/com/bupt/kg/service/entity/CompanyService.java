package com.bupt.kg.service.entity;

import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;


public interface CompanyService {
    GraphData getCompanyGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getCompanyRelationTablesById(Long id);

    Company getCompanyInfoById(Long id);
    PageData<Company> getCompanyPage(PageRequest pageRequest);
    PageData<Company> getCompanyPageByFuzzySearch(Company company, PageRequest pageRequest);
    Long getCount();
    void add(Company company);
    void update(Company company);
    void deleteById(Long id);

    // 融合两个节点
    void mergeNode(Long masterNodeId, Long slaverNodeId);

    Company getCompanyWithAllRelationshipById(Long id);
}
