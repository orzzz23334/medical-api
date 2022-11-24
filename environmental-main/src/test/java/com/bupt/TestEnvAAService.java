package com.bupt;

import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.service.entity.EnvAssessmentAnnouncementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class TestEnvAAService {

    @Autowired
    public EnvAssessmentAnnouncementService envAssessmentAnnouncementService;


    @Test
    public void testgetEnvAAById(){
        Long a = 100L;
        Long b = 100L;

        System.out.println(a==b);
    }


}
