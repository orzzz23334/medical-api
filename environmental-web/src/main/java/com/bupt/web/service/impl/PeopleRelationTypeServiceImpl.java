package com.bupt.web.service.impl;

import com.bupt.web.dao.PeopleRelationTypeDao;
import com.bupt.web.model.pojo.PeopleRelationType;
import com.bupt.web.service.PeopleRelationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeopleRelationTypeServiceImpl implements PeopleRelationTypeService {

    @Autowired
    private PeopleRelationTypeDao peopleRelationTypeDao;
    @Override
    public List<PeopleRelationType> getAllPeopleRelationType() {
        return peopleRelationTypeDao.getAllPeopleRelationType();
    }
}
