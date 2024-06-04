package com.example.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EncryptionTest {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("암호화")
    void encrypt() {
        // given
        String pwd = "123";
        // when
        String encode = bCryptPasswordEncoder.encode(pwd);
        // then
        assertThat(encode).isNotNull();
        System.out.println("encode = " + encode);
    }

    @Test
    @DisplayName("복호화 불가 -> 매칭")
    void decrypt() {
        // given
        String pwd = "123";
        String encode = bCryptPasswordEncoder.encode(pwd);

        // then
        assertThat(bCryptPasswordEncoder.matches(pwd, encode)).isTrue();
    }

}
