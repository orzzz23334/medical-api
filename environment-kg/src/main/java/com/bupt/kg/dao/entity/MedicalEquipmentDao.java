package com.bupt.kg.dao.entity;

import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.MedicalEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalEquipmentDao extends Neo4jRepository<MedicalEquipment, Long> {
    List<MedicalEquipment> findMedicalEquipmentById(Long id);
    @Override
    Page<MedicalEquipment> findAll(Pageable pageable);

    @Query("match (n:MedicalEquipment) return count(n)")
    Long getCount();
    @Query(value = "MATCH (n:MedicalEquipment) WHERE n.name CONTAINS $fuzzyName return n", countQuery = "MATCH (n:MedicalEquipment) WHERE n.name CONTAINS $fuzzyName return count(n)")
    Page<MedicalEquipment> findMedicalEquipmentByFuzzy(@Param("fuzzyName") String fuzzyName, Pageable pageable);
}
