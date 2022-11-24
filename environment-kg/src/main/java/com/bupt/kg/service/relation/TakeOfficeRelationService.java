package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.relation.TakeOfficeRelation;

public interface TakeOfficeRelationService {
    void update(TakeOfficeRelation takeOfficeRelation);
    void deleteById(Long id);
    void add(Long startNodeId, Long endNodeId, TakeOfficeRelation takeOffice);
    void moveTo(Long startNodeId, Long endNodeId, TakeOfficeRelation takeOffice);
}
