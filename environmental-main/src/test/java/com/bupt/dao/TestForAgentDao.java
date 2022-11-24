package com.bupt.dao;

import com.bupt.kg.dao.entity.PersonDao;
import com.bupt.kg.dao.relation.AgentDao;
import com.bupt.kg.model.relation.Agent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestForAgentDao {
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private PersonDao personDao;

    @Test
    public void testForGetSameNamePerson(){
        List<String> personNamesByhasSamePersonName = personDao.findPersonNamesByhasSamePersonName();
        personNamesByhasSamePersonName.forEach(personname -> System.out.println(personname));
    }

//    @Test
//    public void testForAddAgentByNodeId(){
//        Agent agent = new Agent();
//        agent.setContact("");
//        agentDao.addAgentByNodeId(1928692L, 1928683L, agent);
//
//    }
}
