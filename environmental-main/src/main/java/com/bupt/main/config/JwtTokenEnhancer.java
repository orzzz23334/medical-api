package com.bupt.main.config;

import com.bupt.web.model.vo.UserDetail;
import com.google.common.collect.Maps;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * JWT增强器
 *
 * @author zhongpingli
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserDetail uimUserDetailsVO = (UserDetail) authentication.getPrincipal();
        Map<String, Object> info = Maps.newHashMapWithExpectedSize(3);
        //把用户ID设置到JWT中
        info.put("id", uimUserDetailsVO.getId());
//        info.put("roles",uimUserDetailsVO.getRoles());
//        info.put("roleIds",uimUserDetailsVO.getRoleIds());
//        info.put("organId", uimUserDetailsVO.getOrganId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}
