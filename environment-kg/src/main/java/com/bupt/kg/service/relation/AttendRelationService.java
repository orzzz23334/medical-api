package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.IncludeRelation;

public interface AttendRelationService {
    void deleteById(Long id);
    void update(AttendRelation attendRelation);
    void add(Long startNodeId, Long endNodeId, AttendRelation attendRelation);
    void moveTo(Long startNodeId, Long endNodeId, AttendRelation attendRelation);
}
