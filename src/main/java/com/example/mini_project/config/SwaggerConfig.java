package com.example.mini_project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("미니 프로젝트 게시판 API 문서")
                        .description("질문,답변,댓글 ,유저에 대한 api 명세서 ")
                        .version("1.0.0"));
    }
}
