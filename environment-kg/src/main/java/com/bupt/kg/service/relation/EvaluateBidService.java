package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.EvaluateBid;
import com.bupt.kg.model.relation.IncludeRelation;

public interface EvaluateBidService {
    void deleteById(Long id);
    void update(EvaluateBid evaluateBid);
    void add(Long startNodeId, Long endNodeId, EvaluateBid evaluateBid);
    void moveTo(Long startNodeId, Long endNodeId, EvaluateBid evaluateBid);
}
