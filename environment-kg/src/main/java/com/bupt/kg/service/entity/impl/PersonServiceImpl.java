package com.bupt.kg.service.entity.impl;


import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.relation.TakeOfficeRelationDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.entity.PersonDto;
import com.bupt.kg.model.entity.*;
import com.bupt.kg.model.relation.TakeOfficeRelation;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.dao.entity.PersonDao;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.service.entity.PersonService;
import com.bupt.kg.service.relation.*;
import com.bupt.kg.utils.RelationUtils;
import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonDao personDao;
    @Autowired
    private TakeOfficeRelationDao takeOfficeRelationDao;
    @Autowired
    private StockholderRelationService stockholderRelationService;
    @Autowired
    private TakeOfficeRelationService takeOfficeRelationService;
    @Autowired
    private EvaluateBidService evaluateBidService;
    @Autowired
    private IsFriendOfService isFriendOfService;
    @Autowired
    private AttendRelationService attendRelationService;

    @Override
    public List<Person> getPersonByName(String name) {
        return personDao.findPersonByName(name, 1);
    }

    @Override
    public GraphData getPersonGraphById(Long id, Integer nodeLimit) {
        Person person = this.getPersonWithAllRelationshipById(id);
        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        nodes.add(person.buildNodeDto());

        // 添加就职公司
        Optional.ofNullable(person.getTakeOfficeInCompany()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        //添加就职的政府
        Optional.ofNullable(person.getTakeOfficeInGovernment()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加控股公司
        Optional.ofNullable(person.getStockHolderCompany()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
        // 添加就读的学校
        Optional.ofNullable(person.getAttendSchool()).ifPresent(x-> {
            x = RelationUtils.getRelationLimited(x, nodeLimit);
            x.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });
//        // 添加我的朋友是谁
//        Optional.ofNullable(person.getMyFriends()).ifPresent(myFriends ->{
//                myFriends.forEach(myFriend -> {
//                    nodes.add(myFriend.endNode.buildNodeDto());
//                    relations.add(myFriend.buildRelationDto());
//                });
//        });
//        // 添加我是谁的朋友
//        Optional.ofNullable(person.getBeFriedOf()).ifPresent(x ->{
//                x.forEach(beFriendOf -> {
//                    nodes.add(beFriendOf.startNode.buildNodeDto());
//                    relations.add(beFriendOf.buildRelationDto());
//                });
//        });
        // 添加评标的项目
        Optional.ofNullable(person.getEvaluatedBidWinningAnnouncements()).ifPresent(evaluateBids -> {
            evaluateBids = RelationUtils.getRelationLimited(evaluateBids, nodeLimit);
            evaluateBids.forEach(evaluateBid -> {
                nodes.add(evaluateBid.getEndNode().buildNodeDto());
                relations.add(evaluateBid.buildRelationDto());
            });
        });


        return new GraphData(nodes,relations);
    }

    @Override
    public List<RelationTableData> getPersonRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Person person = this.getPersonWithAllRelationshipById(id);

        // 就职单位
        RelationTableData<Company> takeOfficeInCompany = new RelationTableData(Relationship.OUTGOING, RelationConstant.TAKE_OFFICE, EntityConstant.COMPANY);
        Optional.ofNullable(person.getTakeOfficeInCompany()).ifPresent(relations -> {
            relations.forEach(relation -> {
                takeOfficeInCompany.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(takeOfficeInCompany);

        // 政府的任职关系
        RelationTableData<Government> takeOfficeInGovernment = new RelationTableData(Relationship.OUTGOING, RelationConstant.TAKE_OFFICE, EntityConstant.GOVERNMENT);
        Optional.ofNullable(person.getTakeOfficeInGovernment()).ifPresent(relations -> {
            relations.forEach(relation -> {
                takeOfficeInGovernment.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(takeOfficeInGovernment);

        // 股东关系
        RelationTableData<Company> stockHolderCompany = new RelationTableData(Relationship.OUTGOING, RelationConstant.STOCKHOLDER, EntityConstant.COMPANY);
        Optional.ofNullable(person.getStockHolderCompany()).ifPresent(relations -> {
            relations.forEach(relation -> {
                stockHolderCompany.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(stockHolderCompany);

        // 就读关系
        RelationTableData<School> attendSchool = new RelationTableData(Relationship.OUTGOING, RelationConstant.ATTEND, EntityConstant.SCHOOL);
        Optional.ofNullable(person.getAttendSchool()).ifPresent(relations -> {
            relations.forEach(relation -> {
                attendSchool.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(attendSchool);

        // 评标关系
        RelationTableData<BidWinningAnnouncement> EvaluatedBidWinningAnnouncements = new RelationTableData(Relationship.OUTGOING, RelationConstant.EVALUATE_BID, EntityConstant.BID_WINNING_ANNOUNCEMENT);
        Optional.ofNullable(person.getEvaluatedBidWinningAnnouncements()).ifPresent(relations -> {
            relations.forEach(relation -> {
                EvaluatedBidWinningAnnouncements.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(EvaluatedBidWinningAnnouncements);

        // 朋友关系
        RelationTableData<Person> myFriends = new RelationTableData(Relationship.OUTGOING, RelationConstant.IS_FRIEND_OF, EntityConstant.PERSON);
        Optional.ofNullable(person.getMyFriends()).ifPresent(relations -> {
            relations.forEach(relation -> {
                myFriends.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(myFriends);

        RelationTableData<Person> beFriedOf = new RelationTableData(Relationship.INCOMING, RelationConstant.IS_FRIEND_OF, EntityConstant.PERSON);
        Optional.ofNullable(person.getBeFriedOf()).ifPresent(relations -> {
            relations.forEach(relation -> {
                beFriedOf.addNode(relation.getStartNode());
            });
        });
        relationTableDatas.add(beFriedOf);
        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());
        return relationTableDatas;
    }

    @Override
    public Person getPersonInfoById(Long id) {
        List<Person> personList = Option.of(personDao.findPersonById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return personList.get(0);
    }

    @Override
    public PageData<Person> getPersonPage(PageRequest pageRequest) {
        return new PageData<>(personDao.findAll(pageRequest));
    }

    @Override
    public PageData<PersonDto> getPersonPageByFuzzySearch(Person person, PageRequest pageRequest, Company companySearch) {
        Page<Person> page = null;
        if (companySearch.getName().equals("")) {// 如果没给我公司名
            page = personDao.findPeopleByFuzzyPersonName(person.getName(), pageRequest);
        }
        else { // 给我一部分公司名
            page = personDao.findPeopleBycompanyName(person.getName(), pageRequest, companySearch.getName());
        }
        List<PersonDto> personDtos = new ArrayList<>();
        page.getContent().forEach(pagePerson -> {
            pagePerson = getPersonWithAllRelationshipById(pagePerson.id);
            TakeOfficeRelation takeOfficeRelation = null;
            Company company = null;
            Government government = null;
            if (pagePerson.getTakeOfficeInGovernment() != null && pagePerson.getTakeOfficeInGovernment().size() > 0) {
                takeOfficeRelation = pagePerson.getTakeOfficeInGovernment().get(0);
                government = (Government) takeOfficeRelation.getEndNode();
                personDtos.add(new PersonDto(pagePerson, government.getName(), takeOfficeRelation.getName()));
            }else if (pagePerson.getTakeOfficeInCompany() != null && pagePerson.getTakeOfficeInCompany().size() > 0) {
                takeOfficeRelation = pagePerson.getTakeOfficeInCompany().get(0);
                company = (Company) takeOfficeRelation.getEndNode();
                personDtos.add(new PersonDto(pagePerson, company.getName(), takeOfficeRelation.getName()));
            }else {
                personDtos.add(new PersonDto(pagePerson, "", ""));
            }
        });
        Page<PersonDto> pagePersonDtos = new PageImpl<>(personDtos);
        PageData<PersonDto> personPageData = new PageData<>(pagePersonDtos);
        personPageData.setTotalPages(page.getTotalPages());
        personPageData.setPageSize(page.getSize());
        personPageData.setContentSize(page.getNumberOfElements());
        personPageData.setPageNum(page.getNumber() + 1);
        personPageData.setTotalElements(page.getTotalElements());
        return personPageData;

    }

    @Override
    public Long getCount() {
        return personDao.getCount();
    }

    @Override
    public void add(Person person) {
        try {
            if (person.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            personDao.save(person);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public void update(Person person) {
        try {
            if (person.getId() == null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            personDao.save(person);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            personDao.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_DELETE_FAILURE);
        }
    }

    @Override
    @Transactional(rollbackFor = KgException.class)
    public void mergeNode(Long masterNodeId, Long slaverNodeId) {
        Person masterNode = this.getPersonWithAllRelationshipById(masterNodeId);
        Person slaverNode = this.getPersonWithAllRelationshipById(slaverNodeId);

        // 融合节点属性
        if("".equals(masterNode.getName())) {
            masterNode.setName(slaverNode.getName());
        }
        if("".equals(masterNode.getGender())) {
            masterNode.setGender(slaverNode.getGender());
        }
        if("".equals(masterNode.getNation())) {
            masterNode.setNation(slaverNode.getNation());
        }
        if("".equals(masterNode.getNativePlace())) {
            masterNode.setNativePlace(slaverNode.getNativePlace());
        }
        if("".equals(masterNode.getBornDate())) {
            masterNode.setBornDate(slaverNode.getBornDate());
        }
        if("".equals(masterNode.getPoliticsStatus())) {
            masterNode.setPoliticsStatus(slaverNode.getPoliticsStatus());
        }
        if("".equals(masterNode.getHighestEdu())) {
            masterNode.setHighestEdu(slaverNode.getHighestEdu());
        }if("".equals(masterNode.getUrl())) {
            masterNode.setUrl(slaverNode.getUrl());
        }
        if("".equals(masterNode.getResume())) {
            masterNode.setResume(slaverNode.getResume());
        }

        this.update(masterNode);

        // 移动关系
        // 将 slaverNode 上的关系都移动到 masterNode 上
        Optional.ofNullable(slaverNode.getTakeOfficeInCompany()).ifPresent(takeOfficeRelations -> {
            takeOfficeRelations.forEach(takeOfficeRelation -> {
                takeOfficeRelationService.moveTo(masterNode.id, takeOfficeRelation.getEndNode().id, takeOfficeRelation);
            });
        });
        Optional.ofNullable(slaverNode.getTakeOfficeInGovernment()).ifPresent(takeOfficeRelations -> {
            takeOfficeRelations.forEach(takeOfficeRelation -> {
                takeOfficeRelationService.moveTo(masterNode.id, takeOfficeRelation.getEndNode().id, takeOfficeRelation);
            });
        });
        Optional.ofNullable(slaverNode.getStockHolderCompany()).ifPresent(stockholderRelations -> {
            stockholderRelations.forEach(stockholderRelation -> {
                stockholderRelationService.moveTo(masterNode.id, stockholderRelation.getEndNode().id, stockholderRelation);
            });
        });
        Optional.ofNullable(slaverNode.getAttendSchool()).ifPresent(attendRelations -> {
            attendRelations.forEach(attendRelation -> {
                attendRelationService.moveTo(masterNode.id, attendRelation.getEndNode().id, attendRelation);
            });
        });
        Optional.ofNullable(slaverNode.getEvaluatedBidWinningAnnouncements()).ifPresent(evaluateBids -> {
            evaluateBids.forEach(evaluateBid -> {
                evaluateBidService.moveTo(masterNode.id, evaluateBid.getEndNode().id, evaluateBid);
            });
        });
        Optional.ofNullable(slaverNode.getMyFriends()).ifPresent(isFriendOfs -> {
            isFriendOfs.forEach(isFriendOf -> {
                isFriendOfService.moveTo(masterNode.id, isFriendOf.getEndNode().id, isFriendOf);
            });
        });
        Optional.ofNullable(slaverNode.getBeFriedOf()).ifPresent(isFriendOfs -> {
            isFriendOfs.forEach(isFriendOf -> {
                isFriendOfService.moveTo(isFriendOf.getStartNode().id, masterNode.id, isFriendOf);
            });
        });

        // 删除 slaverNode
        this.deleteById(slaverNode.id);

    }

    public Person getPersonWithAllRelationshipById(Long id) {
        Person person = Option.of(personDao.findPersonById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        person.setTakeOfficeInCompany(takeOfficeRelationDao.findTakeOfficeInCompanyByPerson(person));
        person.setTakeOfficeInGovernment(takeOfficeRelationDao.findTakeOfficeInGovernmentByPerson(person));

        return person;
    }

    @Override
    @Async
    public void autoMergePerson() {
        // 首先获取所有的重名名单
        List<String> personNames = personDao.findPersonNamesByhasSamePersonName();

        Set<Long> deletedIds = new HashSet<>(); // 记录哪些节点已经被删除了

        for (String personName : personNames) {
            List<Person> persons = personDao.findPersonByName(personName, 0);

            for (int i=0; i<persons.size(); i++){
                Person master = persons.get(i);
                if (deletedIds.contains(master.getId())) { // 此主节点已经被删除
                    continue; // 下一个主节点
                }
                for (int j=i+1; j<persons.size(); j++) {
                    Person slaver = persons.get(j);
                    if (deletedIds.contains(slaver.getId())) { // 此从节点已经被删除
                        continue; // 切换下一个从节点
                    }

                    if (this.isSamePerson(master, slaver)) {
                        log.info("auto merge" + master.getId() + "-" + slaver.getId() + "!!!");
                        this.mergeNode(master.getId(), slaver.getId());
                        deletedIds.add(slaver.getId());
                    }
                }
            }

            deletedIds.clear(); // 清空删除记录
        }

    }

    @Override
    public boolean isSamePerson(Person master, Person slaver) {
        boolean isSame = false; // 默认不是一个人
        if (!"".equals(master.getResume()) && !"".equals(slaver.getResume())){ // 节点存在简历信息的情况下
            if (master.getName().equals(slaver.getName()) && master.getResume().equals(slaver.getResume())) { // 简历和名字相等
                isSame = true; // 则认为是同一个人
            }
        }
        return isSame;
    }
}
