package com.bupt.kg.dao.relation;

import com.bupt.kg.model.relation.Construct;
import com.bupt.kg.model.relation.Evaluate;
import com.bupt.kg.model.relation.StockholderRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluateDao extends Neo4jRepository<Evaluate, Long> {
    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:EVALUATE{" +
            "contact: :#{#evaluate.contact}" +
            "}]->(e)")
    void addEvaluateByNodeId(@Param("startNodeId") Long startNodeId,
                              @Param("endNodeId") Long endNodeId,
                              @Param("evaluate") Evaluate evaluate);

    @Query("match (s)-[r:EVALUATE]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<Evaluate> getEvaluateByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                              @Param("endNodeId") Long endNodeId);

}
