package com.bupt.kg.model.dto;

import com.bupt.kg.model.relation.RelationAbstract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data @NoArgsConstructor @AllArgsConstructor
/**
 * 封装一个关系
 *
 * 暴露关系的id、类型和 起始节点id
 */
public class RelationDto<T extends RelationAbstract> {
    private Long id;
    private String type;
    private Long source;
    private Long target;

    private T properties;

    public RelationDto(String type, Long source, Long target, T properties){
        this.id = properties.getId();
        this.type = type;
        this.source = source;
        this.target = target;
        this.properties = properties;
    }

}
