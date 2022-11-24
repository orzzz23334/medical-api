package com.bupt;

import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.dao.entity.CompanyDao;
import com.bupt.kg.dao.entity.PersonDao;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.Person;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@SpringBootTest()
public class TestUserDao {
    @Autowired
    private PersonDao personDao;
    @Autowired
    private CompanyDao companyDao;
    public String getRelationType() {
        return this.getClass().getAnnotation(RelationshipEntity.class).value();
    }

    @Test
    public void testCount(){
        Long count = personDao.getCount();
        System.out.println(count);
    }

    @Test
    public void testDeleteById(){
        personDao.deleteById(55L);
    }

    @Test
    public void testAddPerson(){
//        Person person = new Person();
//        person.setName("张三");
//        Person save = personDao.save(person);
//        System.out.println("save:"+save);
//        System.out.println("find:"+personDao.findPersonById(55L).get(0));
    }

    @Test
    public void testUpdatePerson(){
//        Person person = new Person();
//        person.setId(41L);
//        person.setGender("男");
//        person.setName("祝彦");
//        person.setNation("汉族");
//        Person save = personDao.save(person);
//        System.out.println("save:"+save);
//        System.out.println(personDao.findPersonById(55L).get(0));
    }

    @Test
    public void testFindUserPage(){
        Page<Person> all = personDao.findAll(PageRequest.of(0, 5));
        System.out.println(new PageData<>(all));
    }

    @Test
    public void testFindUserById() {
        List<Person> personById = personDao.findPersonById(55L);
        for (Person person : personById) {
            System.out.println(person);
        }
    }

    @Test
    public void testFindCompanyById() {
        List<Company> companyById = companyDao.findCompanyById(1234871L);
        for (Company company : companyById) {
            System.out.println(company);
//            System.out.println(company.getBeStockedCompany());
//            System.out.println(company.getStockHolderCompany());
            System.out.println(company.getStockHolderPerson());
        }
        companyById.forEach(System.out::println);
    }
}

