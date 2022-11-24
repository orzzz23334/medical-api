package com.bupt.kg.dao.relation;

import com.bupt.kg.model.entity.*;
import com.bupt.kg.model.relation.IncludeRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncludeRelationDao extends Neo4jRepository<IncludeRelation, Long> {
    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:INCLUDE]->(e)")
    void addIncludeByNodeId(@Param("startNodeId") Long startNodeId,
                            @Param("endNodeId") Long endNodeId,
                            @Param("include") IncludeRelation include);

    @Query("match (s)-[r:INCLUDE]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<IncludeRelation> getIncludeRelationByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                            @Param("endNodeId") Long endNodeId);

    // for Position
    @Query("match (o:Government)<-[r:INCLUDE]-(p:Position) where id(p) = $positionId return p,r,o")
    List<IncludeRelation<Position, Government>> findIncludeGovernmentByPosition(@Param("positionId") Position position);
    @Query("match (o:Company)<-[r:INCLUDE]-(p:Position) where id(p) = $positionId return p,r,o")
    List<IncludeRelation<Position, Company>> findIncludeCompanyByPosition(@Param("positionId") Position position);
    @Query("match (o:School)<-[r:INCLUDE]-(p:Position) where id(p) = $positionId return p,r,o")
    List<IncludeRelation<Position, School>> findIncludeSchoolByPosition(@Param("positionId") Position position);
    @Query("match (o:Position)<-[r:INCLUDE]-(p:Position) where id(p) = $positionId return p,r,o")
    List<IncludeRelation<Position, Position>> findBeIncludedPositionByPosition(@Param("positionId") Position position);

    // for company
    @Query("match (c:Company)<-[r:INCLUDE]-(o:Company) where id(c) = $childCompanyId return c,r,o")
    List<IncludeRelation<Company, Company>> findparentCompanysBychildCompany(@Param("childCompanyId") Company company);
    @Query("match (c:Company)<-[r:INCLUDE]-(o:Position) where id(c) = $companyId return c,r,o")
    List<IncludeRelation<Position, Company>> findIncludePositionByCompany(@Param("companyId") Company company);

    // for School
    @Query("match (s:School)<-[r:INCLUDE]-(o:Position) where id(s) = $schoolId return s,r,o")
    List<IncludeRelation<Position, School>> findIncludePositionBySchool(@Param("schoolId") School school);
    @Query("match (s:School)<-[r:INCLUDE]-(o:School) where id(s) = $schoolId return s,r,o")
    List<IncludeRelation<School, School>> findIncludeSchoolBySchool(@Param("schoolId") School school);

    // for  Bid
    @Query("match (b:Bid) -[r:INCLUDE]-> (o:BiddingAnnouncement) where id(b) = $bidId return b, r, o")
    List<IncludeRelation<Bid, BiddingAnnouncement>> findBiddingAnnouncementsByBid(@Param("bidId") Bid bid);
    @Query("match (b:Bid) -[r:INCLUDE]-> (o:BidWinningAnnouncement) where id(b) = $bidId return b, r, o")
    List<IncludeRelation<Bid, BidWinningAnnouncement>> findBidWinningAnnouncementsByBid(@Param("bidId") Bid bid);

    // for government
    @Query("match (o:Government)-[r:INCLUDE]->(g:Government) where id(g)=$governmentId return g, r, o")
    List<IncludeRelation<Government, Government>> findIncludeGovernmentByGovernment(@Param("governmentId") Government government);
    @Query("match (o:Position)-[r:INCLUDE]->(g:Government) where id(g)=$governmentId return g, r, o")
    List<IncludeRelation<Position, Government>> findIncludePositionByGovernment(@Param("governmentId") Government government);
}
