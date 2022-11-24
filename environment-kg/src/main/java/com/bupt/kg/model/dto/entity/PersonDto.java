package com.bupt.kg.model.dto.entity;

import com.bupt.kg.model.entity.Company;
import com.bupt.kg.model.entity.Government;
import com.bupt.kg.model.entity.Person;
import com.bupt.kg.model.relation.TakeOfficeRelation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto extends Person{
    private String officeName;
    private String jobName;

    public PersonDto(Person person, String officeName, String jobName){
        this.setId(person.getId());
        this.setName(person.getName());
        this.setGender(person.getGender());
        this.setNation(person.getNation());
        this.setNativePlace(person.getNativePlace());
        this.setBornDate(person.getBornDate());
        this.setPoliticsStatus(person.getPoliticsStatus());
        this.setHighestEdu(person.getHighestEdu());
        this.setUrl(person.getUrl());
        this.setResume(person.getResume());
        this.setPhoneNum(person.getPhoneNum());
        this.setEmail(person.getEmail());

        this.officeName = officeName;
        this.jobName = jobName;
    }
}
