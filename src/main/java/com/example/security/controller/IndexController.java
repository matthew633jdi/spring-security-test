package com.example.security.controller;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.request.UserCreate;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;

    // plain login -> UserDetails
    @GetMapping("/test/login")
    public @ResponseBody
    String testLogin(Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("/test/login ===");
        PrincipalDetails principalDetails1 = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails1.getUser() = " + principalDetails1.getUser());
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        return "session info";
    }

    // OAuth login -> OAuth2User
    @GetMapping("/test/oauth/login")
    public @ResponseBody
    String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth2User) {
        System.out.println("/test/login ===");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
        System.out.println("oauth2User.getAttributes() = " + oauth2User.getAttributes());
        return "session info";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/join")
    public String join(UserCreate request) {
        userService.signup(request);
        return "redirect:/";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "info";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "data";
    }
}
