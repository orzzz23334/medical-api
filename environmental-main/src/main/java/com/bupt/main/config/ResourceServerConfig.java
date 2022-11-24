package com.bupt.main.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

import javax.annotation.Resource;

/**
 * 资源服务器配置
 *
 * @author mrli
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    // ResourceServerConfigurerAdapter是默认情况下spring security oauth2的http配置

    /**
     * swagger过滤文件
     */
    private static final String[] SWAGGER_AUTH_LIST = {
            "/v2/api-docs",
            "/swagger-resources/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
    };

    /**
     * 系统过滤地址
     */
    private static final String[] SYSTEM_AUTH_LIST = {"/oauth/**", "/statics/**", "/kg/**", "/web/user/register"};

    @Resource
    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/fileinfo/**")
//                .permitAll()
//                .antMatchers(HttpMethod.DELETE, "/fileinfo/**")
//                .permitAll()
                .antMatchers(SWAGGER_AUTH_LIST)
                .permitAll()
                .antMatchers(SYSTEM_AUTH_LIST)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll();
    }


    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.expressionHandler(expressionHandler);
    }
}
