package com.bupt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRoleDao extends BaseMapper<UserRole> {
}
