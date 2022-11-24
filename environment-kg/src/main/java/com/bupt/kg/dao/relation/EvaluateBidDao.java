package com.bupt.kg.dao.relation;

import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.EvaluateBid;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluateBidDao extends Neo4jRepository<EvaluateBid, Long> {
    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:EVALUATE_BID]->(e)")
    void addAgentByNodeId(@Param("startNodeId") Long startNodeId,
                          @Param("endNodeId") Long endNodeId,
                          @Param("evaluateBid") EvaluateBid evaluateBid);

    @Query("match (s)-[r:EVALUATE_BID]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<EvaluateBid> getEvaluateBidByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                    @Param("endNodeId") Long endNodeId);

}
