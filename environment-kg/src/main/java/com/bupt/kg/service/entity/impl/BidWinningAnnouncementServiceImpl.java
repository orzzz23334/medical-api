package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.BidWinningAnnouncementDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.*;
import com.bupt.kg.model.entity.Package;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.utils.RelationUtils;
import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.service.entity.BidWinningAnnouncementService;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BidWinningAnnouncementServiceImpl implements BidWinningAnnouncementService {
    @Autowired
    private BidWinningAnnouncementDao bidWinningAnnouncementDao;

    @Override
    public Long getCount() {
        return bidWinningAnnouncementDao.getCount();
    }

    @Override
    public BidWinningAnnouncement getBidWinningAnnouncementInfoById(Long id){
        List<BidWinningAnnouncement> bidWinningAnnouncementList = Option.of(bidWinningAnnouncementDao.findBidWinningAnnouncementById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return bidWinningAnnouncementList.get(0);
    };

    @Override
    public PageData<BidWinningAnnouncement> getBidWinningAnnouncementPageByFuzzySearch(BidWinningAnnouncement bidWinningAnnouncement, PageRequest pageRequest) {
        return new PageData<>(bidWinningAnnouncementDao.findBidWinningAnnouncementPageByFuzzy(bidWinningAnnouncement.getName(), pageRequest));
    }

    @Override
    public void add(BidWinningAnnouncement bidWinningAnnouncement) {
        try {
            if (bidWinningAnnouncement.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            bidWinningAnnouncementDao.save(bidWinningAnnouncement);
        } catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public GraphData getBidWinningAnnouncementGraphById(Long id, Integer nodeLimit) {
        BidWinningAnnouncement bidWinningAnnouncement = this.getBidWinningAnnouncementWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        // 加入主节点
        nodes.add(bidWinningAnnouncement.buildNodeDto());

        // 包含的标段包组
        Optional.ofNullable(bidWinningAnnouncement.getBeIncludedPackage()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        // 包含的中标公告
        Optional.ofNullable(bidWinningAnnouncement.getIncludedProject()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getBidWinningAnnouncementRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        BidWinningAnnouncement bidWinningAnnouncement = this.getBidWinningAnnouncementWithAllRelationshipById(id);

        // 包含的标段包组
        RelationTableData<Package> beIncludedPackageList = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.PACKAGE);
        Optional.ofNullable(bidWinningAnnouncement.getBeIncludedPackage()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beIncludedPackageList.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(beIncludedPackageList);

        // 包含的中标公告
        RelationTableData<Project> includedProjectList = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.PROJECT);
        Optional.ofNullable(bidWinningAnnouncement.getIncludedProject()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includedProjectList.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includedProjectList);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public BidWinningAnnouncement getBidWinningAnnouncementWithAllRelationshipById(Long id) {
        BidWinningAnnouncement bidWinningAnnouncement = Option.of(bidWinningAnnouncementDao.findBidWinningAnnouncementById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        return bidWinningAnnouncement;
    }
}
