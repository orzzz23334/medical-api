package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.HospitalDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.utils.RelationUtils;
import com.bupt.kg.model.entity.Hospital;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.service.entity.HospitalService;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalDao hospitalDao;

    @Override
    public Long getCount() {
        return hospitalDao.getCount();
    }

    @Override
    public Hospital getHospitalInfoById(Long id){
        List<Hospital> hospitalList = Option.of(hospitalDao.findHospitalById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return hospitalList.get(0);
    };

    @Override
    public PageData<Hospital> getHospitalPageByFuzzySearch(Hospital hospital, PageRequest pageRequest) {
        return new PageData<>(hospitalDao.findHospitalByFuzzy(hospital.getName(), pageRequest));
    }

    @Override
    public void add(Hospital hospital) {
        try {
            if (hospital.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            hospitalDao.save(hospital);
        } catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public GraphData getHospitalGraphById(Long id, Integer nodeLimit) {
        Hospital hospital = this.getHospitalWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        // 加入主节点
        nodes.add(hospital.buildNodeDto());

        // 采购的中标实例
        Optional.ofNullable(hospital.getPurchaseBid()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getHospitalRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Hospital hospital = this.getHospitalWithAllRelationshipById(id);

        // 采购的中标实例
        RelationTableData<Bid> list = new RelationTableData(Relationship.OUTGOING, RelationConstant.PURCHASE, EntityConstant.BID);
        Optional.ofNullable(hospital.getPurchaseBid()).ifPresent(relations -> {
            relations.forEach(relation -> {
                list.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(list);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public Hospital getHospitalWithAllRelationshipById(Long id) {
        Hospital hospital = Option.of(hospitalDao.findHospitalById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        return hospital;
    }
}
