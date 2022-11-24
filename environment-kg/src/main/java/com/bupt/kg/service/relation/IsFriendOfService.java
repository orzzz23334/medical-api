package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.relation.IsFriendOf;
import com.bupt.kg.model.relation.SupplierRelation;

public interface IsFriendOfService {
    void update(IsFriendOf isFriendOf);
    void deleteById(Long id);
    void add(Long startNodeId, Long endNodeId, IsFriendOf isFriendOf);
    void moveTo(Long startNodeId, Long endNodeId, IsFriendOf isFriendOf);
}
