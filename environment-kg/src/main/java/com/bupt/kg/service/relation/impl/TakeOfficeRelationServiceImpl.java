package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.TakeOfficeRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.TakeOfficeRelation;
import com.bupt.kg.service.relation.TakeOfficeRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class TakeOfficeRelationServiceImpl implements TakeOfficeRelationService {
    @Autowired
    private TakeOfficeRelationDao takeOfficeRelationDao;
    @Override
    public void update(TakeOfficeRelation takeOfficeRelation) {
        if (takeOfficeRelation.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        takeOfficeRelationDao.save(takeOfficeRelation);
    }

    @Override
    public void deleteById(Long id) {
        takeOfficeRelationDao.deleteById(id);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, TakeOfficeRelation takeOffice) {
        if (takeOffice.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, takeOffice)) {
            takeOfficeRelationDao.addTakeOfficeByNodeId(startNodeId, endNodeId, takeOffice);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, TakeOfficeRelation takeOffice) {
        takeOfficeRelationDao.deleteById(takeOffice.id);

        takeOffice.setId(null);
        takeOffice.setStartNode(null);
        takeOffice.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, takeOffice);
        }catch (KgException kgException){
            log.info("?????????????????????"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, TakeOfficeRelation takeOffice) {
        // TODO: ?????????????????????
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // ??????????????????????????????????????????
        List<TakeOfficeRelation> relationsInDB = takeOfficeRelationDao.getTakeOfficeRelationByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // ?????? ??????????????????????????????????????????????????????
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(takeOffice)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
