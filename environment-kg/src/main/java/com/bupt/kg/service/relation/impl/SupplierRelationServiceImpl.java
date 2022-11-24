package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.SupplierRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.SupplierRelation;
import com.bupt.kg.service.relation.SupplierRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class SupplierRelationServiceImpl implements SupplierRelationService {

    @Autowired
    private SupplierRelationDao supplierRelationDao;
    @Override
    public void update(SupplierRelation supplierRelation) {
        if (supplierRelation.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        supplierRelationDao.save(supplierRelation);
    }

    @Override
    public void deleteById(Long id) {
        supplierRelationDao.deleteById(id);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, SupplierRelation supplier) {
        if (supplier.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, supplier)) {
            supplierRelationDao.addSupplierByNodeId(startNodeId, endNodeId, supplier);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, SupplierRelation supplier) {
        supplierRelationDao.deleteById(supplier.id);

        supplier.setId(null);
        supplier.setStartNode(null);
        supplier.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, supplier);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, SupplierRelation supplier) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<SupplierRelation> relationsInDB = supplierRelationDao.getSupplierRelationByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(supplier)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
