package com.bupt.kg.dao.relation;

import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.IsFriendOf;
import com.bupt.kg.model.relation.SupplierRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IsFriendOfDao extends Neo4jRepository<IsFriendOf, Long> {
    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "create (s)-[r:IS_FRIEND_OF{" +
            "relationType: :#{#isFriendOf.relationType}," +
            "degree: :#{#isFriendOf.degree}," +
            "describe: :#{#isFriendOf.describe}," +
            "startTime: :#{#isFriendOf.startTime}," +
            "InfoChannel: :#{#isFriendOf.InfoChannel}," +
            "dataSource: :#{#isFriendOf.dataSource}" +
            "}]->(e)")
    void addIsFriendOfByNodeId(@Param("startNodeId") Long startNodeId,
                               @Param("endNodeId") Long endNodeId,
                               @Param("isFriendOf") IsFriendOf isFriendOf);

    @Query("match (s)-[r:IS_FRIEND_OF]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<IsFriendOf> getIsFriendOfByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                    @Param("endNodeId") Long endNodeId);

}
