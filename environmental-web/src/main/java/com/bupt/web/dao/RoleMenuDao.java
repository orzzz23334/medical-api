package com.bupt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.web.model.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleMenuDao extends BaseMapper<com.bupt.web.model.pojo.RoleMenu> {
}
