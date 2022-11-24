package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.AgentDao;
import com.bupt.kg.dao.relation.ConstructDao;
import com.bupt.kg.dao.relation.IncludeRelationDao;
import com.bupt.kg.dao.relation.StockholderRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.entity.*;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.dao.entity.CompanyDao;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.service.entity.CompanyService;
import com.bupt.kg.service.relation.*;
import com.bupt.kg.utils.RelationUtils;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private StockholderRelationDao stockholderRelationDao;
    @Autowired
    private ConstructDao constructDao;
    @Autowired
    private IncludeRelationDao includeRelationDao;
    @Autowired
    private AgentDao agentDao;
    @Autowired
    private StockholderRelationService stockholderRelationService;
    @Autowired
    private TakeOfficeRelationService takeOfficeRelationService;
    @Autowired
    private IncludeRelationService includeRelationService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private ConstructService constructService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private WinBidService winBidService;

    @Override
    public GraphData getCompanyGraphById(Long id, Integer nodeLimit) {

        Company company = this.getCompanyWithAllRelationshipById(id);
        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        nodes.add(company.buildNodeDto());


        // 当前工期控股的公司
        Optional.ofNullable(company.getBeStockedCompany()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> { // 此时添加的是endNode
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 控股当前公司的公司
        Optional.ofNullable(company.getStockHolderCompany()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 控股当前公司的人
        Optional.ofNullable(company.getStockHolderPerson()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 任职到这个公司的人
        Optional.ofNullable(company.getTakeOfficePerson()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 当前公司所属的地区
        Optional.ofNullable(company.getIncludePosition()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                // 此时添加的是startNode
                nodes.add(relation.getStartNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        //当前公司建设的项目(环评)
        Optional.ofNullable(company.getConstructs()).ifPresent(constructs -> {
            constructs = RelationUtils.getRelationLimited(constructs, nodeLimit);
            constructs.forEach(construct -> {
                nodes.add(construct.getEndNode().buildNodeDto());
                relations.add(construct.buildRelationDto());
            });
        });
        //当前公司建设的项目(招标)
        Optional.ofNullable(company.getConstructsForBiddingAnnouncement()).ifPresent(constructs -> {
            constructs = RelationUtils.getRelationLimited(constructs, nodeLimit);
            constructs.forEach(construct -> {
                nodes.add(construct.getEndNode().buildNodeDto());
                relations.add(construct.buildRelationDto());
            });
        });
        //当前公司建设的项目(中标)
        Optional.ofNullable(company.getConstructsForBidWinningAnnouncement()).ifPresent(constructs -> {
            constructs = RelationUtils.getRelationLimited(constructs, nodeLimit);
            constructs.forEach(construct -> {
                nodes.add(construct.getEndNode().buildNodeDto());
                relations.add(construct.buildRelationDto());
            });
        });
        //当前公司评价的项目
        Optional.ofNullable(company.getEvaluates()).ifPresent(evaluates -> {
            evaluates = RelationUtils.getRelationLimited(evaluates, nodeLimit);
            evaluates.forEach(evaluate -> {
                nodes.add(evaluate.getEndNode().buildNodeDto());
                relations.add(evaluate.buildRelationDto());
            });
        });
        //当前公司代理的项目（招标）
        Optional.ofNullable(company.getAgentsForBiddingAnnouncement()).ifPresent(agents -> {
            agents = RelationUtils.getRelationLimited(agents, nodeLimit);
            agents.forEach(agent -> {
                nodes.add(agent.getEndNode().buildNodeDto());
                relations.add(agent.buildRelationDto());
            });
        });
        //当前公司代理的项目（中标）
        Optional.ofNullable(company.getAgentsForBidWinningAnnouncement()).ifPresent(agents -> {
            agents = RelationUtils.getRelationLimited(agents, nodeLimit);
            agents.forEach(agent -> {
                nodes.add(agent.getEndNode().buildNodeDto());
                relations.add(agent.buildRelationDto());
            });
        });
        // 当前公司中标的项目
        Optional.ofNullable(company.getWinBidsForBidWinningAnnouncement()).ifPresent(winBids -> {
            winBids = RelationUtils.getRelationLimited(winBids, nodeLimit);
            winBids.forEach(winBid -> {
                nodes.add(winBid.getEndNode().buildNodeDto());
                relations.add((winBid.buildRelationDto()));
            });
        });

        // 当前公司的子公司
        Optional.ofNullable(company.getChildCompanys()).ifPresent(childCompanys -> {
            childCompanys = RelationUtils.getRelationLimited(childCompanys, nodeLimit);
            childCompanys.forEach(childCompany -> {
                nodes.add(childCompany.getEndNode().buildNodeDto());
                relations.add((childCompany.buildRelationDto()));
            });
        });
        // 当前公司的父公司
        Optional.ofNullable(company.getChildCompanys()).ifPresent(parentCompanys -> {
            parentCompanys = RelationUtils.getRelationLimited(parentCompanys, nodeLimit);
            parentCompanys.forEach(parentCompany -> {
                nodes.add(parentCompany.getStartNode().buildNodeDto());
                relations.add((parentCompany.buildRelationDto()));
            });
        });

        return new GraphData(nodes,relations);
    }

    @Override
    public List<RelationTableData> getCompanyRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Company company = this.getCompanyWithAllRelationshipById(id);

        // 控股当前公司的公司
        RelationTableData<Company> stockHolderCompany = new RelationTableData(Relationship.INCOMING, RelationConstant.STOCKHOLDER, EntityConstant.COMPANY);
        Optional.ofNullable(company.getStockHolderCompany()).ifPresent(relations -> {
            relations.forEach(relation -> {
                stockHolderCompany.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(stockHolderCompany);

        // 当前公司的控股的公司
        RelationTableData<Company> beStockedCompany = new RelationTableData(Relationship.OUTGOING, RelationConstant.STOCKHOLDER, EntityConstant.COMPANY);
        Optional.ofNullable(company.getBeStockedCompany()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beStockedCompany.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(beStockedCompany);

        // 控股当前公司的人
        RelationTableData<Person> stockHolderPerson = new RelationTableData(Relationship.INCOMING, RelationConstant.STOCKHOLDER, EntityConstant.PERSON);
        Optional.ofNullable(company.getStockHolderPerson()).ifPresent(relations -> {
            relations.forEach(relation -> {
                stockHolderPerson.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(stockHolderPerson);

        // 任职到这个公司的人
        RelationTableData<Person> takeOfficePerson = new RelationTableData(Relationship.INCOMING, RelationConstant.TAKE_OFFICE, EntityConstant.PERSON);
        Optional.ofNullable(company.getTakeOfficePerson()).ifPresent(relations -> {
            relations.forEach(relation -> {
                takeOfficePerson.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(takeOfficePerson);

        // 当前公司所属的地区
        RelationTableData<Position> includePosition = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.POSITION);
        Optional.ofNullable(company.getIncludePosition()).ifPresent(relations -> {
            relations.forEach(relation -> {
                includePosition.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(includePosition);

        // 当前公司评价的项目
        RelationTableData<EnvAssessmentAnnouncement> evaluates = new RelationTableData(Relationship.OUTGOING, RelationConstant.EVALUATE, EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT);
        Optional.ofNullable(company.getEvaluates()).ifPresent(relations -> {
            relations.forEach(relation -> {
                evaluates.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(evaluates);

        // 当前公司建设的项目（环评）
        RelationTableData<EnvAssessmentAnnouncement> constructs = new RelationTableData(Relationship.OUTGOING, RelationConstant.CONSTRUCT, EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT);
        Optional.ofNullable(company.getConstructs()).ifPresent(relations -> {
            relations.forEach(relation -> {
                constructs.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(constructs);

        // 当前公司建设的项目(招标)
        RelationTableData<BiddingAnnouncement> constructsForBiddingAnnouncement = new RelationTableData(Relationship.OUTGOING, RelationConstant.CONSTRUCT, EntityConstant.BIDDING_ANNOUNCEMENT);
        Optional.ofNullable(company.getConstructsForBiddingAnnouncement()).ifPresent(relations -> {
            relations.forEach(relation -> {
                constructsForBiddingAnnouncement.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(constructsForBiddingAnnouncement);

        // 当前公司代理的项目招标（招标）
        RelationTableData<BiddingAnnouncement> agentsForBiddingAnnouncement = new RelationTableData(Relationship.OUTGOING, RelationConstant.AGENT, EntityConstant.BIDDING_ANNOUNCEMENT);
        Optional.ofNullable(company.getAgentsForBiddingAnnouncement()).ifPresent(relations -> {
            relations.forEach(relation -> {
                agentsForBiddingAnnouncement.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(agentsForBiddingAnnouncement);

        // 当前公司建设的项目（中标）
        RelationTableData<BidWinningAnnouncement> constructsForBidWinningAnnouncement = new RelationTableData(Relationship.OUTGOING, RelationConstant.CONSTRUCT, EntityConstant.BID_WINNING_ANNOUNCEMENT);
        Optional.ofNullable(company.getConstructsForBidWinningAnnouncement()).ifPresent(relations -> {
            relations.forEach(relation -> {
                constructsForBidWinningAnnouncement.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(constructsForBidWinningAnnouncement);

        // 当前公司代理的项目（中标）
        RelationTableData<BidWinningAnnouncement> agentsForBidWinningAnnouncement = new RelationTableData(Relationship.OUTGOING, RelationConstant.AGENT, EntityConstant.BID_WINNING_ANNOUNCEMENT);
        Optional.ofNullable(company.getAgentsForBidWinningAnnouncement()).ifPresent(relations -> {
            relations.forEach(relation -> {
                agentsForBidWinningAnnouncement.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(agentsForBidWinningAnnouncement);

        // 当前公司中标的项目
        RelationTableData<BidWinningAnnouncement> winBidsForBidWinningAnnouncement = new RelationTableData(Relationship.OUTGOING, RelationConstant.WINBID, EntityConstant.BID_WINNING_ANNOUNCEMENT);
        Optional.ofNullable(company.getWinBidsForBidWinningAnnouncement()).ifPresent(relations -> {
            relations.forEach(relation -> {
                winBidsForBidWinningAnnouncement.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(winBidsForBidWinningAnnouncement);

        // 当前公司的子公司
        RelationTableData<Company> childCompany = new RelationTableData(Relationship.OUTGOING, RelationConstant.INCLUDE, EntityConstant.COMPANY);
        Optional.ofNullable(company.getChildCompanys()).ifPresent(relations -> {
            relations.forEach(relation -> {
                childCompany.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(childCompany);

        // 当前公司的父公司
        RelationTableData<Company> parentCompany = new RelationTableData(Relationship.INCOMING, RelationConstant.INCLUDE, EntityConstant.COMPANY);
        Optional.ofNullable(company.getParentCompanys()).ifPresent(relations -> {
            relations.forEach(relation -> {
                parentCompany.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(parentCompany);


        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());

        return relationTableDatas;
    }

    @Override
    public Company getCompanyInfoById(Long id) {
        List<Company> companyList = Option.of(companyDao.findCompanyById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return companyList.get(0);
    }

    @Override
    public PageData<Company> getCompanyPage(PageRequest pageRequest) {
        return new PageData<>(companyDao.findAll(pageRequest));
    }

    @Override
    public PageData<Company> getCompanyPageByFuzzySearch(Company company, PageRequest pageRequest) {
        return new PageData<>(companyDao.findCompaniesByFuzzy(company.getName(), pageRequest));
    }

    @Override
    public Long getCount() {
        return companyDao.getCount();
    }

    @Override
    public void add(Company company) {
        try {
            if (company.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            companyDao.save(company);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public void update(Company company) {
        try {
            if (company.getId() == null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            companyDao.save(company);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            companyDao.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_DELETE_FAILURE);
        }
    }

    @Override
    @Transactional(rollbackFor = KgException.class)
    public void mergeNode(Long masterNodeId, Long slaverNodeId) {
        Company masterNode = this.getCompanyWithAllRelationshipById(masterNodeId);
        Company slaverNode = this.getCompanyWithAllRelationshipById(slaverNodeId);

        // 融合节点属性
        if("".equals(masterNode.getName())) {
            masterNode.setName(slaverNode.getName());
        }
        if("".equals(masterNode.getEnglishName())) {
            masterNode.setEnglishName(slaverNode.getEnglishName());
        }
        if("".equals(masterNode.getAddress())) {
            masterNode.setAddress(slaverNode.getAddress());
        }
        if("".equals(masterNode.getOtherName())) {
            masterNode.setOtherName(slaverNode.getOtherName());
        }
        if("".equals(masterNode.getUscc())) {
            masterNode.setUscc(slaverNode.getUscc());
        }
        if("".equals(masterNode.getEmail())) {
            masterNode.setEmail(slaverNode.getEmail());
        }
        if("".equals(masterNode.getUrl())) {
            masterNode.setUrl(slaverNode.getUrl());
        }
        if("".equals(masterNode.getBusinessScope())) {
            masterNode.setBusinessScope(slaverNode.getBusinessScope());
        }
        if("".equals(masterNode.getProductName())) {
            masterNode.setProductName(slaverNode.getProductName());
        }
        if("".equals(masterNode.getBusinessForm())) {
            masterNode.setBusinessForm(slaverNode.getBusinessForm());
        }
        if("".equals(masterNode.getBornDate())) {
            masterNode.setBornDate(slaverNode.getBornDate());
        }
        if("".equals(masterNode.getIndustry())) {
            masterNode.setIndustry(slaverNode.getIndustry());
        }
        if("".equals(masterNode.getIndustryCode())) {
            masterNode.setIndustryCode(slaverNode.getIndustryCode());
        }
        if("".equals(masterNode.getOrganizationCode())) {
            masterNode.setOrganizationCode(slaverNode.getOrganizationCode());
        }
        if("".equals(masterNode.getRegisterFund())) {
            masterNode.setRegisterFund(slaverNode.getRegisterFund());
        }
        if("".equals(masterNode.getPhoneNumber())) {
            masterNode.setPhoneNumber(slaverNode.getPhoneNumber());
        }
        if("".equals(masterNode.getFax())) {
            masterNode.setFax(slaverNode.getFax());
        }
        if("".equals(masterNode.getPostcode())) {
            masterNode.setPostcode(slaverNode.getPostcode());
        }
        if("".equals(masterNode.getIntroduction())) {
            masterNode.setIntroduction(slaverNode.getIntroduction());
        }

        this.update(masterNode);

        // 移动关系
        // 将 slaverNode 上的关系都移动到 masterNode 上
        Optional.ofNullable(slaverNode.getStockHolderCompany()).ifPresent(stockholderRelations -> {
            stockholderRelations.forEach(stockholderRelation -> {
                stockholderRelationService.moveTo(stockholderRelation.getStartNode().id, masterNode.id, stockholderRelation);
            });
        });
        Optional.ofNullable(slaverNode.getBeStockedCompany()).ifPresent(stockholderRelations -> {
            stockholderRelations.forEach(stockholderRelation -> {
                stockholderRelationService.moveTo(masterNode.id, stockholderRelation.getEndNode().id, stockholderRelation);
            });
        });
        Optional.ofNullable(slaverNode.getStockHolderPerson()).ifPresent(stockholderRelations -> {
            stockholderRelations.forEach(stockholderRelation -> {
                stockholderRelationService.moveTo(stockholderRelation.getStartNode().id, masterNode.id, stockholderRelation);
            });
        });
        Optional.ofNullable(slaverNode.getTakeOfficePerson()).ifPresent(takeOfficeRelations -> {
            takeOfficeRelations.forEach(takeOfficeRelation -> {
                takeOfficeRelationService.moveTo(takeOfficeRelation.getStartNode().id, masterNode.id, takeOfficeRelation);
            });
        });
        Optional.ofNullable(slaverNode.getIncludePosition()).ifPresent(includeRelations -> {
            includeRelations.forEach(includeRelation -> {
                includeRelationService.moveTo(includeRelation.getStartNode().id, masterNode.id, includeRelation);
            });
        });
        Optional.ofNullable(slaverNode.getEvaluates()).ifPresent(evaluates -> {
            evaluates.forEach(evaluate -> {
                evaluateService.moveTo(masterNode.id, evaluate.getEndNode().id, evaluate);
            });
        });
        Optional.ofNullable(slaverNode.getConstructs()).ifPresent(constructs -> {
            constructs.forEach(construct -> {
                constructService.moveTo(masterNode.id, construct.getEndNode().id, construct);
            });
        });
        Optional.ofNullable(slaverNode.getConstructsForBiddingAnnouncement()).ifPresent(constructs -> {
            constructs.forEach(construct -> {
                constructService.moveTo(masterNode.id, construct.getEndNode().id, construct);
            });
        });
        Optional.ofNullable(slaverNode.getAgentsForBiddingAnnouncement()).ifPresent(agents -> {
            agents.forEach(agent -> {
                agentService.moveTo(masterNode.id, agent.getEndNode().id, agent);
            });
        });
        Optional.ofNullable(slaverNode.getConstructsForBidWinningAnnouncement()).ifPresent(constructs -> {
            constructs.forEach(construct -> {
                constructService.moveTo(masterNode.id, construct.getEndNode().id, construct);
            });
        });
        Optional.ofNullable(slaverNode.getAgentsForBidWinningAnnouncement()).ifPresent(agents -> {
            agents.forEach(agent -> {
                agentService.moveTo(masterNode.id, agent.getEndNode().id, agent);
            });
        });
        Optional.ofNullable(slaverNode.getWinBidsForBidWinningAnnouncement()).ifPresent(winBids -> {
            winBids.forEach(winBid -> {
                winBidService.moveTo(masterNode.id, winBid.getEndNode().id, winBid);
            });
        });
        Optional.ofNullable(slaverNode.getChildCompanys()).ifPresent(childCompanys ->{
            childCompanys.forEach(childCompany -> {
                includeRelationService.moveTo(masterNode.id, childCompany.getEndNode().id, childCompany);
            });
        });
        Optional.ofNullable(slaverNode.getParentCompanys()).ifPresent(parentCompanys ->{
            parentCompanys.forEach(parentCompany -> {
                includeRelationService.moveTo(parentCompany.getStartNode().id, masterNode.id, parentCompany);
            });
        });

        // 删除 slaverNode
        this.deleteById(slaverNode.id);

    }

    public Company getCompanyWithAllRelationshipById(Long id) {
        Company company = Option.of(companyDao.findCompanyById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);

        company.setIncludePosition(includeRelationDao.findIncludePositionByCompany(company));
        company.setParentCompanys(includeRelationDao.findparentCompanysBychildCompany(company));

        company.setStockHolderCompany(stockholderRelationDao.findStockHolderCompanyByCompany(company));
        company.setStockHolderPerson(stockholderRelationDao.findStockHolderPersonByCompany(company));

        company.setConstructs(constructDao.findConstructsByCompany(company));
        company.setConstructsForBiddingAnnouncement(constructDao.findConstructsForBiddingAnnouncementByCompany(company));
        company.setConstructsForBidWinningAnnouncement(constructDao.findconstructsForBidWinningAnnouncementByCompany(company));

        company.setAgentsForBiddingAnnouncement(agentDao.findAgentsForBiddingAnnouncementByCompany(company));
        company.setAgentsForBidWinningAnnouncement(agentDao.findAgentsForBidWinningAnnouncementByCompany(company));

        return company;
    }
}
