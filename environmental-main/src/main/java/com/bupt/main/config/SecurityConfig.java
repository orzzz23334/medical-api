package com.bupt.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * spring security配置
 *
 * @author mrli
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // WebSecurityConfigurerAdapter是默认情况下spring security的http配置

    /**
     * swagger过滤文件
     */
    private static final String[] SWAGGER_AUTH_LIST = {
            "/swagger-resources/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/api/v1/swagger-ui.html"
    };

    /**
     * 系统过滤地址
     */
    private static final String[] SYSTEM_AUTH_LIST = {"/oauth/**", "/kg/**", "/web/user/register"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 默认的加密工具
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(SWAGGER_AUTH_LIST) // 给swagger放行
                .permitAll()
                .antMatchers(SYSTEM_AUTH_LIST) // 给系统api放i选哪个
                .permitAll()
//                .anyRequest()
//                .access("@rbacService.hasPermission(request,authentication)")
                .and()
                .formLogin()
                .permitAll();
    }
}
