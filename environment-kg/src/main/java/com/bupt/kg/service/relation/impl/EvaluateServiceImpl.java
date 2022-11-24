package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.EvaluateDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.Evaluate;
import com.bupt.kg.service.relation.EvaluateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class EvaluateServiceImpl implements EvaluateService {
    @Autowired
    private EvaluateDao evaluateDao;
    @Override
    public void update(Evaluate evaluate) {
        if (evaluate.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        evaluateDao.save(evaluate);
    }

    @Override
    public void deleteById(Long id) {
        evaluateDao.deleteById(id);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, Evaluate evaluate) {
        if (evaluate.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, evaluate)) {
            evaluateDao.addEvaluateByNodeId(startNodeId, endNodeId, evaluate);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, Evaluate evaluate) {
        evaluateDao.deleteById(evaluate.id);

        evaluate.setId(null);
        evaluate.setStartNode(null);
        evaluate.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, evaluate);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, Evaluate evaluate) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<Evaluate> relationsInDB = evaluateDao.getEvaluateByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(evaluate)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
