package com.bupt.kg.service;

import com.bupt.kg.common.constant.RelationConstant;
import com.bupt.kg.common.enums.ResultCodeEnum;
import com.bupt.kg.exception.KgException;
import com.bupt.kg.model.relation.*;
import com.bupt.kg.model.vo.translate.Translator;
import com.bupt.kg.service.TranslateService;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TranslateServiceImpl implements TranslateService {
    @Override
    public Translator getTranslatorByEntityType(String entityType) {
        Translator translator = null;
        try {
            translator = (Translator) Class.forName("com.bupt.kg.model.entity."+entityType).getField("translator").get(null);
        } catch (ClassNotFoundException e){
            throw new KgException(ResultCodeEnum.SERVER_REFLACT_CLASSNOTFOUND_ERROR);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new KgException(ResultCodeEnum.SERVER_REFLACT_FIELDNOTFOUND_ERROR);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new KgException(ResultCodeEnum.SERVER_REFLACT_FIELDACCESS_ERROR);
        }

        return translator;
    }

    @Override
    public List<Translator> getAllEntityTranslator() {
        List<Translator> translators = new ArrayList<>();
        try {
            Field[] entityTypes = Class.forName("com.bupt.kg.common.constant.EntityConstant").getFields();
            for (Field entityType : entityTypes) {
                try {
                    translators.add(this.getTranslatorByEntityType((String) entityType.get(null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new KgException(ResultCodeEnum.SERVER_REFLACT_FIELDACCESS_ERROR);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new KgException(ResultCodeEnum.SERVER_REFLACT_CLASSNOTFOUND_ERROR);
        }
        return translators;
    }

    @Override
    public Translator getTranslatorByRelationType(String RelationType) {
        Translator translator = null;
        switch (RelationType){
            case RelationConstant.AGENT:
                translator = Agent.translator;
                break;
            case RelationConstant.ATTEND:
                translator = AttendRelation.translator;
                break;
            case RelationConstant.CONSTRUCT:
                translator = Construct.translator;
                break;
            case RelationConstant.EVALUATE:
                translator = Evaluate.translator;
                break;
            case RelationConstant.INCLUDE:
                translator = IncludeRelation.translator;
                break;
            case RelationConstant.STOCKHOLDER:
                translator = StockholderRelation.translator;
                break;
            case RelationConstant.SUPPLY:
                translator = SupplierRelation.translator;
                break;
            case RelationConstant.TAKE_OFFICE:
                translator = TakeOfficeRelation.translator;
                break;
            case RelationConstant.WINBID:
                translator = WinBid.translator;
                break;
            case RelationConstant.EVALUATE_BID:
                translator = EvaluateBid.translator;
                break;
            case RelationConstant.IS_FRIEND_OF:
                translator = IsFriendOf.translator;
                break;
        }
        return Option.of(translator).getOrElseThrow(() -> new KgException(ResultCodeEnum.RELATION_LABEL_NOT_EXIST));
    }

    @Override
    public List<Translator> getAllRelationTranslator() {
        List<Translator> translators = new ArrayList<>();
        translators.add(IsFriendOf.translator);
        translators.add(Agent.translator);
        translators.add(AttendRelation.translator);
        translators.add(Construct.translator);
        translators.add(Evaluate.translator);
        translators.add(IncludeRelation.translator);
        translators.add(StockholderRelation.translator);
        translators.add(SupplierRelation.translator);
        translators.add(TakeOfficeRelation.translator);
        translators.add(WinBid.translator);
        translators.add(EvaluateBid.translator);


        return translators;
    }
}
