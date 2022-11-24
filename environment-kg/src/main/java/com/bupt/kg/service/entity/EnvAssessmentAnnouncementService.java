package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.EnvAssessmentAnnouncement;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface EnvAssessmentAnnouncementService {
    GraphData getEnvAssessmentAnnouncementGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getEnvAssessmentAnnouncementRelationTablesById(Long id);

    EnvAssessmentAnnouncement getEnvAssessmentAnnouncementInfoById(Long id);
    PageData<EnvAssessmentAnnouncement> getEnvAssessmentAnnouncementPage(PageRequest pageRequest);
    PageData<EnvAssessmentAnnouncement> getEnvAssessmentAnnouncementPageByFuzzySearch(EnvAssessmentAnnouncement envAssessmentAnnouncement, PageRequest pageRequest);
    Long getCount();

    void add(EnvAssessmentAnnouncement envAssessmentAnnouncement);
    void update(EnvAssessmentAnnouncement envAssessmentAnnouncement);
    void deleteById(Long id);

    EnvAssessmentAnnouncement getEnvAssessmentAnnouncementWithAllRelationshipById(Long id);
}
