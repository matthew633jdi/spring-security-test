package com.example.security.config.oauth;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientName();
        String providerId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USER";
        // 필요없는 정보라서 아무 값이나 넣음
        String name = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("TTTT");

        Optional<User> optional = userRepository.findByUsername(name);
        User user = null;

        if (optional.isEmpty()) {
            user = User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .email(email)
                    .username(name)
                    .role(role)
                    .password(password)
                    .build();

            userRepository.save(user);
        }

        return new PrincipalDetails(user, oauth2User.getAttributes());
    }
}
