package com.bupt.kg.service.relation;

import com.bupt.kg.model.relation.EvaluateBid;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.relation.WinBid;

public interface WinBidService {
    void deleteById(Long id);
    void update(WinBid winBid);
    void add(Long startNodeId, Long endNodeId, WinBid winBid);
    void moveTo(Long startNodeId, Long endNodeId, WinBid winBid);
}
