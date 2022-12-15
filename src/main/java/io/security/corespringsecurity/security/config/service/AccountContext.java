package io.security.corespringsecurity.security.config.service;

import io.security.corespringsecurity.domain.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * AuthenticationProvider 의 검증이 완료된 후 AuthenticationManager 로 전달하기 전에
 * 유저 정보와 권한을 셋팅하는 역할을 하는 클래스
 * 스프링에서 UserDetails 를 구현한 User 클래스를 제공하고 있으니 User 를 상속받은 것 !
 */
@Getter
public class AccountContext extends User {

    private final Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);

        /**
         * Authentication 에서 Account Entity 객체를 꺼내쓸 수 있게하기 위해.
         */
        this.account = account;
    }
}
