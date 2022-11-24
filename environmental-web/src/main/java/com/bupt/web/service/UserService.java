package com.bupt.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.vo.PageData;
import com.bupt.web.model.vo.UserDetail;

import java.util.List;

public interface UserService {

    UserDetail getUserByName(String name);

    int register(User user);

    int update(User user);

    int delete(Long id);

    /**
     * 用户查询
     * @param user 用户信息
     * @return 查询到的用户
     */
    PageData<User> selectUserList(Page<User> page, User user);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public Boolean checkUserNameUnique(String userName);

}
