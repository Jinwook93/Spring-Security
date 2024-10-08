package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

// 시큐리티가 /login 주소가 오면 낚아채서 로그인을 진행시킴

// 로그인이 진행 완료가 되면 시큐리티 session을 만듬 (Security ContextHolder에 세션정보를 저장시켜야함)

// 오브젝트 => Authentication 타입 객체
//Authentication 안에 User정보가 있어야 함

// User 오브젝트의 타입 => UserDetails 타입 객체

// Security Session => Authentication 객체 ->  UserDetails(PrincipalDetails) 

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{


	private User user; 	//컴포지션
	private Map<String,Object> attributes;
	
	
	//일반 로그인 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}


	//oAuth 로그인 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	
	//해당 User의 권한을 return하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {		//계정이 만료되었나요? true(아니요)
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {			//계정이 잠겼니?
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {		// 같은 정보를 너무 오래 사용한거 아니니?
		return true;
	}

	@Override
	public boolean isEnabled() { // 사용 사례 : 1년 이상 접속 안할 시 휴면계정일 경우 사용
		
		//휴면 계정 예 
		// user.getLoginDate() <- 로그인 시간
		// 현재시간- 로그인시간 추출해서 해당 값이 1년을 초과할경우 false로 바꿈
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}
