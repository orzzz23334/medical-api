package com.bupt.kg.service.entity;

import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.model.entity.Position;
import com.bupt.kg.model.vo.RelationTableData;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PositionService {
    GraphData getPositionGraphById(Long id, Integer nodeLimit);

    List<RelationTableData> getPositionRelationTablesById(Long id);

    Position getPositionInfoById(Long id);

    PageData<Position> getPositionPage(PageRequest pageRequest);

    PageData<Position> getPositionPageByFuzzySearch(Position position, PageRequest pageRequest);

    Long getCount();

    void add(Position position);

    void update(Position position);

    void deleteById(Long id);

    Position getPositionWithAllRelationshipById(Long id);
}
