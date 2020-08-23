package com.jinchae.demo.config;

import com.google.common.collect.Lists;
import com.jinchae.demo.entity.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * swagger config
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jinchae.demo.controller"))
                .paths(PathSelectors.ant("/v1/member/**"))
                .build()
                .apiInfo(apiInfo())
                .globalOperationParameters(
                        Lists.newArrayList(
                                new ParameterBuilder()
                                        .name("Authorization")
                                        .description("Bearer ${JWT_TOKEN}")
                                        .modelRef(new ModelRef("string"))
                                        .parameterType("header")
                                        .required(false)
                                        .build()
                        ))
                .ignoredParameterTypes(UserPrincipal.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "API",
                "회원 가입/조회 API",
                "1.0.0",
                "Terms of service",
                new Contact("채지인", "www.example.com", "kkam2ya@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }

}
