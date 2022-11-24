package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.ProjectDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.utils.RelationUtils;
import com.bupt.kg.model.entity.Project;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.service.entity.ProjectService;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDao projectDao;

    @Override
    public Long getCount() {
        return projectDao.getCount();
    }

    @Override
    public Project getProjectInfoById(Long id){
        List<Project> projectList = Option.of(projectDao.findProjectById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return projectList.get(0);
    };

    @Override
    public PageData<Project> getProjectPageByFuzzySearch(Project project, PageRequest pageRequest) {
        return new PageData<>(projectDao.findProjectByFuzzy(project.getName(), pageRequest));
    }

    @Override
    public void add(Project project) {
        try {
            if (project.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            projectDao.save(project);
        } catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public GraphData getProjectGraphById(Long id, Integer nodeLimit) {
        Project project = this.getProjectWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        // 加入主节点
        nodes.add(project.buildNodeDto());

        // 包含的中标公告
        Optional.ofNullable(project.getBeIncludedBidWinningAnnouncement()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        // 包含于的招标公告
        Optional.ofNullable(project.getIncludedBiddingAnnouncement()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getProjectRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Project project = this.getProjectWithAllRelationshipById(id);

        // 包含的中标公告
        RelationTableData<BidWinningAnnouncement> beIncludedBidWinningAnnouncementList = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.BID_WINNING_ANNOUNCEMENT);
        Optional.ofNullable(project.getBeIncludedBidWinningAnnouncement()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beIncludedBidWinningAnnouncementList.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(beIncludedBidWinningAnnouncementList);

        // 包含于的招标公告
        RelationTableData<BiddingAnnouncement> includedBiddingAnnouncementList = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.BIDDING_ANNOUNCEMENT);
        Optional.ofNullable(project.getIncludedBiddingAnnouncement()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includedBiddingAnnouncementList.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includedBiddingAnnouncementList);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public Project getProjectWithAllRelationshipById(Long id) {
        Project project = Option.of(projectDao.findProjectById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        return project;
    }
}
