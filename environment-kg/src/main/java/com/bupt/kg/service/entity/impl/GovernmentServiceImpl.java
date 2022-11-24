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
import com.bupt.kg.dao.entity.GovernmentDao;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.Government;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.service.entity.GovernmentService;
import com.bupt.kg.service.relation.IncludeRelationService;
import com.bupt.kg.service.relation.TakeOfficeRelationService;
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
public class GovernmentServiceImpl implements GovernmentService {
    @Autowired
    private GovernmentDao governmentDao;
    @Autowired
    private IncludeRelationDao includeRelationDao;
    @Autowired
    private IncludeRelationService includeRelationService;
    @Autowired
    private TakeOfficeRelationService takeOfficeRelationService;

    @Override
    public GraphData getGovernmentGraphById(Long id, Integer nodeLimit) {
        Government government = this.getGovernmentWithAllRelationshipById(id);
        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        nodes.add(government.buildNodeDto());

        // 添加当前政府机构包含的政府机构
        Optional.ofNullable(government.getBeIncludedGovernment()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是endNode
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加任职到当前公司的人
        Optional.ofNullable(government.getTakeOfficePerson()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加包含当前政府的政府
        Optional.ofNullable(government.getIncludeGovernment()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation->{
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加这个政府所属于的地区
        Optional.ofNullable(government.getIncludePosition()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        return new GraphData(nodes,relations);
    }

    @Override
    public List<RelationTableData> getGovernmentRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Government government = this.getGovernmentWithAllRelationshipById(id);

        // 当前政府上设政府
        RelationTableData<Government> includeGovernment = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.GOVERNMENT);
        Optional.ofNullable(government.getIncludeGovernment()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includeGovernment.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includeGovernment);

        // 当前政府的下设的政府
        RelationTableData<Government> beIncludedGovernment = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.GOVERNMENT);
        Optional.ofNullable(government.getBeIncludedGovernment()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beIncludedGovernment.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(beIncludedGovernment);

        // 任职到这个政府的人
        RelationTableData<Person> takeOfficePerson = new RelationTableData(Relationship.INCOMING, RelationConstant.TAKE_OFFICE, EntityConstant.PERSON);
        Optional.ofNullable(government.getTakeOfficePerson()).ifPresent(relations -> {
            relations.forEach(relation -> {
                takeOfficePerson.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(takeOfficePerson);

        // 这个政府所属的地区
        RelationTableData<Position> includePosition = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.POSITION);
        Optional.ofNullable(government.getIncludePosition()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includePosition.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includePosition);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());

        return relationTableDatas;
    }

    @Override
    public Government getGovernmentInfoById(Long id) {
        List<Government> governmentList = Option.of(governmentDao.findGovernmentById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return governmentList.get(0);
    }

    @Override
    public PageData<Government> getGovernmentPage(PageRequest pageRequest) {
        return new PageData<>(governmentDao.findAll(pageRequest));
    }

    @Override
    public PageData<Government> getGovernmentPageByFuzzySearch(Government government, PageRequest pageRequest) {
        return new PageData<>(governmentDao.findGovernmentsByFuzzy(government.getName(), pageRequest));
    }

    @Override
    public Long getCount() {
        return governmentDao.getCount();
    }

    @Override
    public void add(Government government) {
        try {
            if (government.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            governmentDao.save(government);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public void update(Government government) {
        try {
            if (government.getId() == null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            governmentDao.save(government);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            governmentDao.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_DELETE_FAILURE);
        }
    }

    @Override
    @Transactional(rollbackFor = KgException.class)
    public void mergeNode(Long masterNodeId, Long slaverNodeId) {
        Government masterNode = this.getGovernmentWithAllRelationshipById(masterNodeId);
        Government slaverNode = this.getGovernmentWithAllRelationshipById(slaverNodeId);

        // 融合节点属性
        if("".equals(masterNode.getName())) {
            masterNode.setName(slaverNode.getName());
        }
        if("".equals(masterNode.getAddress())) {
            masterNode.setAddress(slaverNode.getAddress());
        }
        if("".equals(masterNode.getOtherName())) {
            masterNode.setOtherName(slaverNode.getOtherName());
        }
        if("".equals(masterNode.getLevel())) {
            masterNode.setLevel(slaverNode.getLevel());
        }

        this.update(masterNode);

        // 移动关系
        // 将 slaverNode 上的关系都移动到 masterNode 上
        Optional.ofNullable(slaverNode.getIncludeGovernment()).ifPresent(includeRelations -> {
            includeRelations.forEach(includeRelation -> {
                includeRelationService.moveTo(includeRelation.getStartNode().id, masterNode.id, includeRelation);
            });
        });
        Optional.ofNullable(slaverNode.getBeIncludedGovernment()).ifPresent(includeRelations -> {
            includeRelations.forEach(includeRelation -> {
                includeRelationService.moveTo(masterNode.id, includeRelation.getEndNode().id, includeRelation);
            });
        });
        Optional.ofNullable(slaverNode.getTakeOfficePerson()).ifPresent(takeOfficeRelations -> {
            takeOfficeRelations.forEach(takeOfficeRelation -> {
                takeOfficeRelationService.moveTo(takeOfficeRelation.getStartNode().id, masterNode.id, takeOfficeRelation);
            });
        });
        Optional.ofNullable(slaverNode.getIncludePosition()).ifPresent(includeRelations -> {
            includeRelations.forEach(includeRelation -> {
                includeRelationService.moveTo(includeRelation.getStartNode().id, masterNode.id, includeRelation);
            });
        });

        // 删除 slaverNode
        this.deleteById(slaverNode.id);

    }

    public Government getGovernmentWithAllRelationshipById(Long id) {
        Government government = Option.of(governmentDao.findGovernmentById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        government.setIncludeGovernment(includeRelationDao.findIncludeGovernmentByGovernment(government));
        government.setIncludePosition(includeRelationDao.findIncludePositionByGovernment(government));

        return government;
    }
}
