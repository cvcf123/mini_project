package com.example.mini_project.config;

import java.io.IOException;

import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 비동기 로그인 요청에 대해서 성공했을 때 전달되는 핸들러
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // response 로 성공, result:success json 응답
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        String jsonStr = """
				{"result":"success"}
				""";
        response.getWriter().write(jsonStr);
    }

}
