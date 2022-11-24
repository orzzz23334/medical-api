package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.EvaluateBidDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.EvaluateBid;
import com.bupt.kg.service.relation.EvaluateBidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class EvaluateBidServiceImpl implements EvaluateBidService {
    @Autowired
    private EvaluateBidDao evaluateBidDao;

    @Override
    public void deleteById(Long id) {
        evaluateBidDao.deleteById(id);
    }

    @Override
    public void update(EvaluateBid evaluateBid) {
        if(evaluateBid.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        evaluateBidDao.save(evaluateBid);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, EvaluateBid evaluateBid) {
        if (evaluateBid.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, evaluateBid)) {
            evaluateBidDao.addAgentByNodeId(startNodeId, endNodeId, evaluateBid);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, EvaluateBid evaluateBid) {
        evaluateBidDao.deleteById(evaluateBid.id);

        evaluateBid.setId(null);
        evaluateBid.setStartNode(null);
        evaluateBid.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, evaluateBid);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, EvaluateBid evaluateBid) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<EvaluateBid> relationsInDB = evaluateBidDao.getEvaluateBidByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(evaluateBid)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
