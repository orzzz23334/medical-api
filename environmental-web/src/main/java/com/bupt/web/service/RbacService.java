package com.bupt.web.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface RbacService {
    /**
     * 获取角色对应资源
     *
     * @param request
     * @param authentication
     * @return
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
