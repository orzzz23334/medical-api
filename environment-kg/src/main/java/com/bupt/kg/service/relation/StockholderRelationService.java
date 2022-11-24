package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.relation.StockholderRelation;

public interface StockholderRelationService {
    void update(StockholderRelation stockholderRelation);
    void deleteById(Long id);
    void add(Long startNodeId, Long endNodeId, StockholderRelation stockholder);
    void moveTo(Long startNodeId, Long endNodeId, StockholderRelation stockholder);
}
