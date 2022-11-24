package com.bupt.kg.dao.relation;

import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.Government;
import com.bupt.kg.model.entity.Person;
import com.bupt.kg.model.relation.StockholderRelation;
import com.bupt.kg.model.relation.SupplierRelation;
import com.bupt.kg.model.relation.TakeOfficeRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TakeOfficeRelationDao extends Neo4jRepository<TakeOfficeRelation, Long> {

    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:TAKE_OFFICE{" +
            "startTime: :#{#takeOffice.startTime}," +
            "endTime: :#{#takeOffice.endTime}," +
            "name: :#{#takeOffice.name}," +
            "payment: :#{#takeOffice.payment}" +
            "}]->(e)")
    void addTakeOfficeByNodeId(@Param("startNodeId") Long startNodeId,
                             @Param("endNodeId") Long endNodeId,
                             @Param("takeOffice") TakeOfficeRelation takeOffice);

    @Query("match (s)-[r:TAKE_OFFICE]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<TakeOfficeRelation> getTakeOfficeRelationByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                              @Param("endNodeId") Long endNodeId);


    @Query("match (o:Government)<-[r:TAKE_OFFICE]-(p:Person) where id(p) = $personId return p,r,o")
    List<TakeOfficeRelation<Person, Government>> findTakeOfficeInGovernmentByPerson(@Param("personId") Person person);
    @Query("match (o:Company)<-[r:TAKE_OFFICE]-(p:Person) where id(p) = $personId return p,r,o")
    List<TakeOfficeRelation<Person, Company>> findTakeOfficeInCompanyByPerson(@Param("personId") Person person);
}
