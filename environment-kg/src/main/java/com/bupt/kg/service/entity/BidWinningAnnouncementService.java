package com.bupt.kg.service.entity;

import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BidWinningAnnouncementService {
    GraphData getBidWinningAnnouncementGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getBidWinningAnnouncementRelationTablesById(Long id);
    BidWinningAnnouncement getBidWinningAnnouncementInfoById(Long id);
    PageData<BidWinningAnnouncement> getBidWinningAnnouncementPageByFuzzySearch(BidWinningAnnouncement bidWinningAnnouncement, PageRequest pageRequest);
    Long getCount();

    void add(BidWinningAnnouncement bidWinningAnnouncement);

    BidWinningAnnouncement getBidWinningAnnouncementWithAllRelationshipById(Long id);
}
