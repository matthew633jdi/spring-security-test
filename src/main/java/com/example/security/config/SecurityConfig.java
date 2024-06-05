package com.example.security.config;

import com.example.security.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;


    // WebSecurityConfigurerAdapter는 Spring Security 5.7.0-M2부터 deprecated
    // 이 후 버전은 아래와 같이 bean으로 등록하여 사용
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // csrf disable
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService)))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
    // Tip. google login은 완료 후 code가 아닌 Access Token과 user profile을 준다.
    // google login -> 후 처리 필요
    // 1. code 받기(authentication) 2. Access Token(authorization)
    // 3. user profile 가져옴
    // 4-1. 정보를 토대로 회원가입을 자동으로 진행 or
    // 4-2. 추가 정보를 입력하도록 할 수 있음. (쇼핑몰 -> 집주소, 백화점 -> 등급 등)

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
