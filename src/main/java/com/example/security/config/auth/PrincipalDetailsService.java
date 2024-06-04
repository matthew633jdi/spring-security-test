package com.example.security.config.auth;

import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Security session => Authentication => UserDetails

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // username은 login.html에서 name=username과 일치시켜야 함
    // 아니면 SecurityConfig에서 userParameter로 변경해줘야 함
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("=== Log in User name: {}", username);
        Optional<User> optional = userRepository.findByUsername(username);
        User user = optional.orElseThrow(() -> {
                log.error("=== Not Found user name: " + username);
                throw new UsernameNotFoundException("Not found user name: " + username);
        });

        return new PrincipalDetails(user);
    }
}
