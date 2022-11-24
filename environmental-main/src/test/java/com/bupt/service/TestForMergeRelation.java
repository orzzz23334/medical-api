package com.bupt.service;

import com.bupt.kg.dao.entity.CompanyDao;
import com.bupt.kg.dao.relation.ConstructDao;
import com.bupt.kg.dao.relation.StockholderRelationDao;
import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.relation.Construct;
import com.bupt.kg.service.entity.BidService;
import com.bupt.kg.service.entity.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TestForMergeRelation {

    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private StockholderRelationDao stockholderRelationDao;

    @Autowired
    private ConstructDao constructDao;

    @Autowired
    private BidService bidService;

    @Test
    public void mergeTest() {
        Long id1 = 1111L;
        Long id2 = 2222L;

        Company company1 = companyDao.findCompanyById(id1).get(0);
        Company company2 = companyDao.findCompanyById(id2).get(0);

        //stockHolderCompany
//        company2.getStockHolderCompany().forEach();
    }

    @Test
    public void moveRelation_oneStage() {
        Long targetStart = 1928692L;
        Long originStart = 2143304L;

        Company company1 = companyDao.findCompanyById(targetStart).get(0);
        Construct<Company, BidWinningAnnouncement> construct = constructDao.findById(1720147L).get();

        System.out.println(construct.getStartNode());
        System.out.println(construct.getEndNode());

        construct.setStartNode(company1);


        constructDao.save(construct, 2);
    }

    @Test
    public void moveRelation_twoStage() {
        Long targetStart = 1928692L;
        Long originStart = 2143304L;
        Long relationID = 2963400L;

        Company company1 = companyDao.findCompanyById(targetStart).get(0);
        Construct<Company, BidWinningAnnouncement> construct = constructDao.findById(2963400L).get();

        System.out.println(construct.getStartNode());
        System.out.println(construct.getEndNode());

        constructDao.deleteById(construct.id);

        construct.id = null;

        constructDao.addConstructByNodeId(company1.id, construct.getEndNode().id, construct);
    }


    @Test
    public void saveNode() {
        Bid bidInfoById = bidService.getBidInfoById(1027657L);

        bidService.update(bidInfoById);
    }

}
