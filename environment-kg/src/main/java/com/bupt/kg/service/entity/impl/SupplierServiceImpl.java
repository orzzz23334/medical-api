package com.bupt.kg.service.entity.impl;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.dao.entity.SupplierDao;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.dto.NodeDto;
import com.bupt.kg.model.dto.RelationDto;
import com.bupt.kg.model.entity.Bid;
import com.bupt.kg.model.entity.BidWinningAnnouncement;
import com.bupt.kg.model.entity.BiddingAnnouncement;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.utils.RelationUtils;
import com.bupt.kg.model.entity.Supplier;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.service.entity.SupplierService;
import io.vavr.control.Option;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierDao supplierDao;

    @Override
    public Long getCount() {
        return supplierDao.getCount();
    }

    @Override
    public Supplier getSupplierInfoById(Long id){
        List<Supplier> supplierList = Option.of(supplierDao.findSupplierById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST));
        return supplierList.get(0);
    };

    @Override
    public PageData<Supplier> getSupplierPageByFuzzySearch(Supplier supplier, PageRequest pageRequest) {
        return new PageData<>(supplierDao.findSupplierByFuzzy(supplier.getName(), pageRequest));
    }

    @Override
    public void add(Supplier supplier) {
        try {
            if (supplier.getId() != null){
                throw new KgException(ResultCodeEnum.ARGUMENT_NOT_VALID);
            }
            supplierDao.save(supplier);
        } catch (Exception e){
            System.out.println(e);
            throw new KgException(ResultCodeEnum.DB_ADD_FAILURE);
        }
    }

    @Override
    public GraphData getSupplierGraphById(Long id, Integer nodeLimit) {
        Supplier supplier = this.getSupplierWithAllRelationshipById(id);

        Set<NodeDto> nodes = new HashSet<>();
        Set<RelationDto> relations = new HashSet<>();

        // 加入主节点
        nodes.add(supplier.buildNodeDto());

        // 供货的中标实例
        Optional.ofNullable(supplier.getSupplyBid()).ifPresent(list -> {
            list = RelationUtils.getRelationLimited(list, nodeLimit);
            list.forEach(relation -> {
                nodes.add(relation.getEndNode().buildNodeDto());
                relations.add(relation.buildRelationDto());
            });
        });

        return new GraphData(nodes, relations);
    }

    @Override
    public List<RelationTableData> getSupplierRelationTablesById(Long id) {
        List<RelationTableData> relationTableDatas = new ArrayList<>();
        Supplier supplier = this.getSupplierWithAllRelationshipById(id);

        RelationTableData<Bid> list = new RelationTableData(Relationship.OUTGOING, RelationConstant.SUPPLY, EntityConstant.BID);
        Optional.ofNullable(supplier.getSupplyBid()).ifPresent(relations -> {
            relations.forEach(relation -> {
                list.addNode(relation.getEndNode());
            });
        });
        relationTableDatas.add(list);

        relationTableDatas = relationTableDatas.stream().filter(relationTableData -> relationTableData.getNodeList().size() > 0).collect(Collectors.toList());


        return relationTableDatas;
    }

    @Override
    public Supplier getSupplierWithAllRelationshipById(Long id) {
        Supplier supplier = Option.of(supplierDao.findSupplierById(id))
                .getOrElseThrow(() -> new KgException(ResultCodeEnum.ENTITY_NOT_EXIST)).get(0);
        return supplier;
    }
}
