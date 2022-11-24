package com.bupt.web.controller;

import com.bupt.web.common.enums.ResultCodeEnum;
import com.bupt.web.common.response.ResponseData;
import com.bupt.web.model.pojo.PeopleRelationType;
import com.bupt.web.service.PeopleRelationTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "页面渲染所需的信息")
@RestController
@RequestMapping("/web/dict")
public class DisplayController {
    @Autowired
    private PeopleRelationTypeService peopleRelationTypeService;
    @GetMapping("/relation/person/type")
    public ResponseData<List<PeopleRelationType>> getPeopleRelationType() {
        return ResponseData.success(peopleRelationTypeService.getAllPeopleRelationType());
    }

}
