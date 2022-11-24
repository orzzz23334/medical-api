package com.bupt.web.controller;

import com.alibaba.fastjson.JSON;
import com.bupt.web.model.pojo.User;
import com.bupt.web.model.vo.UserDetail;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseController {
    public static User getUserInfo() {
        Object details = SecurityContextHolder.getContext()
                .getAuthentication().getDetails();
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(details), Map.class);
        String tokenValue = map.get("tokenValue").toString();
        User userInfo = new User();
        try {
            SignedJWT sjwt = SignedJWT.parse(tokenValue);
            JWTClaimsSet claims = sjwt.getJWTClaimsSet();
            userInfo.setId(claims.getLongClaim("id"));
            userInfo.setRoles(claims.getStringListClaim("authorities"));
            userInfo.setName(claims.getStringClaim("user_name"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static String getUserName(){
        return getUserInfo().getName();
    }

    public static Long getUserId() {
        return getUserInfo().getId();
    }

    public static List<Long> getRoleIds() { return getUserInfo().getRoleIds(); }

    public static List<String> getRoles() {
        return getUserInfo().getRoles();
    }
}
