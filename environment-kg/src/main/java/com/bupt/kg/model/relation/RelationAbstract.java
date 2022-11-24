package com.bupt.kg.model.relation;

import com.bupt.kg.model.dto.transferable.RelationTransferable;
import com.bupt.kg.model.entity.NodeAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.StartNode;

@Data
public abstract class RelationAbstract<S extends NodeAbstract, E extends NodeAbstract> implements RelationTransferable {
    @Id
    @GeneratedValue
    public Long id;

    @StartNode
    @JsonIgnore // 由于父类是return this， this 实际是一个子类对象所以要写JsonIgnore
    @ToString.Exclude // 防止test的时候，调用toString引起的栈溢出问题
    public S startNode;
    @EndNode
    @JsonIgnore
    @ToString.Exclude
    public E endNode;

    public boolean equalsByProperties(RelationAbstract relationAbstract) {return true;}
}
