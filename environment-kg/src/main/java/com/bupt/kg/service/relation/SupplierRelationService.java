package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.relation.SupplierRelation;

public interface SupplierRelationService {
    void update(SupplierRelation supplierRelation);
    void deleteById(Long id);
    void add(Long startNodeId, Long endNodeId, SupplierRelation supplier);
    void moveTo(Long startNodeId, Long endNodeId, SupplierRelation supplier);
}
