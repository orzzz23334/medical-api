package com.bupt.kg.service.entity;


import com.bupt.kg.model.dto.entity.PersonDto;
import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.entity.Person;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PersonService {
    List<Person> getPersonByName(String name);

    GraphData getPersonGraphById(Long id, Integer nodeLimit);
    List<RelationTableData> getPersonRelationTablesById(Long id);

    Person getPersonInfoById(Long id);

    PageData<Person> getPersonPage(PageRequest pageRequest);
    PageData<PersonDto> getPersonPageByFuzzySearch(Person person, PageRequest pageRequest, Company company);
    Long getCount();

    void add(Person person);

    void update(Person person);

    void deleteById(Long id);

    // 融合两个节点
    void mergeNode(Long masterNodeId, Long slaverNodeId);

    Person getPersonWithAllRelationshipById(Long id);

    void autoMergePerson();
    boolean isSamePerson(Person master, Person slaver);
}
