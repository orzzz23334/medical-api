package com.bupt;

import com.bupt.kg.dao.entity.CompanyDao;
import com.bupt.kg.dao.relation.StockholderRelationDao;
import com.bupt.kg.model.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestStockholderDao {
    @Autowired
    private StockholderRelationDao stockholderRelationDao;
    @Autowired
    private CompanyDao companyDao;
    @Test
    public void testCypher(){
//        List<Company> company = companyDao.findCompanyById(1234871L);
//        System.out.println(stockholderRelationDao.findStockHolderCompanyByCompany(company.get(0)).size());
//        System.out.println(stockholderRelationDao.findStockHolderPersonByCompany(company.get(0)).size());

    }
}
