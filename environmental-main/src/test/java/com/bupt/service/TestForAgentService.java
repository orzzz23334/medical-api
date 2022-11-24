package com.bupt.service;

import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.relation.Agent;
import com.bupt.kg.service.relation.AgentService;
import com.bupt.kg.utils.RelationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestForAgentService {
    @Autowired
    private AgentService agentService;
    @Test
    public void testForAddARelation(){
        Agent<Company, BiddingAnnouncement> agent = new Agent<>();
        agent.setContact("test");


        agentService.add(1928692L,1928683L, agent);

    }
    @Test
    public void testForDeleteRelation(){
        agentService.deleteById(1790752L);
    }

}
