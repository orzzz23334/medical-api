package com.bupt.web.model.vo;

import com.bupt.web.model.pojo.Role;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RoleVo {
    private Long id;
    private String name;
    private String key;
    private Set<String> operation;

    public RoleVo(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.key = role.getKey();
        this.operation = role.getOperation();
    }
}
