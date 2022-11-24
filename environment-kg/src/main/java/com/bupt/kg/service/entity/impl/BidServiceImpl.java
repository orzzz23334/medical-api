package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.BidDao;
import com.bupt.kg.dao.relation.IncludeRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.*;
import com.bupt.kg.model.entity.Package;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.service.entity.BidService;
import com.bupt.kg.service.relation.IncludeRelationService;
import com.bupt.kg.utils.RelationUtils;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidDao bidDao;
    @Autowired
    private IncludeRelationDao includeRelationDao;
    @Autowired
    private IncludeRelationService includeRelationService;

    @Override
    public GraphData getBidGraphById(Long id, Integer nodeLimit) {
        Bid bid = this.getBidWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();


        // 加入主节点
        nodes.add(bid.buildNodeDto());


        // 供应商
        Optional.ofNullable(bid.getSupplier()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 采购医院
        Optional.ofNullable(bid.getPurchaserHospital()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 包含的医疗器械
        Optional.ofNullable(bid.getIncludeMedicalEquipment()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 包含于的标段包组
        Optional.ofNullable(bid.getBeIncludedPackage()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getBidRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Bid bid = this.getBidWithAllRelationshipById(id);

        // 供应商
        RelationTableData<Supplier> supplierList = new RelationTableData(Relationship.INCOMING, RelationConstant.SUPPLY, EntityConstant.SUPPLIER);
        Optional.ofNullable(bid.getSupplier()).ifPresent(relations -> {
            relations.forEach(relation -> {
                supplierList.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(supplierList);


        // 采购医院
        RelationTableData<Hospital> purchaserHospitalList = new RelationTableData(Relationship.INCOMING, RelationConstant.PURCHASE, EntityConstant.HOSPITAL);
        Optional.ofNullable(bid.getPurchaserHospital()).ifPresent(relations -> {
            relations.forEach(relation -> {
                purchaserHospitalList.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(purchaserHospitalList);

        // 包含的医疗器械
        RelationTableData<MedicalEquipment> includeMedicalEquipmentList = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.MEDICAL_EQUIPMENT);
        Optional.ofNullable(bid.getIncludeMedicalEquipment()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includeMedicalEquipmentList.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(includeMedicalEquipmentList);

        // 包含于的标段包组
        RelationTableData<Package> beIncludedPackageList = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.PACKAGE);
        Optional.ofNullable(bid.getBeIncludedPackage()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beIncludedPackageList.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(beIncludedPackageList);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public Bid getBidInfoById(Long id) {
        List<Bid> bids = Option.of(bidDao.findBidById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return bids.get(0);
    }

    @Override
    public PageData<Bid> getBidPageByFuzzySearch(Bid bid, PageRequest pageRequest) {
        return new PageData<>(bidDao.findBidPageByFuzzy(bid.getName(), pageRequest));
    }

    @Override
    public Long getCount() {
        return bidDao.getCount();
    }

    @Override
    public void add(Bid bid) {
        try{
            if (bid.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            bidDao.save(bid);
        }catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public void update(Bid bid) {
        try{
            if (bid.getId() == null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            bidDao.save(bid);
        }catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public void deleteById(Long id) {
        try{
            bidDao.deleteById(id);
        }catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_DELETE_FAILURE);
        }
    }

    /**
     * 将 slaverNode 的 关系全部移动到 masterNode 上去
     * masterNode 的属性为主
     * 融合完成后 删除 slaverNode
     */
    @Override
    @Transactional(rollbackFor = KgException.class)
    public void mergeNode(Long masterNodeId, Long slaverNodeId) {

//        Bid masterNode = this.getBidWithAllRelationshipById(masterNodeId);
//        Bid slaverNode = this.getBidWithAllRelationshipById(slaverNodeId);
//
//        // 融合节点属性
//        if("".equals(masterNode.getName())) {
//            masterNode.setName(slaverNode.getName());
//        }
//        if("".equals(masterNode.getSectionNo())) {
//            masterNode.setSectionNo(slaverNode.getSectionNo());
//        }
//        if("".equals(masterNode.getProjectStage())) {
//            masterNode.setProjectStage(slaverNode.getProjectStage());
//        }
//
//        this.update(masterNode);
//
//
//        // 移动关系
//        // 将 slaverNode 上的关系都移动到 masterNode 上
//        Optional.ofNullable(slaverNode.getBiddingAnnouncements()).ifPresent(includeRelations -> {
//            includeRelations.forEach(includeRelation -> {
//                includeRelationService.moveTo(masterNode.id, includeRelation.getEndNode().id, includeRelation);
//            });
//        });
//        Optional.ofNullable(slaverNode.getBidWinningAnnouncements()).ifPresent(includeRelations -> {
//            includeRelations.forEach(includeRelation -> {
//                includeRelationService.moveTo(masterNode.id, includeRelation.getEndNode().id, includeRelation);
//            });
//        });
//
//        // 删除 slaverNode
//        this.deleteById(slaverNode.id);

    }

    /**
     * 手动解决冲突
     */
    @Override
    public Bid getBidWithAllRelationshipById(Long id) {
        Bid bid = Option.of(bidDao.findBidById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
//        bid.setBiddingAnnouncements(includeRelationDao.findBiddingAnnouncementsByBid(bid));
//        bid.setBidWinningAnnouncements(includeRelationDao.findBidWinningAnnouncementsByBid(bid));
        return bid;
    }
}
