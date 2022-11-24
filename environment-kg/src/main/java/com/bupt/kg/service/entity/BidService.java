package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BidService {
    GraphData getBidGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getBidRelationTablesById(Long id);

    Bid getBidInfoById(Long id);
    PageData<Bid> getBidPageByFuzzySearch(Bid bid, PageRequest pageRequest);
    Long getCount();

    void add(Bid bid);
    void update(Bid bid);
    void deleteById(Long id);

    // 融合两个节点
    void mergeNode(Long masterNodeId, Long slaverNodeId);

    Bid getBidWithAllRelationshipById(Long id);
}
