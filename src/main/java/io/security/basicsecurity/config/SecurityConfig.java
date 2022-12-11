package io.security.basicsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 인가 정책
        http.authorizeRequests()
            .anyRequest().authenticated();

        // 인증 정책
        http
            .formLogin()
//            .loginPage("/loginPage") // 직접 만든 페이지를 사용한다면 .loginPage() 사용하면 됨.
            .defaultSuccessUrl("/")
            .failureUrl("/login")
            .usernameParameter("userId")
            .passwordParameter("passwd")
            .loginProcessingUrl("/login_proc") // form Tag

            .successHandler((request, response, authentication) -> {
                System.out.println("authentication : " + authentication.getName());
                response.sendRedirect("/");
            })
            .failureHandler(((request, response, exception) -> {
                System.out.println("exception : " + exception.getMessage());
                response.sendRedirect("/login");
            }))
            .permitAll();

        return http.build();
    }
}

