package com.bupt;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.dao.MenuDao;
import com.bupt.web.dao.PeopleRelationTypeDao;
import com.bupt.web.dao.RoleDao;
import com.bupt.web.dao.UserDao;
import com.bupt.web.model.pojo.Menu;
import com.bupt.web.model.pojo.PeopleRelationType;
import com.bupt.web.model.pojo.Role;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.vo.PageData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest()
public class TestWebDao {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PeopleRelationTypeDao peopleRelationTypeDao;

    @Test
    public void testMenuDaoAll(){
        List<Menu> sourceTree = menuDao.selectMenuTreeAll();
        System.out.println(sourceTree);
    }

    @Test
    public void testMenuDaoByUserId(){
        List<Menu> sourceTree = menuDao.selectMenuTreeByUserId(2L);
        System.out.println(sourceTree.size());
        System.out.println(sourceTree);
    }
    @Test
    public void  testUserDao(){
//        List<User> admin = userDao.selectUserByUserName("kg-admin");
//        System.out.println(admin);
//        List<User> users = userDao.selectUserById(9L);
//        System.out.println(users);
//        Page<User> userPage = userDao.selectAll(new Page<>(1,2));
//        System.out.println(userPage.getTotal());
//        System.out.println(userPage.getPages());
//        System.out.println(userPage.getCurrent());
//        System.out.println(userPage.getRecords().size());
//        PageData pageData = new PageData(userPage);
//        System.out.println(pageData);
    }

    @Test
    public void  testPeopleRelationTypeDao(){
        PeopleRelationType peopleRelationTypes = peopleRelationTypeDao.getPeopleRelationTypeByCode(200);

        System.out.println(peopleRelationTypes);
    }
}
