package com.bupt.kg.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装汉化信息
 */
@AllArgsConstructor
@Getter @Setter
public class TranslateDto {
    private String key;
    private String value;
}
