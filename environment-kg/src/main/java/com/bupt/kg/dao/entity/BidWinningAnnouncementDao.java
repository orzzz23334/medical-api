package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidWinningAnnouncementDao extends Neo4jRepository<BidWinningAnnouncement, Long> {
    List<BidWinningAnnouncement> findBidWinningAnnouncementById(Long id);
    Page<BidWinningAnnouncement> findAll(Pageable pageable);

    @Query("match (n:BidWinningAnnouncement) return count(n)")
    Long getCount();
    @Query(value = "MATCH (n:BidWinningAnnouncement) WHERE n.name CONTAINS $fuzzyName return n order by n.releaseTime DESC", countQuery = "MATCH (n:BidWinningAnnouncement) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<BidWinningAnnouncement> findBidWinningAnnouncementByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
