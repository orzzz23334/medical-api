package com.bupt.kg.service.entity;

import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.entity.Government;
import com.bupt.kg.model.vo.RelationTableData;
import io.swagger.models.auth.In;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface GovernmentService {
    GraphData getGovernmentGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getGovernmentRelationTablesById(Long id);

    Government getGovernmentInfoById(Long id);
    PageData<Government> getGovernmentPage(PageRequest pageRequest);
    PageData<Government> getGovernmentPageByFuzzySearch(Government government, PageRequest pageRequest);
    Long getCount();
    void add(Government government);
    void update(Government government);
    void deleteById(Long id);

    // 融合两个节点
    void mergeNode(Long masterNodeId, Long slaverNodeId);

    Government getGovernmentWithAllRelationshipById(Long id);
}
