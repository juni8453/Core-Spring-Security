//package io.security.basicsecurity.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.savedrequest.SavedRequest;
//
//import javax.servlet.http.HttpSession;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public UserDetailsManager users() {
//
//        UserDetails user = User.builder()
//            .username("user")
//            .password("{noop}1111")
//            .roles("USER")
//            .build();
//
//        UserDetails sys = User.builder()
//            .username("sys")
//            .password("{noop}1111")
//            .roles("SYS")
//            .build();
//
//        UserDetails admin = User.builder()
//            .username("admin")
//            .password("{noop}1111")
//            .roles("ADMIN", "SYS", "USER")
//            .build();
//
//        return new InMemoryUserDetailsManager(user, sys, admin);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        // 인증, 인가 예외 API
//        http
//            .exceptionHandling()
//
//            // 인증 예외 처리
//            .authenticationEntryPoint(((request, response, authException) -> {
//                response.sendRedirect("/login");
//            }))
//
//            // 인가 예외 처리
//            .accessDeniedHandler((request, response, accessDeniedException) -> {
//                response.sendRedirect("/denied");
//            });
//
//        // 인가 정책
//        // 설정 시 구체적인 경로가 먼저 오고, 그것보다 큰 범위의 경로가 뒤에 오도록 설정하자.
//        http
//            // /shop 을 포함하는 모든 도메인에 대해 인가 요청 발생
////            .antMatcher("/shop/**")
//            .authorizeRequests()
//            .antMatchers("/login").permitAll()
//            .antMatchers("/user").hasRole("USER")
//            .antMatchers("/admin/pay").hasRole("ADMIN")
//            .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
//            .anyRequest().authenticated();
//
//        // 인증 정책
//        http
//            .formLogin()
////            .loginPage("/loginPage") // 직접 만든 페이지를 사용한다면 .loginPage() 사용하면 됨.
//            .defaultSuccessUrl("/")
//            .failureUrl("/login")
//            .usernameParameter("userId")
//            .passwordParameter("passwd")
//            .loginProcessingUrl("/login_proc") // form Tag (AntPathRequestMatcher Class 관련)
//
//            .successHandler((request, response, authentication) -> {
//                // login 성공 시 Cache 에 사용자 정보를 저장해놨기 때문에 로그인 시도 시 바로 원하는 곳으로 보낼 수 있다.
//                HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//                // saveRequest 안에 원래 사용자가 가고자 했던 요청 정보가 저장되어있다.
//                SavedRequest saveRequest = requestCache.getRequest(request, response);
//                String redirectUrl = saveRequest.getRedirectUrl();
//                response.sendRedirect(redirectUrl);
//            })
//            .failureHandler(((request, response, exception) -> {
//                System.out.println("exception : " + exception.getMessage());
//                response.sendRedirect("/login");
//            }))
//            .permitAll();
//
//        http
//            .logout()
////            .logoutUrl("/logout") logoutSuccessHandler() sendRedirect() 있기 때문에 없어도 되는 로
//            .logoutSuccessUrl("/login")
//
//            // 로그아웃 핸들러가 처리하는 세션 무효, 인증 토큰 삭제 작업 외 필요한 별도 처리를 위해 사용자가 정의한 핸들러를 추가할 수 있다.
//            .addLogoutHandler(((request, response, authentication) -> {
//                HttpSession session = request.getSession();
//                session.invalidate();
//            }))
//            .logoutSuccessHandler(((request, response, authentication) -> { // loginSuccessUrl() + 비즈니스 로직을 담을 수 있다.
//                System.out.println("로그아웃을 성공했습니다.");
//                response.sendRedirect("/login");
//            }))
//            .deleteCookies("remember-me");
//
//        // 동시적 세션 제어
//        http
//            .rememberMe()
//            .rememberMeParameter("remember") // 기본 Parameter 명은 remember-me
//            .tokenValiditySeconds(3600) // 기본 유지 시간은 14일
//            .alwaysRemember(false) // 기본 값은 false, true 로 두면 Remember-me 기능이 활성화되지 않아도 항상 실행
//            .userDetailsService(userDetailsService); // Remember-me 인증 시 유저 계정을 조회하는 처리를 위한 클래스 설
//
//
//        // SessionManagementFilter, ConcurrentSessionFilter 의 연계로 처리
//        // SessionManagementFilter 에서 먼저 Session 만료시키고, ConcurrentSessionFilter 에서 Session 이 만료되었는지 확인하는 순서로 동작
//        // PPT 11 페이지 참고
//        http
//            .sessionManagement()
//            .maximumSessions(1) // 최대 Session 저장 허용 갯수
//            .maxSessionsPreventsLogin(true); // true -> 현재 사용자 인증 실패, 동시 로그인 차단 / false -> 이전 사용자 세션 만료
//
//        // 세션 고정 공격 대비 로직
//        // changeSessionId() 기본 값을 통해 공격자가 사용자에게 자신의 Cookie 를 집어넣고 인증받게 하더라도 인증 성공 후 Cookie 가 변경되기 때문에
//        // 세션 고정 공격을 막을 수 있다.
//        http
//            .sessionManagement()
//            .sessionFixation().changeSessionId();
//
//        // 세션 생성 정책
//        /**
//         * 1. 항상 Security 에서 Session 생성
//         * 2. 필요 시 Security 에서 Session 생성
//         * 3. Security 가 Session 을 생성하지 않지만, 이미 존재하는 Session 이 있다면 사용
//         * 4. Security 가 Session 을 생성하지도 않고, 사용하지도 않음.
//         */
//
//        http
//            .sessionManagement()
////            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) (1)
//            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED); // 기본 (2)
////            .sessionCreationPolicy(SessionCreationPolicy.NEVER) (3)
////            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) (4)
//
//        return http.build();
//    }
//}
//
