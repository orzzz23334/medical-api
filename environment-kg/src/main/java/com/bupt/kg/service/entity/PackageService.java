package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.Package;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PackageService {
    GraphData getPackageGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getPackageRelationTablesById(Long id);
    Package getPackageInfoById(Long id);
    PageData<Package> getPackagePageByFuzzySearch(Package pkg, PageRequest pageRequest);
    Long getCount();

    void add(Package pkg);

    Package getPackageWithAllRelationshipById(Long id);
}
