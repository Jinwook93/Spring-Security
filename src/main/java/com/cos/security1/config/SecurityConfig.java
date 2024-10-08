package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
																			//securedEnabled : @Secured 어노테이션 활성화
																		//prePostEnabled : ,@PostAuthorize, @PreAuthorize 어노테이션 활성화
																	//@PostAuthorize: 메서드가 끝난 뒤 실행됨. @PreAuthorize: 메서드가 시작되기 전에 실행됨
public class SecurityConfig {

	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	//해당 메서드의 리턴되는 오브젝트를 IOC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean				//'스프링 시큐리티 필터'를 '스프링 시큐리티 필터 체인'에 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {	//webMVCConfigureradapter 는 스프링 시큐리티 5.7 이후 지원 종료

        http.csrf().disable();

        http.authorizeRequests()
            .requestMatchers("/user/**").authenticated()	// authenticated: 인증만 되면 허가하도록 해 줌.antMatchers -> requestMatchers 
            .requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
            .requestMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
            .anyRequest().permitAll()
        .and()
            .formLogin()
            .loginPage("/loginForm")		//로그인 페이지 지정. 비허가 접근 404일시 /login으로 이동되게 함
      //      .usernameParameter("username2")	//html의 username을 username2로 사용하고 싶을떄 사용. loadUserByUsername의 username 파라미터랑 이름이 일치해야 한다.
            .loginProcessingUrl("/login")	//비허가 접근 404일시 /login으로 이동되게 함. "/login" 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행 (Controller에 /login 안 만들어도됨)
            .defaultSuccessUrl("/")	//로그인 완료시 이동할 페이지
        .and()				//Auth 로그인 매핑 페이지 관련 내용 추가 
        .oauth2Login()
        .loginPage("/loginForm")		
        .userInfoEndpoint()
        .userService(principalOauth2UserService);
        return http.build();
        
      //생략해도 무방 //구글 로그인이 완료된 뒤의 후처리가 필요
        //구글 로그인이 완료된 후의 처리가 필요 1. 코드받기(인증) 2. 액세스토큰(권한) 3. 사용자 프로필 정보를 가져오고 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
        //4-2.(이메일, 전화번호,이름,아이디)쇼핑몰 -> (집주소)백화점몰 -> (VIP,일반등급) 등의 추가정보를 입력해야 할 경우
        //코드 짤 필요 없음 oAuth2 라이브러리는 액세스토큰+사용자 프로필 정보를 바로 반환해줌
    }
}
