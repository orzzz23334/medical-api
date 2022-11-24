package com.bupt.kg.dao.relation;

import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.Construct;
import com.bupt.kg.model.relation.StockholderRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentDao extends Neo4jRepository<Agent, Long> {

    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:AGENT{" +
                                "contact: :#{#agent.contact}" +
                            "}]->(e)")
    void addAgentByNodeId(@Param("startNodeId") Long startNodeId,
                          @Param("endNodeId") Long endNodeId,
                          @Param("agent") Agent agent);

    @Query("match (s)-[r:AGENT]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<Agent> getAgentByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                              @Param("endNodeId") Long endNodeId);


    // for Company
    @Query("match (o:BiddingAnnouncement)<-[r:AGENT]-(c:Company) where id(c) = $companyId return c, r, o")
    List<Agent<Company, BiddingAnnouncement>> findAgentsForBiddingAnnouncementByCompany(@Param("companyId") Company company);
    @Query("match (o:BidWinningAnnouncement)<-[r:AGENT]-(c:Company) where id(c) = $companyId return c, r, o")
    List<Agent<Company, BidWinningAnnouncement>> findAgentsForBidWinningAnnouncementByCompany(@Param("companyId") Company company);
}
