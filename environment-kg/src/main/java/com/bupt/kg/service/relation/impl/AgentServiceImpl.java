package com.bupt.kg.service.relation.impl;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.BiddingAnnouncementDao;
import com.bupt.kg.dao.entity.CompanyDao;
import com.bupt.kg.dao.relation.AgentDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.model.relation.IncludeRelation;
import com.bupt.kg.service.relation.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class AgentServiceImpl implements AgentService {
    @Autowired
    private AgentDao agentDao;

    @Override
    public void deleteById(Long id) {
        agentDao.deleteById(id);
    }

    @Override
    public void update(Agent agent) {
        if(agent.getId() == null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        agentDao.save(agent);
    }

    @Override
    public void add(Long startNodeId, Long endNodeId, Agent agent) {
        if (agent.getId() != null){
            throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
        }
        if (startNodeId == endNodeId) {
            throw new KgException(ResultCodeEnum.AUTO_REGRESSION);
        }
        if (!hasRelation(startNodeId, endNodeId, agent)) {
            agentDao.addAgentByNodeId(startNodeId, endNodeId, agent);
        }
    }

    @Override
    public void moveTo(Long startNodeId, Long endNodeId, Agent agent) {
        agentDao.deleteById(agent.id);

        agent.setId(null);
        agent.setStartNode(null);
        agent.setEndNode(null);
        try {
            this.add(startNodeId, endNodeId, agent);
        }catch (KgException kgException){
            log.info("跳过自回归关系"+startNodeId+"-"+endNodeId);
        }

    }

    private boolean hasRelation(Long startNodeId, Long endNodeId, Agent agent) {
        // TODO: 查重逻辑写在这
        AtomicReference<Boolean> hasRelation = new AtomicReference<>(false);

        // 拿到给定节点之间的此类型关系
        List<Agent> relationsInDB = agentDao.getAgentByStartNodeIdAndEndNodeId(startNodeId, endNodeId);

        // 判断 拿到的关系中有没有属性完全相同的关系
        Optional.ofNullable(relationsInDB).ifPresent(relations -> {
            relations.forEach(relation -> {
                if (relation.equalsByProperties(agent)) {
                    hasRelation.set(true);
                }
            });
        });

        return hasRelation.get();
    }
}
