package com.pawalert.backend.global.oauth;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.domain.user.repository.UserRepository;
import com.pawalert.backend.global.config.redis.RedisService;
import com.pawalert.backend.global.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService 사용
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 제공자 정보
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, naver, kakao
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        System.out.println("OAuth2User loadUser 클래스");


        // 사용자 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String username;
        String profileImageUrl = null;  // 프로필 이미지 URL 저장
        String email = null;  // Google과 Naver의 경우 이메일 저장
        String uid;  // 소셜 로그인에서 제공하는 고유 ID


        System.out.println("Attributes: " + attributes);

        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            // todo : kakao 는 비지니스 인증을 받아야 email 을 가져올 수 있음
            username = (String) profile.get("nickname");
            profileImageUrl = (String) profile.get("profile_image_url"); // 카카오의 프로필 이미지 URL
            uid = String.valueOf(attributes.get("id")); // 카카오는 "id"가 사용자 고유 ID
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            if (response == null || !response.containsKey("id")) {
                throw new OAuth2AuthenticationException("Missing 'id' attribute in Naver response.");
            }

            username = (String) response.get("name");
            email = (String) response.get("email");
            profileImageUrl = (String) response.get("profile_image"); // 네이버의 프로필 이미지 URL
            uid = (String) response.get("id"); // "id" 필드를 직접 사용
        } else {
            username = oAuth2User.getAttribute("name");
            email = oAuth2User.getAttribute("email");
            profileImageUrl = oAuth2User.getAttribute("picture"); // 구글의 프로필 이미지 URL
            uid = oAuth2User.getAttribute(userNameAttributeName); // Google은 "sub"
        }

        // todo : 서버 배포 후 동작확인해보기
//        redisService.loginSaveData(uid, "192.168.0.0.1", LocalDateTime.now()); // 로그인 정보 저장


        System.out.println("Attributes: " + attributes);

        // 사용자 정보 저장 또는 업데이트
        UserEntity user = userRepository.findByUid(uid)
                .orElseGet(UserEntity::new);


        user.setUserName(username);


        if (email != null) {
            user.setEmail(email);
        }
        if (profileImageUrl != null) {
            user.setProfilePictureUrl(profileImageUrl);// 프로필 이미지 URL 설정
        }

        user.setUid(uid);
        user.setAuthProvider(registrationId);
        userRepository.save(user);

        // 사용자 권한 설정
        return new DefaultOAuth2User(
                Collections.singletonList(() -> user.getRole().name()),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }
}
