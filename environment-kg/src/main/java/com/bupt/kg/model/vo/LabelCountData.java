package com.bupt.kg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Jacksonized
/**
 * 用于 汉化、一个LabelData建模一类节点或者一类关系
 * 存放的 key 和 value 分别代表数据库的字段和对应的汉语
 */
public class LabelCountData {
    private Map<String, Long> labelCounts = new HashMap<>();

    public void addCount(String label, Long number) {
        labelCounts.put(label, number);
    }

}
