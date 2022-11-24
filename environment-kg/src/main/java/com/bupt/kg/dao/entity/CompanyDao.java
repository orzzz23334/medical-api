package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyDao extends Neo4jRepository<Company,Long> {
    List<Company> findCompanyById(Long id);
    Page<Company> findAll(Pageable pageable);
    @Query("match (n:Company) return count(n)")
    Long getCount();

    // 当前仅根据名字可以根据需要进行拓展
    @Query(value = "MATCH (n:Company) WHERE n.name CONTAINS $fuzzyName return n",countQuery = "MATCH (n:Company) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Company> findCompaniesByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);

}
