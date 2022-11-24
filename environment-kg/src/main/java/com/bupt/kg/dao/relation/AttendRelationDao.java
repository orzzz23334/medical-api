package com.bupt.kg.dao.relation;

import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.StockholderRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendRelationDao extends Neo4jRepository<AttendRelation, Long> {
    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:ATTEND{" +
            "startTime: :#{#attend.startTime}," +
            "endTime: :#{#attend.endTime}," +
            "degree: :#{#attend.degree}," +
            "major: :#{#attend.major}" +
            "}]->(e)")
    void addAttendByNodeId(@Param("startNodeId") Long startNodeId,
                           @Param("endNodeId") Long endNodeId,
                           @Param("attend") AttendRelation attend);

    @Query("match (s)-[r:ATTEND]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<AttendRelation> getAttendRelationByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                              @Param("endNodeId") Long endNodeId);

}
