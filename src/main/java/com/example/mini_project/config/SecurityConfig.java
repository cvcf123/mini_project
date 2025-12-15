package com.example.mini_project.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // #1
    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            MyAuthenticationSuccessHandler successHandler,
            MyAuthenticationFailureHandler failureHandler			) throws Exception{
        return http
                .authorizeHttpRequests( request -> request
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/register",
                                "/register.html",
                                "/login.html",
                                "/users/**",
                                "/csrf-token",
                                "/answer/list",//  답변 조회는 비로그인도 가능함
                                "/question/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"


                        ).permitAll() // 일부 요청
                        .anyRequest().authenticated() // 나머지 요청
                )
                // csrf on ( 기본 적용 )
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .formLogin(
                        form -> form
                                .loginPage("/login.html")  // 자체 login.html 설정
                                .loginProcessingUrl("/login") // spring security 의 기본 login (post) 으로 설정
                                .successHandler(successHandler) // 로그인 성공 시 처리자
                                .failureHandler(failureHandler) // 로그인 실패 시 처리자
                                // client 가 비동기 login 요청 <- client 가 결과에 따라 page 또는 데이터 처리를 하겠다.
                                // 아래 코드는 백엔드가 결정(redirect)
//							.defaultSuccessUrl("/")
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 기본값도 /logout
                        // MyAuthen~Handler 처럼 MyLogoutSuccessHandler 를 만들 수도 있지만, 아래 처럼 직접 처리도 가능
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    """
                                    {"result":"success"}
                                    """
                            );
                        })
                )
                .build();
    }
}
