package io.security.basicsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/login")
    public String login() {
        return "This is not Spring Security loginPage";
    }

    @GetMapping("/denied")
    public String denied() {
        return "Access Denied";
    }

    @GetMapping("/user")
    public String user() {

        return "user";
    }

    @GetMapping("/admin/pay")
    public String adminPay() {
        return "adminPay";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
