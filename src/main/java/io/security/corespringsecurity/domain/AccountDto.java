package io.security.corespringsecurity.domain;

import lombok.Data;

@Data
public class AccountDto {

  private String username;

  private String password;

  private String email;

  private String age;

  private String role;

  public static Account toDto(AccountDto accountDto, String encodedPassword) {
    return Account.builder()
        .username(accountDto.getUsername())
        .password(encodedPassword)
        .email(accountDto.getEmail())
        .age(accountDto.getAge())
        .role(accountDto.getRole())
        .build();
  }
}
