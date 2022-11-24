package com.bupt.kg.dao.relation;

import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.Person;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.model.relation.StockholderRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockholderRelationDao extends Neo4jRepository<StockholderRelation, Long> {

    @Query("match (s),(e) where id(s)=$startNodeId and id(e)=$endNodeId " +
            "merge (s)-[r:STOCKHOLDER{" +
            "startTime: :#{#stockholder.startTime}," +
            "endTime: :#{#stockholder.endTime}," +
            "shareholdingRatio: :#{#stockholder.shareholdingRatio}," +
            "ultimateBeneficialShare: :#{#stockholder.ultimateBeneficialShare}," +
            "numberOfShare: :#{#stockholder.numberOfShare}," +
            "investmentAmount: :#{#stockholder.investmentAmount}," +
            "shareType: :#{#stockholder.shareType}," +
            "holdingRelationship: :#{#stockholder.holdingRelationship}" +
            "}]->(e)")
    void addStockholderByNodeId(@Param("startNodeId") Long startNodeId,
                            @Param("endNodeId") Long endNodeId,
                            @Param("stockholder") StockholderRelation stockholder);

    @Query("match (s)-[r:STOCKHOLDER]->(e) " +
            "where id(s)=$startNodeId and id(e)=$endNodeId " +
            "return s,r,e")
    List<StockholderRelation> getStockholderRelationByStartNodeIdAndEndNodeId(@Param("startNodeId") Long startNodeId,
                                                                      @Param("endNodeId") Long endNodeId);

    @Query("match (c:Company)<-[r:STOCKHOLDER]-(o:Person) where id(c) = $companyId return c,r,o")
    List<StockholderRelation<Person, Company>> findStockHolderPersonByCompany(@Param("companyId") Company company);
    @Query("match (c:Company)<-[r:STOCKHOLDER]-(o:Company) where id(c) = $companyId return c,r,o")
    List<StockholderRelation<Company, Company>> findStockHolderCompanyByCompany(@Param("companyId") Company company);
}
