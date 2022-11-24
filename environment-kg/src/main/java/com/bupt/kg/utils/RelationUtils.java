package com.bupt.kg.utils;

import com.alibaba.fastjson.JSON;
import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.entity.*;
import com.bupt.kg.model.relation.*;

import java.util.List;

public class RelationUtils {
    public static RelationAbstract getRelationByType(String relationType, String startNodeType, String endNodeType, Object data) {
        // 创建关系对象
        // 由于修改关系的时候不允许起始节点为空，所以需要对起始节点进行赋值
        RelationAbstract relation = null;
        switch (relationType) {
            case RelationConstant.ATTEND:
                if (EntityConstant.PERSON.equals(startNodeType) && EntityConstant.SCHOOL.equals(endNodeType)){
                    AttendRelation<Person, School> attendRelation = JSON.parseObject(JSON.toJSONString(data), AttendRelation.class);
                    attendRelation.setStartNode(new Person());
                    attendRelation.setEndNode(new School());
                    relation = attendRelation;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.AGENT:
                if(EntityConstant.COMPANY.equals(startNodeType) && EntityConstant.BIDDING_ANNOUNCEMENT.equals(endNodeType)){
                    Agent<Company, BiddingAnnouncement> agent = JSON.parseObject(JSON.toJSONString(data), Agent.class);
                    agent.setStartNode(new Company());
                    agent.setEndNode(new BiddingAnnouncement());
                    relation = agent;
                }else if (EntityConstant.COMPANY.equals(startNodeType) && EntityConstant.BID_WINNING_ANNOUNCEMENT.equals(endNodeType)) {
                    Agent<Company, BidWinningAnnouncement> agent = JSON.parseObject(JSON.toJSONString(data), Agent.class);
                    agent.setStartNode(new Company());
                    agent.setEndNode(new BidWinningAnnouncement());
                    relation = agent;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.CONSTRUCT:
                if (EntityConstant.COMPANY.equals(startNodeType) && EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT.equals(endNodeType)){
                    Construct<Company, EnvAssessmentAnnouncement> construct = JSON.parseObject(JSON.toJSONString(data), Construct.class);
                    construct.setStartNode(new Company());
                    construct.setEndNode(new EnvAssessmentAnnouncement());
                    relation = construct;
                }else if(EntityConstant.COMPANY.equals(startNodeType) && EntityConstant.BIDDING_ANNOUNCEMENT.equals(endNodeType)){
                    Construct<Company, BiddingAnnouncement> construct = JSON.parseObject(JSON.toJSONString(data), Construct.class);
                    construct.setStartNode(new Company());
                    construct.setEndNode(new BiddingAnnouncement());
                    relation = construct;
                }else if(EntityConstant.COMPANY.equals(startNodeType) && EntityConstant.BID_WINNING_ANNOUNCEMENT.equals(endNodeType)){
                    Construct<Company, BidWinningAnnouncement> construct = JSON.parseObject(JSON.toJSONString(data), Construct.class);
                    construct.setStartNode(new Company());
                    construct.setEndNode(new BidWinningAnnouncement());
                    relation = construct;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.EVALUATE:
                if (EntityConstant.COMPANY.equals(startNodeType) && EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT.equals(endNodeType)){
                    Evaluate<Company, EnvAssessmentAnnouncement> evaluate = JSON.parseObject(JSON.toJSONString(data), Evaluate.class);
                    evaluate.setStartNode(new Company());
                    evaluate.setEndNode(new EnvAssessmentAnnouncement());
                    relation = evaluate;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.INCLUDE:
                if (EntityConstant.POSITION.equals(startNodeType) && EntityConstant.COMPANY.equals(endNodeType)){
                    IncludeRelation<Position, Company> includeRelation = JSON.parseObject(JSON.toJSONString(data), IncludeRelation.class);
                    includeRelation.setStartNode(new Position());
                    includeRelation.setEndNode(new Company());
                    relation = includeRelation;
                } else if (EntityConstant.GOVERNMENT.equals(startNodeType) && EntityConstant.GOVERNMENT.equals(endNodeType)){
                    IncludeRelation<Government, Government> includeRelation = JSON.parseObject(JSON.toJSONString(data), IncludeRelation.class);
                    includeRelation.setStartNode(new Government());
                    includeRelation.setEndNode(new Government());
                    relation = includeRelation;
                } else if (EntityConstant.POSITION.equals(startNodeType) && EntityConstant.GOVERNMENT.equals(endNodeType)){
                    IncludeRelation<Position, Government> includeRelation = JSON.parseObject(JSON.toJSONString(data), IncludeRelation.class);
                    includeRelation.setStartNode(new Position());
                    includeRelation.setEndNode(new Government());
                    relation = includeRelation;
                } else if (EntityConstant.POSITION.equals(startNodeType) && EntityConstant.SCHOOL.equals(endNodeType)){
                    IncludeRelation<Position, School> includeRelation = JSON.parseObject(JSON.toJSONString(data), IncludeRelation.class);
                    includeRelation.setStartNode(new Position());
                    includeRelation.setEndNode(new School());
                    relation = includeRelation;
                } else if (EntityConstant.POSITION.equals(startNodeType) && EntityConstant.POSITION.equals(endNodeType)){
                    IncludeRelation<Position, Position> includeRelation = JSON.parseObject(JSON.toJSONString(data), IncludeRelation.class);
                    includeRelation.setStartNode(new Position());
                    includeRelation.setEndNode(new Position());
                    relation = includeRelation;
                } else if (EntityConstant.SCHOOL.equals(startNodeType) && EntityConstant.SCHOOL.equals(endNodeType)){
                    IncludeRelation<School, School> includeRelation = JSON.parseObject(JSON.toJSONString(data), IncludeRelation.class);
                    includeRelation.setStartNode(new School());
                    includeRelation.setEndNode(new School());
                    relation = includeRelation;
                }else if (EntityConstant.BID.equals(startNodeType) && EntityConstant.BID_WINNING_ANNOUNCEMENT.equals(endNodeType)){
                    IncludeRelation<Bid, BidWinningAnnouncement> includeRelation = JSON.parseObject(JSON.toJSONString(data), IncludeRelation.class);
                    includeRelation.setStartNode(new Bid());
                    includeRelation.setEndNode(new BidWinningAnnouncement());
                    relation = includeRelation;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.STOCKHOLDER:
                if (EntityConstant.COMPANY.equals(startNodeType) && EntityConstant.COMPANY.equals(endNodeType)){
                    StockholderRelation<Company, Company> stockholderRelation = JSON.parseObject(JSON.toJSONString(data), StockholderRelation.class);
                    stockholderRelation.setStartNode(new Company());
                    stockholderRelation.setEndNode(new Company());
                    relation = stockholderRelation;
                } else if (EntityConstant.PERSON.equals(startNodeType) && EntityConstant.COMPANY.equals(endNodeType)){
                    StockholderRelation<Person, Company> stockholderRelation = JSON.parseObject(JSON.toJSONString(data), StockholderRelation.class);
                    stockholderRelation.setStartNode(new Person());
                    stockholderRelation.setEndNode(new Company());
                    relation = stockholderRelation;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.SUPPLY:

                break;
            case RelationConstant.TAKE_OFFICE:
                if (EntityConstant.PERSON.equals(startNodeType) && EntityConstant.COMPANY.equals(endNodeType)){
                    TakeOfficeRelation<Person, Company> takeOfficeRelation = JSON.parseObject(JSON.toJSONString(data), TakeOfficeRelation.class);
                    takeOfficeRelation.setStartNode(new Person());
                    takeOfficeRelation.setEndNode(new Company());
                    relation = takeOfficeRelation;
                } else if (EntityConstant.PERSON.equals(startNodeType) && EntityConstant.GOVERNMENT.equals(endNodeType)){
                    TakeOfficeRelation<Person, Government> takeOfficeRelation = JSON.parseObject(JSON.toJSONString(data), TakeOfficeRelation.class);
                    takeOfficeRelation.setStartNode(new Person());
                    takeOfficeRelation.setEndNode(new Government());
                    relation = takeOfficeRelation;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.WINBID:
                if (EntityConstant.COMPANY.equals(startNodeType) && EntityConstant.BID_WINNING_ANNOUNCEMENT.equals(endNodeType)) {
                    WinBid<Company, BidWinningAnnouncement> winBid = JSON.parseObject(JSON.toJSONString(data), WinBid.class);
                    winBid.setStartNode(new Company());
                    winBid.setEndNode(new BidWinningAnnouncement());
                    relation = winBid;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.EVALUATE_BID:
                if (EntityConstant.PERSON.equals(startNodeType) && EntityConstant.BID_WINNING_ANNOUNCEMENT.equals(endNodeType)) {
                    EvaluateBid<Person, BidWinningAnnouncement> evaluateBid= JSON.parseObject(JSON.toJSONString(data), EvaluateBid.class);
                    evaluateBid.setStartNode(new Person());
                    evaluateBid.setEndNode(new BidWinningAnnouncement());
                    relation = evaluateBid;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            case RelationConstant.IS_FRIEND_OF:
                if (EntityConstant.PERSON.equals(startNodeType) && EntityConstant.PERSON.equals(endNodeType)) {
                    IsFriendOf isFriendOf = JSON.parseObject(JSON.toJSONString(data), IsFriendOf.class);
                    isFriendOf.setStartNode(new Person());
                    isFriendOf.setEndNode(new Person());
                    relation = isFriendOf;
                }else {
                    throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
                }
                break;
            default:
                throw new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST);
        }
        return relation;

    }
    public static <T> List<T> getRelationLimited(List<T> relations, Integer nodeLimit){
        return relations.subList(0, Math.min(relations.size(), nodeLimit));
    }
}