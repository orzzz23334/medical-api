package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidDao extends Neo4jRepository<Bid, Long> {
    List<Bid> findBidById(Long id);
    @Override
    Page<Bid> findAll(Pageable pageable);

    @Query("match (n:Bid) return count(n)")
    Long getCount();
    @Query(value = "MATCH (n:Bid) WHERE n.name CONTAINS $fuzzyName return n", countQuery = "MATCH (n:Bid) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<Bid> findBidByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);

}
