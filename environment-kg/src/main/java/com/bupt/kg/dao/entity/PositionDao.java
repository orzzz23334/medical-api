package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionDao extends Neo4jRepository<Position,Long> {
    List<Position> findPositionById(Long id);
    Page<Position> findAll(Pageable pageable);
    @Query("match (n:Position) return count(n)")
    Long getCount();

    // 当前仅根据名字可以根据需要进行拓展
    @Query(value = "MATCH (n:Position) WHERE n.name CONTAINS $fuzzyName return n",countQuery = "MATCH (n:Position) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Position> findPositionsByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
