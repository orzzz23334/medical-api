package com.bupt.main.config;



import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 配置 Swagger2.
 * API 浏览、测试工具, 访问页面 <code>http://{host:port}/swagger-ui.html</code>
 * https://yq.aliyun.com/articles/181435
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // 定义分隔符,配置Swagger多包
    private static final String splitter = ";";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(basePackage("com.bupt"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(token())
                .securityContexts(contexts())
                .directModelSubstitute(LocalDate.class, Date.class)
                .useDefaultResponseMessages(false);
    }


    private List<ApiKey> token() {
        List<ApiKey> keys = new ArrayList<>();
        keys.add(new ApiKey("Authorization", "Authorization", "header"));
        return keys;
    }

    private List<SecurityContext> contexts() {
        List<SecurityContext> contexts = new ArrayList<>();
        contexts.add(SecurityContext.builder()
                .securityReferences(auth())
                .forPaths(PathSelectors.any())
                .build());
        return contexts;
    }


    private List<SecurityReference> auth() {
        List<SecurityReference> auths = new ArrayList<>();
        auths.add(new SecurityReference("Authorization", new AuthorizationScope[0]));
        return auths;
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Programming Makes Us Happy")
                .description("Environmental Project")
                .contact(new Contact("Crescent73",
                        "",
                        ""))
                .version("1.0.0")
                .build();
    }


    /**
     * 重写basePackage方法，使能够实现多包访问，复制贴上去
     *
     * @param basePackage [basePackage]
     * @return com.google.common.base.Predicate<springfox.documentation.RequestHandler>
     * @date 2019/1/26
     */
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitter)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
