package com.bupt.kg.dao.entity;


import com.bupt.kg.model.entity.Government;
import com.bupt.kg.model.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GovernmentDao extends Neo4jRepository<Government,Long> {
    List<Government> findGovernmentById(Long id);
    Page<Government> findAll(Pageable pageable);
    @Query("match (n:Government) return count(n)")
    Long getCount();

    // 当前仅根据名字可以根据需要进行拓展
    @Query(value = "MATCH (n:Government) WHERE n.name CONTAINS $fuzzyName return n",countQuery = "MATCH (n:Government) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Government> findGovernmentsByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
