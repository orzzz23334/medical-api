package com.bupt.kg.controller;

import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.model.vo.GraphData;
import com.bupt.kg.common.response.ResponseData;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.vo.RelationTableData;
import com.bupt.kg.service.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "kg graph接口")
@RestController
@EnableAsync
@RequestMapping("/kg/graph")
public class GraphController {
    @Autowired
    private PersonService personService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private GovernmentService governmentService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private EnvAssessmentAnnouncementService envAssessmentAnnouncementService;
    @Autowired
    private BiddingAnnouncementService biddingAnnouncementService;
    @Autowired
    private BidService bidService;
    @Autowired
    private BidWinningAnnouncementService bidWinningAnnouncementService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private MedicalEquipmentService medicalEquipmentService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ProjectService projectService;

    @ApiOperation(value="根据id查找实体及其关系",response = ResponseData.class)
    @GetMapping("/{entity}/{id}")
    public ResponseData getEntityById(@ApiParam("实体类型") @PathVariable("entity") String entity,
                                      @ApiParam("实体的id") @PathVariable("id") Long id,
                                      @ApiParam("最大节点数") @RequestParam(value = "nodeLimit", required = false, defaultValue = "10") Integer nodeLimit){
        GraphData graphData = null;
        switch (entity) {
            case EntityConstant.COMPANY:
                graphData = companyService.getCompanyGraphById(id, nodeLimit);
                break;
            case EntityConstant.GOVERNMENT:
                graphData = governmentService.getGovernmentGraphById(id, nodeLimit);
                break;
            case EntityConstant.PERSON:
                graphData = personService.getPersonGraphById(id, nodeLimit);
                break;
            case EntityConstant.POSITION:
                graphData = positionService.getPositionGraphById(id, nodeLimit);
                break;
            case EntityConstant.SCHOOL:
                graphData = schoolService.getSchoolGraphById(id, nodeLimit);
                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:
                graphData = envAssessmentAnnouncementService.getEnvAssessmentAnnouncementGraphById(id, nodeLimit);
                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:
                graphData = biddingAnnouncementService.getBiddingAnnouncementGraphById(id, nodeLimit);
                break;
            case EntityConstant.BID:
                graphData = bidService.getBidGraphById(id, nodeLimit);
                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:
                graphData = bidWinningAnnouncementService.getBidWinningAnnouncementGraphById(id, nodeLimit);
                break;
            case EntityConstant.SUPPLIER:
                graphData = supplierService.getSupplierGraphById(id, nodeLimit);
                break;
            case EntityConstant.HOSPITAL:
                graphData = hospitalService.getHospitalGraphById(id, nodeLimit);
                break;
            case EntityConstant.MEDICAL_EQUIPMENT:
                graphData = medicalEquipmentService.getMedicalEquipmentGraphById(id, nodeLimit);
                break;
            case EntityConstant.PACKAGE:
                graphData = packageService.getPackageGraphById(id, nodeLimit);
                break;
            case EntityConstant.PROJECT:
                graphData = projectService.getProjectGraphById(id, nodeLimit);
                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK,graphData);
    }

    @ApiOperation(value = "融合这两个节点", response = ResponseData.class)
    @PutMapping("/{entityType}/{masterNodeId}/{slaverNodeId}")
    public ResponseData mergeTwoNode(@PathVariable("entityType") String entity,
                                     @PathVariable("masterNodeId") Long masterNodeId,
                                     @PathVariable("slaverNodeId") Long slaverNodeId) {
        switch (entity) {
            case EntityConstant.COMPANY:
                companyService.mergeNode(masterNodeId, slaverNodeId);
                break;
            case EntityConstant.GOVERNMENT:
                governmentService.mergeNode(masterNodeId, slaverNodeId);
                break;
            case EntityConstant.PERSON:
                personService.mergeNode(masterNodeId, slaverNodeId);
                break;
            case EntityConstant.POSITION:

                break;
            case EntityConstant.SCHOOL:

                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:

                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:

                break;
            case EntityConstant.BID:
                bidService.mergeNode(masterNodeId, slaverNodeId);
                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:

                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK);
    }

    @ApiOperation(value="根据id查找其关系及节点",response = ResponseData.class)
    @GetMapping("/relation/table/{entity}/{id}")
    public ResponseData getRelationTablesById(@ApiParam("实体类型") @PathVariable("entity") String entity,
                                      @ApiParam("实体的id") @PathVariable("id") Long id){
        List<RelationTableData> relationTables = null;
        switch (entity) {
            case EntityConstant.COMPANY:
                relationTables = companyService.getCompanyRelationTablesById(id);
                break;
            case EntityConstant.GOVERNMENT:
                relationTables = governmentService.getGovernmentRelationTablesById(id);
                break;
            case EntityConstant.PERSON:
                relationTables = personService.getPersonRelationTablesById(id);
                break;
            case EntityConstant.POSITION:
                relationTables = positionService.getPositionRelationTablesById(id);
                break;
            case EntityConstant.SCHOOL:
                relationTables = schoolService.getSchoolRelationTablesById(id);
                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:
                relationTables = envAssessmentAnnouncementService.getEnvAssessmentAnnouncementRelationTablesById(id);
                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:
                relationTables = biddingAnnouncementService.getBiddingAnnouncementRelationTablesById(id);
                break;
            case EntityConstant.BID:
                relationTables = bidService.getBidRelationTablesById(id);
                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:
                relationTables = bidWinningAnnouncementService.getBidWinningAnnouncementRelationTablesById(id);
                break;
            case EntityConstant.SUPPLIER:
                relationTables = supplierService.getSupplierRelationTablesById(id);
                break;
            case EntityConstant.HOSPITAL:
                relationTables = hospitalService.getHospitalRelationTablesById(id);
                break;
            case EntityConstant.MEDICAL_EQUIPMENT:
                relationTables = medicalEquipmentService.getMedicalEquipmentRelationTablesById(id);
                break;
            case EntityConstant.PACKAGE:
                relationTables = packageService.getPackageRelationTablesById(id);
                break;
            case EntityConstant.PROJECT:
                relationTables = projectService.getProjectRelationTablesById(id);
                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK, relationTables);
    }
    @ApiOperation(value = "异步合并自动识别的一样的实体")
    @PostMapping("/auto/{entity}/merge")
    public ResponseData autoMerge(@PathVariable("entity") String entity){
        switch (entity) {
            case EntityConstant.COMPANY:

                break;
            case EntityConstant.GOVERNMENT:

                break;
            case EntityConstant.PERSON:
                personService.autoMergePerson();
                break;
            case EntityConstant.POSITION:

                break;
            case EntityConstant.SCHOOL:

                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:

                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:

                break;
            case EntityConstant.BID:

                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:

                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK);
    }
}
