package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.AttendRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.service.relation.AttendRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class AttendRelationServiceImpl implements AttendRelationService {
    @Autowired
    private AttendRelationDao attendRelationDao;
    @Override
    public void deleteById(Long id) {
        attendRelationDao.deleteById(id);
    }

    @Override
    public void update(AttendRelation attendRelation) {
        if (attendRelation.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        attendRelationDao.save(attendRelation);

    }

    @Override
    public void add(Long startNodeId, Long endNodeId, AttendRelation attendRelation) {
        if (attendRelation.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, attendRelation)) {
            attendRelationDao.addAttendByNodeId(startNodeId, endNodeId, attendRelation);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, AttendRelation attendRelation) {
        attendRelationDao.deleteById(attendRelation.id);

        attendRelation.setId(null);
        attendRelation.setStartNode(null);
        attendRelation.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, attendRelation);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, AttendRelation attendRelation) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<AttendRelation> relationsInDB = attendRelationDao.getAttendRelationByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(attendRelation)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
