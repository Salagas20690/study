package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.photogramstart.config.oauth.OAuth2DeatilsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity // 해당 파일로 시큐리티를 활성화
@Configuration // IoC
public class SecurityConfig {
	
	private final OAuth2DeatilsService oAuth2DetailsService;

	@Bean
	BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); //csrf 토큰 검사 비활성화
		http.authorizeRequests()
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/auth/signin") // GET
			.loginProcessingUrl("/auth/signin") // POST -> 스프링 시큐리티가 로그인 프로세스 진행
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login() // form 로그인도 하는데, oauth2로그인도 할거야
			.userInfoEndpoint() // oauth2로그인을 하면 최종응답을 회원정보로 받을수있다.
			.userService(oAuth2DetailsService);
		return http.build();
	}
	
//	@Override 더이상 사용하지 않음(extends websecurityconfigureradapter)
//	protected void configure(HttpSecurity http) throws Exception {
//		// TODO Auto-generated method stub
//		// super.configure(http); - 기존 시큐리티 비활성화
//		http.authorizeHttpRequests()
//			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**").authenticated()
//			.anyRequest().permitAll()
//			.and()
//			.formLogin()
//			.loginPage("/auth/signin")
//			.defaultSuccessUrl("/");
//	}
	
}
