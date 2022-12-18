package io.security.corespringsecurity.service;

import io.security.corespringsecurity.domain.Account;

public interface UserService {

  String passwordEncoding(String password);

  void createUser(Account account);
}
