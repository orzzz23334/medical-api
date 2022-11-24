package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.Supplier;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SupplierService {
    GraphData getSupplierGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getSupplierRelationTablesById(Long id);
    Supplier getSupplierInfoById(Long id);
    PageData<Supplier> getSupplierPageByFuzzySearch(Supplier supplier, PageRequest pageRequest);
    Long getCount();

    void add(Supplier supplier);

    Supplier getSupplierWithAllRelationshipById(Long id);
}
