package com.bupt.kg.model.vo;

import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 封装Graph Response
 * 分为两个数据域
 *  * 节点数据域
 *  * 关系域
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class GraphData {
    private Set<NodeDto> nodes;
    private Set<RelationDto> relations;
}
