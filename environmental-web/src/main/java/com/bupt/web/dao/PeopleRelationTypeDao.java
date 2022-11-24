package com.bupt.web.dao;

import com.bupt.web.model.pojo.PeopleRelationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PeopleRelationTypeDao {
    List<PeopleRelationType> getAllPeopleRelationType();
    PeopleRelationType getPeopleRelationTypeByCode(@Param("code") Integer code);
}
