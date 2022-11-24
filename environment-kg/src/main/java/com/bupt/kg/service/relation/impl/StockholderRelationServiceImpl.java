package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.StockholderRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.StockholderRelation;
import com.bupt.kg.service.relation.StockholderRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class StockholderRelationServiceImpl implements StockholderRelationService {
    @Autowired
    private StockholderRelationDao stockholderRelationDao;
    @Override
    public void update(StockholderRelation stockholderRelation) {
        if (stockholderRelation.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        stockholderRelationDao.save(stockholderRelation);
    }

    @Override
    public void deleteById(Long id) {
        stockholderRelationDao.deleteById(id);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, StockholderRelation stockholder) {
        if (stockholder.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, stockholder)) {
            stockholderRelationDao.addStockholderByNodeId(startNodeId, endNodeId, stockholder);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, StockholderRelation stockholder) {
        stockholderRelationDao.deleteById(stockholder.id);

        stockholder.setId(null);
        stockholder.setStartNode(null);
        stockholder.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, stockholder);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, StockholderRelation stockholder) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<StockholderRelation> relationsInDB = stockholderRelationDao.getStockholderRelationByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(stockholder)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
