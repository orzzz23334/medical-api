package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BiddingAnnouncementService {
    GraphData getBiddingAnnouncementGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getBiddingAnnouncementRelationTablesById(Long id);
    BiddingAnnouncement getBiddingAnnouncementInfoById(Long id);
    PageData<BiddingAnnouncement> getBiddingAnnouncementPageByFuzzySearch(BiddingAnnouncement biddingAnnouncement, PageRequest pageRequest);
    Long getCount();

    void add(BiddingAnnouncement biddingAnnouncement);

    BiddingAnnouncement getBiddingAnnouncementWithAllRelationshipById(Long id);
}
