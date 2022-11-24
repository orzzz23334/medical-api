package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.IncludeRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.Government;
import com.bupt.kg.model.entity.School;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.dao.entity.PositionDao;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.Position;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.service.entity.PositionService;
import com.bupt.kg.utils.RelationUtils;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionDao positionDao;
    @Autowired
    private IncludeRelationDao includeRelationDao;
    @Override
    public GraphData getPositionGraphById(Long id, Integer nodeLimit) {
        Position position = this.getPositionWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        nodes.add(position.buildNodeDto());

        // 地区下包含的地区
        Optional.ofNullable(position.getBeIncludedPosition()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是endNode
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 包含这个地区的地区
        Optional.ofNullable(position.getIncludePosition()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加这个地区包含的公司
        Optional.ofNullable(position.getIncludeCompany()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode ??
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加这个地区包含的政府
        Optional.ofNullable(position.getIncludeGovernment()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode ??
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加这个地区包含的学校
        Optional.ofNullable(position.getIncludeSchool()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是endNode
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getPositionRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Position position = this.getPositionWithAllRelationshipById(id);

        // 地区下包含的政府
        RelationTableData<Government> includeGovernment = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.GOVERNMENT);
        Optional.ofNullable(position.getIncludeGovernment()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includeGovernment.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(includeGovernment);

        // 地区下包含的公司
        RelationTableData<Company> includeCompany = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.COMPANY);
        Optional.ofNullable(position.getIncludeCompany()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includeCompany.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(includeCompany);

        // 地区下包含的学校
        RelationTableData<School> includeSchool = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.SCHOOL);
        Optional.ofNullable(position.getIncludeSchool()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includeSchool.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(includeSchool);

        // 地区下包含的地区
        RelationTableData<Position> beIncludedPosition = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.POSITION);
        Optional.ofNullable(position.getBeIncludedPosition()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beIncludedPosition.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(beIncludedPosition);

        // 包含这个地区的地区
        RelationTableData<Position> includePosition = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.POSITION);
        Optional.ofNullable(position.getIncludePosition()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includePosition.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includePosition);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public Position getPositionInfoById(Long id) {
        List<Position> positionList = Option.of(positionDao.findPositionById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return positionList.get(0);
    }

    @Override
    public PageData<Position> getPositionPage(PageRequest pageRequest) {
        return new PageData<>(positionDao.findAll(pageRequest));
    }

    @Override
    public PageData<Position> getPositionPageByFuzzySearch(Position position, PageRequest pageRequest) {
        return new PageData<>(positionDao.findPositionsByFuzzy(position.getName(), pageRequest));
    }

    @Override
    public Long getCount() {
        return positionDao.getCount();
    }

    @Override
    public void add(Position position) {
        try {
            if (position.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            positionDao.save(position);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public void update(Position position) {
        try {
            if (position.getId() == null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            positionDao.save(position);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            positionDao.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_DELETE_FAILURE);
        }
    }

    @Override
    public Position getPositionWithAllRelationshipById(Long id) {
        Position position = Option.of(positionDao.findPositionById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);

        position.setIncludeGovernment(includeRelationDao.findIncludeGovernmentByPosition(position));
        position.setIncludeCompany(includeRelationDao.findIncludeCompanyByPosition(position));
        position.setIncludeSchool(includeRelationDao.findIncludeSchoolByPosition(position));
        position.setBeIncludedPosition(includeRelationDao.findBeIncludedPositionByPosition(position));

        return position;
    }
}
