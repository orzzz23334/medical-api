package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Person;
import com.bupt.kg.model.entity.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchoolDao extends Neo4jRepository<School,Long> {
    List<School> findSchoolById(Long id);

    Page<School> findAll(Pageable pageable);
    @Query("match (n:School) return count(n)")
    Long getCount();


    // 当前仅根据名字可以根据需要进行拓展
    @Query(value = "MATCH (n:School) WHERE n.name CONTAINS $fuzzyName return n",countQuery = "MATCH (n:School) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<School> findSchoolsByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
