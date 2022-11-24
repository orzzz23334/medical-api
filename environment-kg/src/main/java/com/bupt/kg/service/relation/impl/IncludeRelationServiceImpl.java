package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.IncludeRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.service.relation.IncludeRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class IncludeRelationServiceImpl implements IncludeRelationService {
    @Autowired
    private IncludeRelationDao includeRelationDao;
    @Override
    public void update(IncludeRelation includeRelation) {
        if (includeRelation.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        includeRelationDao.save(includeRelation);
    }

    @Override
    public void deleteById(Long id) {
        includeRelationDao.deleteById(id);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, IncludeRelation include) {
        if (include.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        // TODO: 查重逻辑写在这
        if (!hasRelation(startNodeId, endNodeId, include)) {
            includeRelationDao.addIncludeByNodeId(startNodeId, endNodeId, include);
        }
    }

    @Override
    /**
     *  1. 删除此关系
     *  2. 如果目标节点之间有没属性相同的关系，则将此关系添加到目标节点之间
     */
    public void moveTo(Long startNodeId, Long endNodeId, IncludeRelation includeRelation) {
        includeRelationDao.deleteById(includeRelation.id);

        includeRelation.setId(null);
        includeRelation.setStartNode(null);
        includeRelation.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, includeRelation);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    /**
     * 根据 起始节点id 和 relation的属性 来判断是否存在这个关系
     *
     * @param startNodeId 目标关系的开始节点id
     * @param endNodeId 目标关系的结束节点id
     * @param includeRelation 封装目标关系的属性，作为查重判断依据，不会访问这个关系的 startNode 和 endNode 字段，允许没有这两个字段
     * @return 数据库中是否存在这个关系
     */
    private boolean hasRelation(Long startNodeId, Long endNodeId, IncludeRelation includeRelation) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<IncludeRelation> relationsInDB = includeRelationDao.getIncludeRelationByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(includeRelation)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
