package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.IncludeRelation;

public interface IncludeRelationService {
    void update(IncludeRelation includeRelation);
    void deleteById(Long id);
    void add(Long startNodeId, Long endNodeId, IncludeRelation include);

    /**
     * 检查目标节点之间是否有相同的关系
     * 如果没有则先删除旧的再添加新的
     * 如果有则只删除旧的
     * @param startNodeId
     * @param endNodeId
     * @param includeRelation
     */
    void moveTo(Long startNodeId, Long endNodeId, IncludeRelation includeRelation);
}
