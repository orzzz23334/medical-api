package com.bupt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.web.model.pojo.Role;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.vo.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao extends BaseMapper<User> {
    List<User> selectUserByUserName(String userName);

    List<User> selectUserById(Long userId);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public int checkUserNameUnique(String userName);

    int deleteUserById(Long userId);
}
