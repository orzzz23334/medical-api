package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.IncludeRelation;

public interface AgentService {
    void deleteById(Long id);
    void update(Agent agent);
    void add(Long startNodeId, Long endNodeId, Agent agent);
    void moveTo(Long startNodeId, Long endNodeId, Agent agent);
}
