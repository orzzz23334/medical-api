package com.bupt.kg.dao.relation;

import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.StockholderRelation;
import com.bupt.kg.model.relation.WinBid;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WinBidDao extends Neo4jRepository<WinBid, Long> {
    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:WINBID{" +
            "contact: :#{#winBid.contact}" +
            "bidAmount: :#{#winBid.bidAmount}" +
            "}]->(e)")
    void addAgentByNodeId(@Param("startNodeId") Long startNodeId,
                          @Param("endNodeId") Long endNodeId,
                          @Param("winBid") WinBid winBid);

    @Query("match (s)-[r:WINBID]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<WinBid> getWinBidByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                              @Param("endNodeId") Long endNodeId);

}
