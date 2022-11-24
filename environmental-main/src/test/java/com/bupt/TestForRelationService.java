package com.bupt;

import com.alibaba.fastjson.JSON;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.EnvAssessmentAnnouncement;
import com.bupt.kg.model.relation.Construct;
import com.bupt.kg.model.relation.RelationAbstract;
import com.bupt.kg.service.relation.ConstructService;
import com.bupt.kg.utils.RelationUtils;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class TestForRelationService {
    @Autowired
    private ConstructService constructService;
    @Test
    public void updateRelation(){
        String json = "{\n" +
                "    \"id\": 657728,\n" +
                "    \"contact\":\"\",\n" +
//                "    \"company\":{},\n" +
//                "    \"envAssessmentAnnouncement\":{}\n" +
                "}";



//        constructService.update((Construct) RelationUtils.getRelationByType("CONSTRUCT", "Company", "EnvAssessmentAnnouncement", JSON.parseObject(json, Object.class)));

    }
}
