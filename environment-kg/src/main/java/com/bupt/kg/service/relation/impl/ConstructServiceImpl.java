package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.ConstructDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.Construct;
import com.bupt.kg.service.relation.ConstructService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class ConstructServiceImpl implements ConstructService {
    @Autowired
    private ConstructDao constructDao;
    @Override
    public void update(Construct construct) {
        if (construct.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        constructDao.save(construct);
    }

    @Override
    public void deleteById(Long id) {
        constructDao.deleteById(id);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, Construct construct) {
        if (construct.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, construct)) {
            constructDao.addConstructByNodeId(startNodeId, endNodeId, construct);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, Construct construct) {
        constructDao.deleteById(construct.id);

        construct.setId(null);
        construct.setStartNode(null);
        construct.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, construct);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, Construct construct) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<Construct> relationsInDB = constructDao.getConstructByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(construct)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
