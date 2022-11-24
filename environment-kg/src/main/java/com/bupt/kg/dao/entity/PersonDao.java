package com.bupt.kg.dao.entity;


import com.bupt.kg.model.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Repository
public interface PersonDao extends Neo4jRepository<Person,Long> {
    List<Person> findPersonByName(String name, @Depth int depth);
    List<Person> findPersonById(Long id);

    Page<Person> findAll(Pageable pageable);

    @Query("match (n:Person) with  n.name as name, count(n) as num where num > 1  return name;")
    List<String> findPersonNamesByhasSamePersonName();

    @Query("match (n:Person) return count(n)")
    Long getCount();


    // 当前仅根据名字可以根据需要进行拓展
    @Query(value = "MATCH (n:Person) WHERE n.name CONTAINS $fuzzyName return n",countQuery = "MATCH (n:Person) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Person> findPeopleByFuzzyPersonName(@Param("fuzzyName") String fuzzyName, Pageable pageable);

    // 当前根据名字和公司名可以根据需要进行拓展
    @Query(value = "MATCH (n:Person)-[:TAKE_OFFICE]->(c:Company) WHERE n.name CONTAINS $fuzzyPersonName and c.name CONTAINS $fuzzyCompanyName return n",countQuery = "MATCH (n:Person)-[:TAKE_OFFICE]->(c:Company) WHERE n.name CONTAINS $fuzzyPersonName and c.name CONTAINS $fuzzyCompanyName return count(n)")
    Page<Person> findPeopleBycompanyName(@Param("fuzzyPersonName") String fuzzyName, Pageable pageable, @Param("fuzzyCompanyName") String companyName);

}
