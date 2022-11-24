package com.bupt.kg.controller;

import com.alibaba.fastjson.JSON;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.common.response.ResponseData;
import com.bupt.kg.dao.relation.WinBidDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.*;
import com.bupt.kg.service.relation.*;
import com.bupt.kg.utils.RelationUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Api(tags = "kg relation接口")
@RestController
@RequestMapping("/kg/relation")
public class RelationController {
    @Autowired
    private AttendRelationService attendRelationService;
    @Autowired
    private ConstructService constructService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private IncludeRelationService includeRelationService;
    @Autowired
    private StockholderRelationService stockholderRelationService;
    @Autowired
    private SupplierRelationService supplierRelationService;
    @Autowired
    private TakeOfficeRelationService takeOfficeRelationService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private WinBidService winBidService;
    @Autowired
    private EvaluateBidService evaluateBidService;
    @Autowired
    private IsFriendOfService isFriendOfService;

    @PutMapping("/{relation}/{id}")
    @ApiOperation(value = "更新关系的属性", response = ResponseData.class)
    public ResponseData updateRelation(@ApiParam("关系类型") @PathVariable("relation")String relationType,
                                       @ApiParam("关系id") @PathVariable("id") Long id,
                                       @ApiParam("起节点类型") @RequestParam(value = "startNodeType") String startNodeType,
                                       @ApiParam("止节点类型") @RequestParam(value = "endNodeType") String endNodeType,
                                       @ApiParam("封装关系的属性（必须包含id字段）") @RequestBody Object data){
        if (relationType == null || id == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }

        RelationAbstract relation = RelationUtils.getRelationByType(relationType, startNodeType, endNodeType, data);
        switch (relationType){
            case RelationConstant.ATTEND:
                attendRelationService.update((AttendRelation) relation);
                break;
            case RelationConstant.AGENT:
                agentService.update((Agent) relation);
                break;
            case RelationConstant.CONSTRUCT:
                constructService.update((Construct) relation);
                break;
            case RelationConstant.EVALUATE:
                evaluateService.update((Evaluate) relation);
                break;
            case RelationConstant.INCLUDE:
                includeRelationService.update((IncludeRelation) relation);
                break;
            case RelationConstant.STOCKHOLDER:
                stockholderRelationService.update((StockholderRelation) relation);
                break;
            case RelationConstant.SUPPLY:
                supplierRelationService.update((SupplierRelation) relation);
                break;
            case RelationConstant.TAKE_OFFICE:
                takeOfficeRelationService.update((TakeOfficeRelation) relation);
                break;
            case RelationConstant.EVALUATE_BID:
                evaluateBidService.update((EvaluateBid) relation);
                break;
            case RelationConstant.WINBID:
                winBidService.update((WinBid) relation);
                break;
            case RelationConstant.IS_FRIEND_OF:
                isFriendOfService.update((IsFriendOf) relation);
                break;
            default:
                throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK);
    }

    @DeleteMapping ("/{relation}/{id}")
    @ApiOperation(value = "删除指定的关系", response = ResponseData.class)
    public ResponseData deleteRelation(@ApiParam("关系类型") @PathVariable("relation")String relationType,
                                       @ApiParam("关系id") @PathVariable("id") Long id){

        if (relationType == null || id == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        switch (relationType){
            case RelationConstant.ATTEND:
                attendRelationService.deleteById(id);
                break;
            case RelationConstant.AGENT:
                agentService.deleteById(id);
                break;
            case RelationConstant.CONSTRUCT:
                constructService.deleteById(id);
                break;
            case RelationConstant.EVALUATE:
                evaluateService.deleteById(id);
                break;
            case RelationConstant.INCLUDE:
                includeRelationService.deleteById(id);
                break;
            case RelationConstant.STOCKHOLDER:
                stockholderRelationService.deleteById(id);
                break;
            case RelationConstant.SUPPLY:
                supplierRelationService.deleteById(id);
                break;
            case RelationConstant.TAKE_OFFICE:
                takeOfficeRelationService.deleteById(id);
                break;
            case RelationConstant.EVALUATE_BID:
                evaluateBidService.deleteById(id);
                break;
            case RelationConstant.WINBID:
                winBidService.deleteById(id);
                break;
            case RelationConstant.IS_FRIEND_OF:
                isFriendOfService.deleteById(id);
                break;
            default:
                throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK);
    }
    @PostMapping("/{relation}")
    @ApiOperation(value = "在指定节点之间增加关系", response = ResponseData.class)
    public ResponseData addRelationByNodeId(@ApiParam("添加的关系类型") @PathVariable("relation") String relationType,
                                            @ApiParam("头节点ID") @RequestParam("startNodeId") Long startNodeId,
                                            @ApiParam("尾节点ID") @RequestParam("endNodeId") Long endNodeId,
                                            @ApiParam("封装关系的属性（不允许包含id字段）") @RequestBody Object data){

        if (relationType == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }

        switch (relationType){
            case RelationConstant.ATTEND:
                AttendRelation attendRelation = JSON.parseObject(JSON.toJSONString(data), AttendRelation.class);
                attendRelationService.add(startNodeId, endNodeId, attendRelation);
                break;
            case RelationConstant.AGENT:
                Agent agent = JSON.parseObject(JSON.toJSONString(data), Agent.class);
                agentService.add(startNodeId, endNodeId, agent);
                break;
            case RelationConstant.CONSTRUCT:
                Construct construct = JSON.parseObject(JSON.toJSONString(data), Construct.class);
                constructService.add(startNodeId, endNodeId, construct);
                break;
            case RelationConstant.EVALUATE:
                Evaluate evaluate = JSON.parseObject(JSON.toJSONString(data), Evaluate.class);
                evaluateService.add(startNodeId, endNodeId, evaluate);
                break;
            case RelationConstant.INCLUDE:
                IncludeRelation includeRelation = JSON.parseObject(JSON.toJSONString(data), IncludeRelation.class);
                includeRelationService.add(startNodeId, endNodeId, includeRelation);
                break;
            case RelationConstant.STOCKHOLDER:
                StockholderRelation stockholderRelation = JSON.parseObject(JSON.toJSONString(data), StockholderRelation.class);
                stockholderRelationService.add(startNodeId, endNodeId, stockholderRelation);
                break;
            case RelationConstant.SUPPLY:
                SupplierRelation supplierRelation = JSON.parseObject(JSON.toJSONString(data), SupplierRelation.class);
                supplierRelationService.add(startNodeId, endNodeId, supplierRelation);
                break;
            case RelationConstant.TAKE_OFFICE:
                TakeOfficeRelation takeOfficeRelation = JSON.parseObject(JSON.toJSONString(data), TakeOfficeRelation.class);
                takeOfficeRelationService.add(startNodeId, endNodeId, takeOfficeRelation);
                break;
            case RelationConstant.EVALUATE_BID:
                EvaluateBid evaluateBid = JSON.parseObject(JSON.toJSONString(data), EvaluateBid.class);
                evaluateBidService.add(startNodeId, endNodeId, evaluateBid);
                break;
            case RelationConstant.WINBID:
                WinBid winBid = JSON.parseObject(JSON.toJSONString(data), WinBid.class);
                winBidService.add(startNodeId, endNodeId, winBid);
                break;
            case RelationConstant.IS_FRIEND_OF:
                IsFriendOf isFriendOf = JSON.parseObject(JSON.toJSONString(data), IsFriendOf.class);
                isFriendOf.setInfoChannel("人工添加"); // 设置默认值
                String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
                isFriendOf.setDataSource("人工添加@"+today);
                isFriendOfService.add(startNodeId, endNodeId, isFriendOf);
                break;
            default:
                throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK);
    }

}
