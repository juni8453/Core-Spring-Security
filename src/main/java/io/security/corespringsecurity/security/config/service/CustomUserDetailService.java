package io.security.corespringsecurity.security.config.service;

import io.security.corespringsecurity.domain.Account;
import io.security.corespringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
/**
 * UserDetailService 의 loadUserByUsername(username) 메서드를 통해 검증과정을 거치고 (ID 유효성 검사)
 * UserDetails Type 을 반환해 Password 유효성 검증, 다른 추가 검증을 거친 뒤 최종적으로 SecurityContext 등에 저장된다.
 */
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /**
         * 유저 유효성 검사를 위해 DB 에서 유저를 조회한다.
         */
        Account account = userRepository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));

        /**
         * 유효성 검사를 모두 통과했다면, 유저 정보, 유저 권한을 담은 UserDetails 구현체가 최종적으로 Filter 까지 반환되어야하므로,
         * AccountContext 객체를 반환한다. AccountContext 는 Authentication(유저정보, 유저권한) 이라고 생각하면 됨 !
         */
        // TODO 즉, AccountContext(유저 정보, 유저 권한) 을 반환하고 Provider 에서 Manager 로 넘길 때
        //  비어있는 Authentication 객체에 UserDetails 객체가 저장되는 것.
        return new AccountContext(account, roles);
    }
}
