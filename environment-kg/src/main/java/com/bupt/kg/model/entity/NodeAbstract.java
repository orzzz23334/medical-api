package com.bupt.kg.model.entity;

import com.bupt.kg.model.dto.transferable.NodeTransferable;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

@Data
public abstract class NodeAbstract implements NodeTransferable {
    @Id
    @GeneratedValue
    public Long id;
}
