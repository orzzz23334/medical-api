package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.IsFriendOfDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.IsFriendOf;
import com.bupt.kg.service.relation.IsFriendOfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class IsFriendOfServiceImpl implements IsFriendOfService {
    @Autowired
    private IsFriendOfDao isFriendOfDao;
    @Override
    public void update(IsFriendOf isFriendOf) {
        if (isFriendOf.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        isFriendOfDao.save(isFriendOf);
    }

    @Override
    public void deleteById(Long id) {
        isFriendOfDao.deleteById(id);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, IsFriendOf isFriendOf) {
        if (isFriendOf.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, isFriendOf)) {

            List<IsFriendOf> isFriendOfs = new ArrayList<>();

            String[] types = isFriendOf.getRelationType().split(";");
            for (String type : types) {
                isFriendOfs.add(
                        new IsFriendOf(type,
                            isFriendOf.getDegree(),
                            isFriendOf.getDescribe(),
                            isFriendOf.getStartTime(),
                            isFriendOf.getInfoChannel(),
                            isFriendOf.getDataSource())
                );
            }
            isFriendOfs.forEach(relation -> {
                isFriendOfDao.addIsFriendOfByNodeId(startNodeId, endNodeId, relation);
            });
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, IsFriendOf isFriendOf) {
        isFriendOfDao.deleteById(isFriendOf.id);

        isFriendOf.setId(null);
        isFriendOf.setStartNode(null);
        isFriendOf.setEndNode(null);

        try {
            isFriendOf.setInfoChannel("自动合并"); // 设置默认值
            this.add(startNodeId, endNodeId, isFriendOf);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, IsFriendOf isFriendOf) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<IsFriendOf> relationsInDB = isFriendOfDao.getIsFriendOfByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(isFriendOf)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
