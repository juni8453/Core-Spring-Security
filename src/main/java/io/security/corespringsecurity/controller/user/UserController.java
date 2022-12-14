package io.security.corespringsecurity.controller.user;

import io.security.corespringsecurity.domain.Account;
import io.security.corespringsecurity.domain.AccountDto;
import io.security.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/mypage")
    public String myPage() throws Exception {
        return "user/mypage";
    }

    @GetMapping("/users")
    public String createUser() {
        return "user/login/register";
    }

    @PostMapping("/users")
    public String createUser(AccountDto accountDto) {
        // Self Call 방지를 위해 같은 Bean 에서 호출하는 것이 아닌, 상위 클래스인 Controller 에서 따로 호출
        String encodedPassword = userService.passwordEncoding(accountDto.getPassword());
        Account account = AccountDto.toDto(accountDto, encodedPassword);
        userService.createUser(account);

        return "redirect:/";
    }
}
