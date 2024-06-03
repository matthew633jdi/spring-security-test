package com.example.security.service;

import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import com.example.security.request.UserCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(UserCreate userCreate) {
        String encodedPwd = bCryptPasswordEncoder.encode(userCreate.getPassword());
        User user = User.builder()
                .username(userCreate.getUsername())
                .email(userCreate.getEmail())
                .role("USER")
                .password(encodedPwd)
                .build();

        userRepository.save(user);
    }
}
