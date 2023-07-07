package com.cos.photogramstart.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DeatilsService extends DefaultOAuth2UserService {
	
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("OAuth2 서비스 탐");
		OAuth2User oauth2User = super.loadUser(userRequest);
		System.out.println(oauth2User.getAttributes());
		
		Map<String, Object> userInfo = oauth2User.getAttributes();
		
		System.out.println("=========================================================");
		System.out.println((long)userInfo.get("id"));
		System.out.println(((Map)userInfo.get("properties")).get("nickname"));
		System.out.println(((Map)userInfo.get("kakao_account")).get("email"));
		System.out.println("=========================================================");
		
		String username = "kakao_" + userInfo.get("id").toString();
		String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
		String email = (String) ((Map)userInfo.get("kakao_account")).get("email");
		String name = (String) ((Map)userInfo.get("properties")).get("nickname");
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) { // 최초 로그인
			User user = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.name(name)
					.role("ROLE_USER")
					.build();
			userRepository.save(user);
			
			return new PrincipalDetails(userRepository.save(user), oauth2User.getAttributes());
			
		} else { // 이미 가입됨
			return new PrincipalDetails(userEntity, oauth2User.getAttributes());
		}
	
	}
	
}
