package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectDao extends Neo4jRepository<Project, Long> {
    List<Project> findProjectById(Long id);
    @Override
    Page<Project> findAll(Pageable pageable);

    @Query("match (n:Project) return count(n)")
    Long getCount();
    @Query(value = "MATCH (n:Project) WHERE n.name CONTAINS $fuzzyName return n", countQuery = "MATCH (n:Project) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Project> findProjectByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
