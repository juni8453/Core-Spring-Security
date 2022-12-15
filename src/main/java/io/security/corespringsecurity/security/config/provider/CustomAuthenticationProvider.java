package io.security.corespringsecurity.security.config.provider;

import io.security.corespringsecurity.security.config.service.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * CustomAuthenticationProvider 클래스에서 실질적으로 인증을 처리하는 로직
     * AuthenticationManager 에서 받아온 Username, Credential 만 가지고있는 Authentication 타입 객체를 검증하는 로직이다.
     * ID 는 loadUserByUsername() 에서 검증이 됬으니, PW, 추가 검증 후 Authentication(유저 정보, 권한 정보) AuthenticationManager 로 반환해야한다.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, accountContext.getPassword())) {
            throw new BadCredentialsException("일치하지 않는 패스워드입니다.");
        }

        /**
         *  우리는 FormLogin 방식이기 때문에 Authentication 을 구현한 구현체로 UsernamePasswordAuthenticationToken 을 사용한다.
         *  패스워드 검증 이후 Authentication 객체 내부에 유저 정보 + 유저 권한을 셋팅하기 위해서, 인자로 UserDetails 를 구현한
         *  AccountContext 클래스를 사용한다.
         *  Credential (Password) 는 null 로 초기화하는 것을 권장한다.
         */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            accountContext.getAccount(), null, accountContext.getAuthorities()
        );

        return authenticationToken;
    }

    /**
     * ProviderManager 객체에서 유저 인증을 처리할 수 있는 자식부터 부모까지 적절한 Provider 를 찾는 로직이 있는데,
     * 우리가 만든 CustomAuthenticationProvider 객체를 찾아 supports() 메서드로 타고 들어오는 것을 처리하는 메서드
     * 즉, Authentication 객체를 검증해 인자로 받은 Authentication 객체 타입과 일치할 때
     * CustomAuthenticationProvider 클래스가 인증을 처리하도록 조건을 주는 것.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
