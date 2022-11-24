package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.WinBidDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.AttendRelation;
import com.bupt.kg.model.relation.WinBid;
import com.bupt.kg.service.relation.WinBidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class WinBidServiceImpl implements WinBidService {
    @Autowired
    private WinBidDao winBidDao;
    @Override
    public void deleteById(Long id) {
        winBidDao.deleteById(id);
    }

    @Override
    public void update(WinBid winBid) {
        if(winBid.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        winBidDao.save(winBid);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, WinBid winBid) {
        if (winBid.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, winBid)) {
            winBidDao.addAgentByNodeId(startNodeId, endNodeId, winBid);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, WinBid winBid) {
        winBidDao.deleteById(winBid.id);

        winBid.setId(null);
        winBid.setStartNode(null);
        winBid.setEndNode(null);

        try {
            this.add(startNodeId, endNodeId, winBid);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }
    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, WinBid winBid) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<WinBid> relationsInDB = winBidDao.getWinBidByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(winBid)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
