package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackageDao extends Neo4jRepository<Package, Long> {
    List<Package> findPackageById(Long id);
    @Override
    Page<Package> findAll(Pageable pageable);

    @Query("match (n:Package) return count(n)")
    Long getCount();
    @Query(value = "MATCH (n:Package) WHERE n.name CONTAINS $fuzzyName return n", countQuery = "MATCH (n:Package) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Package> findPackageByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
