package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierDao extends Neo4jRepository<Supplier, Long> {
    List<Supplier> findSupplierById(Long id);
    @Override
    Page<Supplier> findAll(Pageable pageable);

    @Query("match (n:Supplier) return count(n)")
    Long getCount();
    @Query(value = "MATCH (n:Supplier) WHERE n.name CONTAINS $fuzzyName return n", countQuery = "MATCH (n:Supplier) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Supplier> findSupplierByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
