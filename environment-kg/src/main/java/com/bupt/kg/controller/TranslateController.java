package com.bupt.kg.controller;

import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.common.response.ResponseData;
import com.bupt.kg.model.vo.translate.Translator;
import com.bupt.kg.service.TranslateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kg/translate")
@Api(tags = "翻译的API")
public class TranslateController {

    @Autowired
    TranslateService translateService;

    @GetMapping("/entity/{entityType}")
    @ApiOperation("翻译指定的实体类型")
    public ResponseData getTranslatorByEntityType(@ApiParam("想要翻译的实体") @PathVariable(name = "entityType")String entityType){
        Translator translator = translateService.getTranslatorByEntityType(entityType);
        return ResponseData.setResult(ResultCodeEnum.OK, translator);
    }

    @GetMapping("/entity/all")
    @ApiOperation("翻译所有的实体类型")
    public ResponseData getAllEntityTranslator(){
        List<Translator> translators = translateService.getAllEntityTranslator();
        return ResponseData.setResult(ResultCodeEnum.OK, translators);
    }
    @GetMapping("/relation/{relationType}")
    @ApiOperation("翻译指定类型的关系")
    public ResponseData getTranslatorByRelationType(@ApiParam("想要翻译的关系") @PathVariable(name = "relationType")String relationType){
        Translator translator = translateService.getTranslatorByRelationType(relationType);
        return ResponseData.setResult(ResultCodeEnum.OK, translator);
    }
    @GetMapping("/relation/all")
    @ApiOperation("翻译所有的关系类型")
    public ResponseData getAllRelationTranslator(){
        List<Translator> translators = translateService.getAllRelationTranslator();
        return ResponseData.setResult(ResultCodeEnum.OK, translators);
    }
}
