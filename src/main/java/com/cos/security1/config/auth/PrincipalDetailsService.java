package com.cos.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

//시큐리티 설정에서 loginProcessingUrl("/login")
// '/login' 요청이 오면 UserDetailsService 타입으로 IoC되어있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService{

	
	@Autowired
	private UserRepository userRepository;
	
	//시큐리티 session(내부 Authentication(내부 UserDetails))
	//함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);	//기본적인 CRUD 함수만 제공하므로 userRepository에서 findByUsername을
																			//추가해야한다
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		else {
			return null;
		}
	}

}
