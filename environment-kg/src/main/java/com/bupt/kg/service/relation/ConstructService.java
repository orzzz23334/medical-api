package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.Construct;
import com.bupt.kg.model.relation.IncludeRelation;

public interface ConstructService {
    void update(Construct construct);
    void deleteById(Long id);
    void add(Long startNodeId, Long endNodeId, Construct construct);
    void moveTo(Long startNodeId, Long endNodeId, Construct construct);
}
