package com.bupt.kg.controller;

import com.alibaba.fastjson.JSON;
import com.bupt.kg.common.constant.EntityConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.entity.*;
import com.bupt.kg.model.entity.Package;
import com.bupt.kg.model.vo.PageData;
import com.bupt.kg.common.response.ResponseData;
import com.bupt.kg.service.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "kg entity接口")
@RestController
@RequestMapping("/kg/table")
public class EntityController {
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

    @ApiOperation(value="根据id删除实体",response = ResponseData.class)
    @DeleteMapping("/{entity}/{id}")
    public ResponseData deleteEntity(@ApiParam("实体类型") @PathVariable("entity") String entity,
                                     @ApiParam("实体id") @PathVariable("id") Long id){
        if (entity == null || id == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        switch (entity) {
            case EntityConstant.COMPANY:
                companyService.deleteById(id);
                break;
            case EntityConstant.GOVERNMENT:
                governmentService.deleteById(id);
                break;
            case EntityConstant.PERSON:
                personService.deleteById(id);
                break;
            case EntityConstant.POSITION:
                positionService.deleteById(id);
                break;
            case EntityConstant.SCHOOL:
                schoolService.deleteById(id);
                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:
                envAssessmentAnnouncementService.deleteById(id);
                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:
//                biddingAnnouncementService.deleteById(id);
                break;
            case EntityConstant.BID:
                bidService.deleteById(id);
                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:
//                bidWinningAnnouncementService.deleteById(id);
                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK);
    }

    @ApiOperation(value="修改实体",response = ResponseData.class)
    @PutMapping("/{entity}/{id}")
    public ResponseData updateEntity(@ApiParam("实体类型") @PathVariable("entity") String entity,
                                     @ApiParam("实体的id") @PathVariable("id") Long id,
                                     @ApiParam("实体的信息封装（必须包含id字段）") @RequestBody Object data){
        if (entity == null || id == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        switch (entity) {
            case EntityConstant.COMPANY:
                companyService.update(JSON.parseObject(JSON.toJSONString(data), Company.class));
                break;
            case EntityConstant.GOVERNMENT:
                governmentService.update(JSON.parseObject(JSON.toJSONString(data), Government.class));
                break;
            case EntityConstant.PERSON:
                personService.update(JSON.parseObject(JSON.toJSONString(data), Person.class));
                break;
            case EntityConstant.POSITION:
                positionService.update(JSON.parseObject(JSON.toJSONString(data), Position.class));
                break;
            case EntityConstant.SCHOOL:
                schoolService.update(JSON.parseObject(JSON.toJSONString(data), School.class));
                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:
                envAssessmentAnnouncementService.update(JSON.parseObject(JSON.toJSONString(data), EnvAssessmentAnnouncement.class));
                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:
//                biddingAnnouncementService.update(JSON.parseObject(JSON.toJSONString(data), BiddingAnnouncement.class));
                break;
            case EntityConstant.BID:
                bidService.update(JSON.parseObject(JSON.toJSONString(data), Bid.class));
                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:
//                bidWinningAnnouncementService.update(JSON.parseObject(JSON.toJSONString(data), BidWinningAnnouncement.class));
                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK);
    }

    @ApiOperation(value="添加实体",response = ResponseData.class)
    @PostMapping("/{entity}")
    public ResponseData addEntity(@ApiParam("实体类型") @PathVariable("entity") String entity,
                                  @ApiParam("实体的封装（不允许包含id字段）") @RequestBody Object data){
        if (entity == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        switch (entity) {
            case EntityConstant.COMPANY:
                companyService.add(JSON.parseObject(JSON.toJSONString(data), Company.class));
                break;
            case EntityConstant.GOVERNMENT:
                governmentService.add(JSON.parseObject(JSON.toJSONString(data), Government.class));
                break;
            case EntityConstant.PERSON:
                personService.add(JSON.parseObject(JSON.toJSONString(data), Person.class));
                break;
            case EntityConstant.POSITION:
                positionService.add(JSON.parseObject(JSON.toJSONString(data), Position.class));
                break;
            case EntityConstant.SCHOOL:
                schoolService.add(JSON.parseObject(JSON.toJSONString(data), School.class));
                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:
                envAssessmentAnnouncementService.add(JSON.parseObject(JSON.toJSONString(data), EnvAssessmentAnnouncement.class));
                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:
                biddingAnnouncementService.add(JSON.parseObject(JSON.toJSONString(data), BiddingAnnouncement.class));
                break;
            case EntityConstant.BID:
                bidService.add(JSON.parseObject(JSON.toJSONString(data), Bid.class));
                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:
                bidWinningAnnouncementService.add(JSON.parseObject(JSON.toJSONString(data), BidWinningAnnouncement.class));
                break;
            case EntityConstant.SUPPLIER:
                supplierService.add(JSON.parseObject(JSON.toJSONString(data), Supplier.class));
                break;
            case EntityConstant.HOSPITAL:
                hospitalService.add(JSON.parseObject(JSON.toJSONString(data), Hospital.class));
                break;
            case EntityConstant.MEDICAL_EQUIPMENT:
                medicalEquipmentService.add(JSON.parseObject(JSON.toJSONString(data), MedicalEquipment.class));
                break;
            case EntityConstant.PACKAGE:
                packageService.add(JSON.parseObject(JSON.toJSONString(data), Package.class));
                break;
            case EntityConstant.PROJECT:
                projectService.add(JSON.parseObject(JSON.toJSONString(data), Project.class));
                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK);
    }

    @ApiOperation(value="获取实体详情",response = ResponseData.class)
    @GetMapping("/{entity}/{id}")
    public ResponseData getEntityInfoById(@ApiParam("实体类型") @PathVariable("entity") String entity,
                                          @ApiParam("实体id") @PathVariable("id") Long id){
        if (entity == null || id == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        Object data;
        switch (entity) {
            case EntityConstant.COMPANY:
                data = companyService.getCompanyInfoById(id);
                break;
            case EntityConstant.GOVERNMENT:
                data = governmentService.getGovernmentInfoById(id);
                break;
            case EntityConstant.PERSON:
                data = personService.getPersonInfoById(id);
                break;
            case EntityConstant.POSITION:
                data = positionService.getPositionInfoById(id);
                break;
            case EntityConstant.SCHOOL:
                data = schoolService.getSchoolInfoById(id);
                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:
                data = envAssessmentAnnouncementService.getEnvAssessmentAnnouncementInfoById(id);
                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:
                data = biddingAnnouncementService.getBiddingAnnouncementInfoById(id);
                break;
            case EntityConstant.BID:
                data = bidService.getBidInfoById(id);
                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:
                data = bidWinningAnnouncementService.getBidWinningAnnouncementInfoById(id);
                break;
            case EntityConstant.SUPPLIER:
                data = supplierService.getSupplierInfoById(id);
                break;
            case EntityConstant.HOSPITAL:
                data = hospitalService.getHospitalInfoById(id);
                break;
            case EntityConstant.MEDICAL_EQUIPMENT:
                data = medicalEquipmentService.getMedicalEquipmentInfoById(id);
                break;
            case EntityConstant.PACKAGE:
                data = packageService.getPackageInfoById(id);
                break;
            case EntityConstant.PROJECT:
                data = projectService.getProjectInfoById(id);
                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK, data);
    }

    @ApiOperation(value="模糊匹配获取分页实体",response = ResponseData.class)
    @PostMapping("/fuzzySearch/{entity}")
    public ResponseData getPageEntitiesByFuzzySearch(@ApiParam("实体类型") @PathVariable("entity") String entity,
                                                     @ApiParam("页数") @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                                     @ApiParam("每页的数据数量") @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize,
                                                     @ApiParam("用于模糊查询的字段信息") @RequestBody Object data){
        if (entity == null){
            return ResponseData.setResult(ResultCodeEnum.PARA_FORMAT_ERROR);
        }
        PageData pageData = null;
        switch (entity) {
            case EntityConstant.COMPANY:
                pageData = companyService.getCompanyPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), Company.class), PageRequest.of(pageNum-1, pageSize));
                break;
            case EntityConstant.GOVERNMENT:
                pageData = governmentService.getGovernmentPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), Government.class), PageRequest.of(pageNum-1, pageSize));
                break;
            case EntityConstant.PERSON:

                HashMap<String, String> map = JSON.parseObject(JSON.toJSONString(data), HashMap.class);
                String[] names = map.get("name").trim().split(" ");

                String personName = "";
                String companyName = "";
                if (names.length == 1) {
                    personName = names[0];
                    companyName = "";
                }else {
                    personName = names[0];
                    companyName = names[names.length-1];
                }

                Person person = new Person();
                person.setName(personName);
                Company company = new Company();
                company.setName(companyName);

                pageData = personService.getPersonPageByFuzzySearch(person, PageRequest.of(pageNum-1,pageSize), company);
                break;
            case EntityConstant.POSITION:
                pageData = positionService.getPositionPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), Position.class), PageRequest.of(pageNum-1, pageSize));
                break;
            case EntityConstant.SCHOOL:
                pageData = schoolService.getSchoolPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), School.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT:
                pageData = envAssessmentAnnouncementService.getEnvAssessmentAnnouncementPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), EnvAssessmentAnnouncement.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.BIDDING_ANNOUNCEMENT:
                pageData = biddingAnnouncementService.getBiddingAnnouncementPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), BiddingAnnouncement.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.BID:
                pageData = bidService.getBidPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), Bid.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.BID_WINNING_ANNOUNCEMENT:
                pageData = bidWinningAnnouncementService.getBidWinningAnnouncementPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), BidWinningAnnouncement.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.SUPPLIER:
                pageData = supplierService.getSupplierPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), Supplier.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.HOSPITAL:
                pageData = hospitalService.getHospitalPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), Hospital.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.MEDICAL_EQUIPMENT:
                pageData = medicalEquipmentService.getMedicalEquipmentPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), MedicalEquipment.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.PACKAGE:
                pageData = packageService.getPackagePageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), Package.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            case EntityConstant.PROJECT:
                pageData = projectService.getProjectPageByFuzzySearch(JSON.parseObject(JSON.toJSONString(data), Project.class), PageRequest.of(pageNum - 1, pageSize));
                break;
            default:
                throw new KgException(ResultCodeEnum.ENTITY_LABEL_NOT_EXIST);
        }
        return ResponseData.setResult(ResultCodeEnum.OK,pageData);
    }
    @GetMapping("/entities/number")
    @ApiOperation(value = "返回所有实体的数量")
    public ResponseData getEntitiesNumber() {
        Map<String, Long> labelCountDatas = new HashMap<>();

//        labelCountDatas.put(EntityConstant.GOVERNMENT, governmentService.getCount());
//        labelCountDatas.put(EntityConstant.COMPANY, companyService.getCount());
        labelCountDatas.put(EntityConstant.BIDDING_ANNOUNCEMENT, biddingAnnouncementService.getCount());
//        labelCountDatas.put(EntityConstant.ENV_ASSESSMENT_ANNOUNCEMENT, envAssessmentAnnouncementService.getCount());
//        labelCountDatas.put(EntityConstant.PERSON, personService.getCount());
        labelCountDatas.put(EntityConstant.BID, bidService.getCount());
//        labelCountDatas.put(EntityConstant.POSITION, positionService.getCount());
//        labelCountDatas.put(EntityConstant.SCHOOL, schoolService.getCount());
        labelCountDatas.put(EntityConstant.BID_WINNING_ANNOUNCEMENT, bidWinningAnnouncementService.getCount());
        labelCountDatas.put(EntityConstant.SUPPLIER, supplierService.getCount());
        labelCountDatas.put(EntityConstant.HOSPITAL, hospitalService.getCount());
        labelCountDatas.put(EntityConstant.MEDICAL_EQUIPMENT, medicalEquipmentService.getCount());
        labelCountDatas.put(EntityConstant.PACKAGE, packageService.getCount());
        labelCountDatas.put(EntityConstant.PROJECT, projectService.getCount());

        return ResponseData.setResult(ResultCodeEnum.OK, labelCountDatas);
    }
}
