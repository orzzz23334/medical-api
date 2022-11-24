package com.bupt.kg.model.vo;

import com.bupt.kg.model.entity.NodeAbstract;
import com.bupt.kg.model.relation.RelationAbstract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationTableData<N extends NodeAbstract> {
    private String direction;
    private String relationType;
    private String nodeType;
    private List<N> nodeList;

    public RelationTableData(String direction, String relationType, String nodeType) {
        this.direction = direction;
        this.relationType = relationType;
        this.nodeType = nodeType;
        this.nodeList = new ArrayList<>();
    }

    public void addNode(N node) {
        nodeList.add(node);
    }

}
