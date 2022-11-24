package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.EnvAssessmentAnnouncement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnvAssessmentAnnouncementDao extends Neo4jRepository<EnvAssessmentAnnouncement, Long> {
    List<EnvAssessmentAnnouncement> findEnvAssessmentAnnouncementById(Long id);
    Page<EnvAssessmentAnnouncement> findAll(Pageable pageable);

    @Query("match (n:EnvAssessmentAnnouncement) return count(n)")
    Long getCount();

    // 当前仅根据名字可以根据需要进行拓展
    @Query(value = "MATCH (n:EnvAssessmentAnnouncement) WHERE n.name CONTAINS $fuzzyName return n",countQuery = "MATCH (n:EnvAssessmentAnnouncement) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<EnvAssessmentAnnouncement> findEnvAssessmentAnnouncementsByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
