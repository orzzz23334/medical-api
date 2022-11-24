package com.bupt.web.model.vo;

import com.bupt.web.model.pojo.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MenuVo {
    private String name;
    private String router;
    private String path;
    private String icon;
    private String component;
    private String redirect;
    private Boolean invisible;
    private Authority authority;
    private List<MenuVo> children;
    @Data
    static class Authority {
        private Long[] role;
    }

    public MenuVo(Menu menu) {
        this.name = menu.getName();
//        this.router = menu.getRouter();
        this.path = menu.getPath();
        this.icon = menu.getIcon();
        this.invisible = menu.getInvisible();
        this.component = menu.getComponent();
        this.redirect = menu.getRedirect();
        // 设置roles
//        if (menu.getRoles().length > 0) {
//            this.authority = new Authority();
//            this.authority.setRole(menu.getRoles());
//        }
        // 递归设置children
        this.children = menu.getChildren().stream().map(MenuVo::new).collect(Collectors.toList());

    }
}
