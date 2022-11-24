package com.bupt.kg.dao.relation;

import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.EnvAssessmentAnnouncement;
import com.bupt.kg.model.relation.Construct;
import com.bupt.kg.model.relation.StockholderRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConstructDao extends Neo4jRepository<Construct, Long> {

    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:CONSTRUCT{" +
            "contact: :#{#construct.contact}" +
            "}]->(e)")
    void addConstructByNodeId(@Param("startNodeId") Long startNodeId,
                          @Param("endNodeId") Long endNodeId,
                          @Param("construct") Construct construct);

    @Query("match (s)-[r:CONSTRUCT]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<Construct> getConstructByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                              @Param("endNodeId") Long endNodeId);


    // for company
    @Query("match (o:BiddingAnnouncement)<-[r:CONSTRUCT]-(c:Company) where id(c) = $companyId return c, r, o")
    List<Construct<Company, BiddingAnnouncement>> findConstructsForBiddingAnnouncementByCompany(@Param("companyId") Company company);
    @Query("match (o:EnvAssessmentAnnouncement)<-[r:CONSTRUCT]-(c:Company) where id(c) = $companyId return c, r, o")
    List<Construct<Company, EnvAssessmentAnnouncement>> findConstructsByCompany(@Param("companyId") Company company);
    @Query("match (o:BidWinningAnnouncement)<-[r:CONSTRUCT]-(c:Company) where id(c) = $companyId return c, r, o")
    List<Construct<Company, BidWinningAnnouncement>> findconstructsForBidWinningAnnouncementByCompany(@Param("companyId") Company company);

}
