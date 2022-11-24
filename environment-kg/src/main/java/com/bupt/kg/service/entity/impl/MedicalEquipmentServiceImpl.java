package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.MedicalEquipmentDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.utils.RelationUtils;
import com.bupt.kg.model.entity.MedicalEquipment;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.service.entity.MedicalEquipmentService;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalEquipmentServiceImpl implements MedicalEquipmentService {
    @Autowired
    private MedicalEquipmentDao medicalEquipmentDao;

    @Override
    public Long getCount() {
        return medicalEquipmentDao.getCount();
    }

    @Override
    public MedicalEquipment getMedicalEquipmentInfoById(Long id){
        List<MedicalEquipment> medicalEquipmentList = Option.of(medicalEquipmentDao.findMedicalEquipmentById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return medicalEquipmentList.get(0);
    };

    @Override
    public PageData<MedicalEquipment> getMedicalEquipmentPageByFuzzySearch(MedicalEquipment medicalEquipment, PageRequest pageRequest) {
        return new PageData<>(medicalEquipmentDao.findMedicalEquipmentByFuzzy(medicalEquipment.getName(), pageRequest));
    }

    @Override
    public void add(MedicalEquipment medicalEquipment) {
        try {
            if (medicalEquipment.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            medicalEquipmentDao.save(medicalEquipment);
        } catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public GraphData getMedicalEquipmentGraphById(Long id, Integer nodeLimit) {
        MedicalEquipment medicalEquipment = this.getMedicalEquipmentWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        // 加入主节点
        nodes.add(medicalEquipment.buildNodeDto());

        // 包含于的中标实例
        Optional.ofNullable(medicalEquipment.getIncludedBid()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getMedicalEquipmentRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        MedicalEquipment medicalEquipment = this.getMedicalEquipmentWithAllRelationshipById(id);

        // 包含于的中标实例
        RelationTableData<Bid> includedBidList = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.BID);
        Optional.ofNullable(medicalEquipment.getIncludedBid()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includedBidList.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includedBidList);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public MedicalEquipment getMedicalEquipmentWithAllRelationshipById(Long id) {
        MedicalEquipment medicalEquipment = Option.of(medicalEquipmentDao.findMedicalEquipmentById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        return medicalEquipment;
    }
}
