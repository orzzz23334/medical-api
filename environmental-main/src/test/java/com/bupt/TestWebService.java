package com.bupt;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.service.entity.PersonService;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.vo.MenuVo;
import com.bupt.web.model.vo.PageData;
import com.bupt.web.model.vo.RoleVo;
import com.bupt.web.service.MenuService;
import com.bupt.web.service.RoleService;
import com.bupt.web.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest()
public class TestWebService {
    @Autowired
    private PersonService personService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Test
    public void testUserService(){
        PageData<User> userPage = userService.selectUserList(new Page<>(1,2), new User());
        System.out.println(userPage);
//        System.out.println(userPage.getTotal());
//        System.out.println(userPage.getPages());
//        System.out.println(userPage.getCurrent());
//        System.out.println(userPage.getRecords().size());
//        System.out.println(userPage.getRecords());
    }


    @Test
    public void testRoleService(){
        List<RoleVo> rolePermissionByUserId = roleService.getRolePermissionByUserId(4L);
        System.out.println(rolePermissionByUserId);
    }

    @Test
    public void testMenuService(){
        List<MenuVo> menus = menuService.selectMenuTreeByUserId(4L);
        System.out.println(menus.size());
        System.out.println(menus);
    }
    @Test
    public void testService(){
        GraphData personById = personService.getPersonGraphById(542786L,10);
        System.out.println(personById);
    }
}
