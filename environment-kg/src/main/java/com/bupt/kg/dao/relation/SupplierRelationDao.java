package com.bupt.kg.dao.relation;

import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.StockholderRelation;
import com.bupt.kg.model.relation.SupplierRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierRelationDao extends Neo4jRepository<SupplierRelation, Long> {
    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:SUPPLY{" +
            "startTime: :#{#supplier.startTime}," +
            "endTime: :#{#supplier.endTime}," +
            "ratio: :#{#supplier.ratio}," +
            "amount: :#{#supplier.amount}," +
            "relation: :#{#supplier.relation}" +
            "}]->(e)")
    void addSupplierByNodeId(@Param("startNodeId") Long startNodeId,
                                @Param("endNodeId") Long endNodeId,
                                @Param("supplier") SupplierRelation supplier);

    @Query("match (s)-[r:SUPPLY]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<SupplierRelation> getSupplierRelationByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                    @Param("endNodeId") Long endNodeId);

}
