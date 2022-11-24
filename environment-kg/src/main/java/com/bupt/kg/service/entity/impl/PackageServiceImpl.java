package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.PackageDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.utils.RelationUtils;
import com.bupt.kg.model.entity.Package;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.service.entity.PackageService;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PackageServiceImpl implements PackageService {
    @Autowired
    private PackageDao packageDao;

    @Override
    public Long getCount() {
        return packageDao.getCount();
    }

    @Override
    public Package getPackageInfoById(Long id){
        List<Package> packageList = Option.of(packageDao.findPackageById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return packageList.get(0);
    };

    @Override
    public PageData<Package> getPackagePageByFuzzySearch(Package pkg, PageRequest pageRequest) {
        return new PageData<>(packageDao.findPackageByFuzzy(pkg.getName(), pageRequest));
    }

    @Override
    public void add(Package pkg) {
        try {
            if (pkg.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            packageDao.save(pkg);
        } catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public GraphData getPackageGraphById(Long id, Integer nodeLimit) {
        Package pkg = this.getPackageWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        // 加入主节点
        nodes.add(pkg.buildNodeDto());

        // 包含的中标实例
        Optional.ofNullable(pkg.getBeIncludedBid()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        // 包含于的中标公告
        Optional.ofNullable(pkg.getIncludedBidWinningAnnouncement()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getPackageRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Package pkg = this.getPackageWithAllRelationshipById(id);

        // 包含的中标实例
        RelationTableData<Bid> beIncludedBidList = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.BID);
        Optional.ofNullable(pkg.getBeIncludedBid()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beIncludedBidList.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(beIncludedBidList);

        // 包含于的中标公告
        RelationTableData<BidWinningAnnouncement> includedBidWinningAnnouncementList = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.BID_WINNING_ANNOUNCEMENT);
        Optional.ofNullable(pkg.getIncludedBidWinningAnnouncement()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includedBidWinningAnnouncementList.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includedBidWinningAnnouncementList);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public Package getPackageWithAllRelationshipById(Long id) {
        Package pkg = Option.of(packageDao.findPackageById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        return pkg;
    }
}
