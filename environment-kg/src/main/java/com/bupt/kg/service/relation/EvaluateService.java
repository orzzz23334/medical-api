package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.Evaluate;
import com.bupt.kg.model.relation.IncludeRelation;

public interface EvaluateService {
    void update(Evaluate evaluate);
    void deleteById(Long id);
    void add(Long startNodeId, Long endNodeId, Evaluate evaluate);
    void moveTo(Long startNodeId, Long endNodeId, Evaluate evaluate);
}
