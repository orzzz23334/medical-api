package com.bupt.main.config;

import com.bupt.web.service.impl.UserServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.List;


/**
 * 认证服务器
 *
 * @author mrli
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager; // password grants are switched on by injecting an AuthenticationManager

    @Resource
    private UserServiceImpl userServiceImpl;

    @Resource
    private JwtTokenEnhancer jwtTokenEnhancer;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //a configurer that defines the client details service. Client details can be initialized, or you can just refer to an existing store.
        clients.inMemory()// Basic ZW52aXJvbm1lbnRhbDplbnZpcm9ubWVudGFs
                .withClient("environmental")//the client id.
                .secret(passwordEncoder.encode("environmental")) //the client id.
                .accessTokenValiditySeconds(60 * 60 * 4)
//                .refreshTokenValiditySeconds(60 * 60 * 12)
                .scopes("all") //The scope to which the client is limited. If scope is undefined or empty (the default) the client is not limited by scope.
                .authorizedGrantTypes("authorization_code", "password"); //Grant types that are authorized for the client to use
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //configure(ClientDetailsServiceConfigurer clients)
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = Lists.newArrayList();
        delegates.add(jwtTokenEnhancer);// set additional info
        delegates.add(accessTokenConverter());
        enhancerChain.setTokenEnhancers(delegates);
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userServiceImpl)
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(enhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //defines the authorization and token endpoints and the token services.
        security.allowFormAuthenticationForClients(); // 设置表单授权
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        System.out.println(jwtAccessTokenConverter.getAccessTokenConverter());
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "uimsys".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "uimsys".toCharArray());
    }


}
