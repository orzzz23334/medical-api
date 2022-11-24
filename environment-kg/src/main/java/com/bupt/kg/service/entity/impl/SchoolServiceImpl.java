package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.IncludeRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.entity.Person;
import com.bupt.kg.model.entity.Position;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.dao.entity.SchoolDao;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.School;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.service.entity.SchoolService;
import com.bupt.kg.utils.RelationUtils;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    private SchoolDao schoolDao;
    @Autowired
    private IncludeRelationDao includeRelationDao;
    @Override
    public GraphData getSchoolGraphById(Long id, Integer nodeLimit) {
        School school = this.getSchoolWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        nodes.add(school.buildNodeDto());

        // 添加当前学校包含的学校
        Optional.ofNullable(school.getBeIncludedSchool()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是endNode
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加包含这个学校的学校
        Optional.ofNullable(school.getIncludedSchool()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加这个学校属于的地区
        Optional.ofNullable(school.getIncludePosition()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加到这个学校学习的人
        Optional.ofNullable(school.getAttendPerson()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getSchoolRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        School school = this.getSchoolWithAllRelationshipById(id);

        // 地区下包含的学校
        RelationTableData<Position> includePosition = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.POSITION);
        Optional.ofNullable(school.getIncludePosition()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includePosition.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includePosition);

        // 当前学校包含的学校
        RelationTableData<School> beIncludedSchool = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.SCHOOL);
        Optional.ofNullable(school.getBeIncludedSchool()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beIncludedSchool.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(beIncludedSchool);

        // 当前学校被包含的学校
        RelationTableData<School> includedSchool = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.SCHOOL);
        Optional.ofNullable(school.getIncludedSchool()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includedSchool.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includedSchool);

        // 就读这个学校的人
        RelationTableData<Person> attendPerson = new RelationTableData(Relationship.INCOMING, RelationConstant.ATTEND, EntityConstant.PERSON);
        Optional.ofNullable(school.getAttendPerson()).ifPresent(relations -> {
            relations.forEach(relation -> {
                attendPerson.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(attendPerson);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());

        return relationTableDatas;
    }

    @Override
    public School getSchoolInfoById(Long id) {
        List<School> schoolList = Option.of(schoolDao.findSchoolById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return schoolList.get(0);
    }

    @Override
    public PageData<School> getSchoolPage(PageRequest pageRequest) {
        return new PageData<>(schoolDao.findAll(pageRequest));
    }

    @Override
    public PageData<School> getSchoolPageByFuzzySearch(School school, PageRequest pageRequest) {
        return new PageData<>(schoolDao.findSchoolsByFuzzy(school.getName(), pageRequest));
    }

    @Override
    public Long getCount() {
        return schoolDao.getCount();
    }

    @Override
    public void add(School school) {
        try {
            if (school.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            schoolDao.save(school);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public void update(School school) {
        try {
            if (school.getId() == null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            schoolDao.save(school);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            schoolDao.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_DELETE_FAILURE);
        }
    }

    @Override
    public School getSchoolWithAllRelationshipById(Long id) {
        School school = Option.of(schoolDao.findSchoolById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        school.setIncludePosition(includeRelationDao.findIncludePositionBySchool(school));
        school.setIncludedSchool(includeRelationDao.findIncludeSchoolBySchool(school));

        return school;
    }
}
