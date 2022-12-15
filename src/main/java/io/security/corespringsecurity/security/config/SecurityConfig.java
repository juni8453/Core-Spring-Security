package io.security.corespringsecurity.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users() {
        String password = passwordEncoder().encode("1111");

        UserDetails user = User.builder()
            .username("user")
            .password(password)
            .roles("USER")
            .build();

        UserDetails manager = User.builder()
            .username("manager")
            .password(password)
            .roles("MANAGER", "USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(password)
            .roles("ADMIN", "MANAGER")
            .build();

        return new InMemoryUserDetailsManager(user, manager, admin);
    }

    // 보안 필터를 적용할 필요가 없는 정적 리소스 ignore 처리
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 인가 정책 설정
        http
            .authorizeRequests()
            .antMatchers("/", "/users").permitAll()
            .antMatchers("/mypage").hasRole("USER")
            .antMatchers("/message").hasRole("MANAGER")
            .antMatchers("/config").hasRole("ADMIN")
            .anyRequest().authenticated();


        // 인증 정책 설정
        http
            .formLogin();

        return http.build();
    }
}
