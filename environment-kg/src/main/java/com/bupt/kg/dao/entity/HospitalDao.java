package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalDao extends Neo4jRepository<Hospital, Long> {
    List<Hospital> findHospitalById(Long id);
    @Override
    Page<Hospital> findAll(Pageable pageable);

    @Query("match (n:Hospital) return count(n)")
    Long getCount();
    @Query(value = "MATCH (n:Hospital) WHERE n.name CONTAINS $fuzzyName return n", countQuery = "MATCH (n:Hospital) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Hospital> findHospitalByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
