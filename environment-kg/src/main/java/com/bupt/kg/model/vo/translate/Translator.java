package com.bupt.kg.model.vo.translate;

import com.bupt.kg.model.dto.TranslateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class Translator {
    // 节点或关系的中文名称
    private String key;
    // 节点或者关系的数据库名称
    private String value;

    private List<TranslateDto> properties;
}
