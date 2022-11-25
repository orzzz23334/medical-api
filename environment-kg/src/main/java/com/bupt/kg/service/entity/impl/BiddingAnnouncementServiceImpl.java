package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.BiddingAnnouncementDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.*;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.utils.RelationUtils;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.service.entity.BiddingAnnouncementService;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BiddingAnnouncementServiceImpl implements BiddingAnnouncementService {
    @Autowired
    private BiddingAnnouncementDao biddingAnnouncementDao;

    @Override
    public Long getCount() {
        return biddingAnnouncementDao.getCount();
    }

    @Override
    public BiddingAnnouncement getBiddingAnnouncementInfoById(Long id){
        List<BiddingAnnouncement> biddingAnnouncementList = Option.of(biddingAnnouncementDao.findBiddingAnnouncementById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return biddingAnnouncementList.get(0);
    };

    @Override
    public PageData<BiddingAnnouncement> getBiddingAnnouncementPageByFuzzySearch(BiddingAnnouncement biddingAnnouncement, PageRequest pageRequest) {
        return new PageData<>(biddingAnnouncementDao.findBiddingAnnouncementByFuzzy(biddingAnnouncement.getName(), pageRequest));
    }

    @Override
    public void add(BiddingAnnouncement biddingAnnouncement) {
        try {
            if (biddingAnnouncement.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            biddingAnnouncementDao.save(biddingAnnouncement);
        } catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public GraphData getBiddingAnnouncementGraphById(Long id, Integer nodeLimit) {
        BiddingAnnouncement biddingAnnouncement = this.getBiddingAnnouncementWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        // 加入主节点
        nodes.add(biddingAnnouncement.buildNodeDto());

        // 包含的项目
        Optional.ofNullable(biddingAnnouncement.getBeIncludedProject()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getBiddingAnnouncementRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        BiddingAnnouncement biddingAnnouncement = this.getBiddingAnnouncementWithAllRelationshipById(id);

        // 包含的项目
        RelationTableData<Project> beIncludedProjectList = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.PROJECT);
        Optional.ofNullable(biddingAnnouncement.getBeIncludedProject()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beIncludedProjectList.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(beIncludedProjectList);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public BiddingAnnouncement getBiddingAnnouncementWithAllRelationshipById(Long id) {
        BiddingAnnouncement biddingAnnouncement = Option.of(biddingAnnouncementDao.findBiddingAnnouncementById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        return biddingAnnouncement;
    }
}
