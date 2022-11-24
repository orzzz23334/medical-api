package com.bupt.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.common.enums.ResultCodeEnum;
import com.bupt.web.common.enums.UimErrorEnum;
import com.bupt.web.dao.UserDao;
import com.bupt.web.dao.UserRoleDao;
import com.bupt.web.exception.WebException;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.pojo.UserRole;
import com.bupt.web.model.vo.PageData;
import com.bupt.web.model.vo.UserDetail;
import com.bupt.web.service.UserService;

import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public UserDetail getUserByName(String name) {
        List<User> users = userDao.selectUserByUserName(name);
        if (users.isEmpty()) {
            throw new WebException(UimErrorEnum.USER_IS_NOT_EXISTS);
        } else {
            User user = users.get(0);
            UserDetail userDetail = new UserDetail();
            userDetail.setId(user.getId());
            userDetail.setName(user.getName());
            userDetail.setPassword(user.getPassword());
            userDetail.setRoles(user.getRoles());
            userDetail.setRoleIds(user.getRoleIds());
//            userDetail.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            return userDetail;
        }
    }

    @Override
    public int register(User user) {
        List<User> users = userDao.selectUserByUserName(user.getName());
        if (users.size()> 0){
            throw new WebException(ResultCodeEnum.USER_HAS_EXIST);
        }
        // 用户密码加密
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        int rows = userDao.insert(user);
        Option.of(rows).getOrElseThrow(() -> new WebException(ResultCodeEnum.DB_ADD_FAILURE));
        // 添加用户角色
        insertUserRole(user);
        return rows;
    }

    public void insertUserRole(User user) {
        List<Long> roleIds = user.getRoleIds();
        if (roleIds != null) {
            roleIds.forEach(roleId -> {
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                userRoleDao.insert(ur);
            });

        }
    }

    @Override
    public int update(User user) {
        if (user.getPassword() != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        if (user.getRoleIds() != null) {
            // 删除用户与角色关联
            deleteUserRoleByUserId(user.getId());
            // 重新添加用户与角色关联
            insertUserRole(user);
        }
        // 更新用户
        return Option.of(userDao.updateById(user))
                .getOrElseThrow(() -> new WebException(ResultCodeEnum.DB_UPDATE_ERROR));
    }

    @Override
    public int delete(Long id) {
        // 检查有没有删除用户的权限
        // 删除用户角色
        deleteUserRoleByUserId(id);
        // 删除用户
        return Option.of(userDao.deleteUserById(id))
                .getOrElseThrow(() -> new WebException(ResultCodeEnum.DB_UPDATE_ERROR));
    }

    public void deleteUserRoleByUserId(Long id) {
        QueryWrapper<UserRole> wrapperUser = new QueryWrapper<>();
        wrapperUser.eq("user_id",id);
        userRoleDao.delete(wrapperUser);
    }

    @Override
    public PageData<User> selectUserList(Page<User> page, User user) {
        QueryWrapper<User> wrapperUser = new QueryWrapper<>();
        if (user != null) {
            wrapperUser.eq( user.getId() != null,"id",user.getId());
            wrapperUser.eq( user.getName() != null,"name",user.getName());
            wrapperUser.eq( user.getIsDeleted() != null,"is_deleted",user.getIsDeleted());
        }
        Page<User> userPage = userDao.selectPage(page, wrapperUser);
        return new PageData<>(userPage);
    }

    @Override
    public Boolean checkUserNameUnique(String userName) {
        int count = userDao.checkUserNameUnique(userName);
        return count > 0 ? Boolean.FALSE: Boolean.TRUE;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetail userDetailVo = Option.of(getUserByName(s))
                .getOrElseThrow(() -> new WebException(UimErrorEnum.USER_LOGIN_NAME_ERROR));
        return userDetailVo;
    }
}
