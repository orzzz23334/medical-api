package com.bupt.kg.service;

import com.bupt.kg.model.vo.translate.Translator;

import java.util.List;

public interface TranslateService {
    Translator getTranslatorByEntityType(String entityType);
    List<Translator> getAllEntityTranslator();
    Translator getTranslatorByRelationType(String RelationType);
    List<Translator> getAllRelationTranslator();
}
