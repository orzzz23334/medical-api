package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.EnvAssessmentAnnouncementDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.EnvAssessmentAnnouncement;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.service.entity.EnvAssessmentAnnouncementService;
import com.bupt.kg.utils.RelationUtils;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnvAssessmentAnnouncementServiceImpl implements EnvAssessmentAnnouncementService {

    @Autowired
    private EnvAssessmentAnnouncementDao envAssessmentAnnouncementDao;
    @Override
    public GraphData getEnvAssessmentAnnouncementGraphById(Long id, Integer nodeLimit) {
        EnvAssessmentAnnouncement envAssessmentAnnouncement = this.getEnvAssessmentAnnouncementWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();


        nodes.add(envAssessmentAnnouncement.buildNodeDto());
        // load construct relatio
        Optional.ofNullable(envAssessmentAnnouncement.getConstructs()).ifPresent(constructs -> {
            constructs = RelationUtils.getRelationLimited(constructs, nodeLimit);
            constructs.forEach(
                    construct -> {
                        nodes.add(construct.getStartNode().buildNodeDto());
                        relations.add(construct.buildRelationDto());
                    });
        });

        Optional.ofNullable((envAssessmentAnnouncement.getEvaluates())).ifPresent(evaluates -> {
            evaluates = RelationUtils.getRelationLimited(evaluates, nodeLimit);
            evaluates.forEach(
                    evaluate -> {
                        nodes.add(evaluate.getStartNode().buildNodeDto());
                        relations.add(evaluate.buildRelationDto());
                    });
        });

        return new GraphData(nodes, relations);

    }

    @Override
    public List<RelationTableData> getEnvAssessmentAnnouncementRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        EnvAssessmentAnnouncement envAssessmentAnnouncement = this.getEnvAssessmentAnnouncementWithAllRelationshipById(id);

        RelationTableData<Company> evaluates = new RelationTableData(Relationship.INCOMING, RelationConstant.EVALUATE, EntityConstant.COMPANY);
        Optional.ofNullable(envAssessmentAnnouncement.getEvaluates()).ifPresent(relations -> {
            relations.forEach(relation -> {
                evaluates.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(evaluates);

        RelationTableData<Company> constructs = new RelationTableData(Relationship.INCOMING, RelationConstant.CONSTRUCT, EntityConstant.COMPANY);
        Optional.ofNullable(envAssessmentAnnouncement.getConstructs()).ifPresent(relations -> {
            relations.forEach(relation -> {
                constructs.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(constructs);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());

        return relationTableDatas;
    }

    @Override
    public EnvAssessmentAnnouncement getEnvAssessmentAnnouncementInfoById(Long id) {
        List<EnvAssessmentAnnouncement> envAssessmentAnnouncements = Option.of(envAssessmentAnnouncementDao.findEnvAssessmentAnnouncementById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return envAssessmentAnnouncements.get(0);
    }

    @Override
    public PageData<EnvAssessmentAnnouncement> getEnvAssessmentAnnouncementPage(PageRequest pageRequest) {
        return new PageData<>(envAssessmentAnnouncementDao.findAll(pageRequest));
    }

    @Override
    public PageData<EnvAssessmentAnnouncement> getEnvAssessmentAnnouncementPageByFuzzySearch(EnvAssessmentAnnouncement envAssessmentAnnouncement, PageRequest pageRequest) {
        return new PageData<>(envAssessmentAnnouncementDao.findEnvAssessmentAnnouncementsByFuzzy(envAssessmentAnnouncement.getName(), pageRequest));
    }

    @Override
    public Long getCount() {

        return envAssessmentAnnouncementDao.getCount();
    }

    @Override
    public void add(EnvAssessmentAnnouncement envAssessmentAnnouncement) {
        try{
            if (envAssessmentAnnouncement.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            envAssessmentAnnouncementDao.save(envAssessmentAnnouncement);
        }catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public void update(EnvAssessmentAnnouncement envAssessmentAnnouncement) {
        try{
            if (envAssessmentAnnouncement.getId() == null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            System.out.println(envAssessmentAnnouncement);
            envAssessmentAnnouncementDao.save(envAssessmentAnnouncement);
        }catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Long id) {
        try{
            envAssessmentAnnouncementDao.deleteById(id);
        }catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_DELETE_FAILURE);
        }
    }

    @Override
    public EnvAssessmentAnnouncement getEnvAssessmentAnnouncementWithAllRelationshipById(Long id) {
        EnvAssessmentAnnouncement envAssessmentAnnouncement = Option.of(envAssessmentAnnouncementDao.findEnvAssessmentAnnouncementById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        return envAssessmentAnnouncement;
    }
}
