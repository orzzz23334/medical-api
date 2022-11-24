package com.bupt.web.model.vo;

import com.bupt.web.model.pojo.Menu;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TreeSelect {

    /** 节点名称 */
    private String title;
    /** 节点ID */
    private Long key;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect(Menu menu) {
        this.key = menu.getId();
        this.title = menu.getName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}
