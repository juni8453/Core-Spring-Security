package io.security.basicsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

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
            .loginProcessingUrl("/login_proc") // form Tag (AntPathRequestMatcher Class 관련)

            .successHandler((request, response, authentication) -> {
                System.out.println("authentication : " + authentication.getName());
                response.sendRedirect("/");
            })
            .failureHandler(((request, response, exception) -> {
                System.out.println("exception : " + exception.getMessage());
                response.sendRedirect("/login");
            }))
            .permitAll();

        http
            .logout()
//            .logoutUrl("/logout") logoutSuccessHandler() sendRedirect() 있기 때문에 없어도 되는 로
            .logoutSuccessUrl("/login")

            // 로그아웃 핸들러가 처리하는 세션 무효, 인증 토큰 삭제 작업 외 필요한 별도 처리를 위해 사용자가 정의한 핸들러를 추가할 수 있다.
            .addLogoutHandler(((request, response, authentication) -> {
                HttpSession session = request.getSession();
                session.invalidate();
            }))
            .logoutSuccessHandler(((request, response, authentication) -> { // loginSuccessUrl() + 비즈니스 로직을 담을 수 있다.
                System.out.println("로그아웃을 성공했습니다.");
                response.sendRedirect("/login");
            }))
            .deleteCookies("remember-me");

        http
            .rememberMe()
            .rememberMeParameter("remember") // 기본 Parameter 명은 remember-me
            .tokenValiditySeconds(3600) // 기본 유지 시간은 14일
            .alwaysRemember(false) // 기본 값은 false, true 로 두면 Remember-me 기능이 활성화되지 않아도 항상 실행
            .userDetailsService(userDetailsService); // Remember-me 인증 시 유저 계정을 조회하는 처리를 위한 클래스 설

        return http.build();
    }
}

