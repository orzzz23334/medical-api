package com.bupt.kg.model.dto;

import com.bupt.kg.model.entity.NodeAbstract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 *  封装一个节点
 *  暴露 id 和 label
 */
public class NodeDto<T extends NodeAbstract> {
    private Long id;
    private String label;
    private T properties;

    public NodeDto(String label, T properties){
        this.id = properties.getId();
        this.label = label;
        this.properties = properties;
    }
}
