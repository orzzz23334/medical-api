package com.bupt.web.service.impl;

import com.bupt.web.common.enums.PermissionEnum;
import com.bupt.web.service.RbacService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

import static io.vavr.API.*;

@Component("rbacService")
public class RbacServiceImpl implements RbacService {
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        // 1. 判断访问的资源是否是公共资源
        boolean exists = publicResourceCheck(request.getRequestURI());
        if (exists) {
            return true;
        }
        // 2. 获取用户角色
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.size() == 0) {
            return false;
        }
        // 3. 获取请求方式, 支持的四种请求方式才放行
        String method = request.getMethod();
        PermissionEnum permissionEnum = Match(method).of(
                Case($("GET"), PermissionEnum.BROWSE),
                Case($("PUT"), PermissionEnum.UPDATE),
                Case($("POST"), PermissionEnum.CREATE),
                Case($("DELETE"), PermissionEnum.DELETE),
                Case($(), PermissionEnum.OTHER)
        );
        if (permissionEnum.equals(PermissionEnum.OTHER)) {
            return false;
        }
        // 4. 获取角色对应的资源
        // 4.1 获取当前用户的角色列表
        return false;
    }

    private Boolean publicResourceCheck(String url){
        return false;
    }
}
