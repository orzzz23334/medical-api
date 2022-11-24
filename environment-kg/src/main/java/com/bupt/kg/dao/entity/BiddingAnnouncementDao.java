package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.BiddingAnnouncement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BiddingAnnouncementDao extends Neo4jRepository<BiddingAnnouncement, Long> {
    List<BiddingAnnouncement> findBiddingAnnouncementById(Long id);
    Page<BiddingAnnouncement> findAll(Pageable pageable);

    @Query("match (n:BiddingAnnouncement) return count(n)")
    Long getCount();

    @Query(value = "MATCH (n:BiddingAnnouncement) WHERE n.name CONTAINS $fuzzyName return n order by n.releaseTime DESC", countQuery = "MATCH (n:BiddingAnnouncement) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<BiddingAnnouncement> findBiddingAnnouncementByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
