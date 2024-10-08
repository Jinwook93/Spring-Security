package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller	//view 리턴
public class IndexController {

	private UserRepository userRepository;
	
	@Autowired
	public IndexController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@ResponseBody
	@GetMapping("/test/login")
	public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
	
		System.out.println("/test/login ============");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : "+ principalDetails.getUser());	//둘 다 같은 데이터임을 확인
		
		System.out.println("userDetails : "+ userDetails.getUser());		//둘 다 같은데이터임을 확인
		
		return "세션 정보 확인하기";
	}
	
	
	@ResponseBody
	@GetMapping("/test/oauth/login")
	public String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth ) {	//DI(의존성 주입)
	
		System.out.println("/test/oauth/login ============");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication:" + oAuth2User.getAttributes());
		System.out.println("oauth2User" +oauth.getAttributes());
		return "OAuth 세션 정보 확인하기";
	}
	
	
	@GetMapping({" ","/"})
	public String index() {
		//머스테치 기본폴더 src/main/resources/
		return "index";	//뷰리졸버 설정 : templates(prefix),.mustache(suffix)생략 가능!!
	}
	
	//일반로그인을 해도 PrincipalDetails을 받을 수 있고,
	//OAuth로그인을 해도 PrincipalDetails을 받을 수 있다. 즉 따로 만들 필요가 없다.
	@GetMapping({"/user"})
	@ResponseBody
	public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails: " + principalDetails.getUser());
		//머스테치 기본폴더 src/main/resources/
		return "user";	//뷰리졸버 설정 : templates(prefix),.mustache(suffix)생략 가능!!
	}
	
	@GetMapping({"/manager"})
	@ResponseBody
	public String manager() {
		//머스테치 기본폴더 src/main/resources/
		return "manager";	//뷰리졸버 설정 : templates(prefix),.mustache(suffix)생략 가능!!
	}
	
	//스프링 시큐리티가 해당주소를 낚아챔
	@GetMapping({"/loginForm"})
	public String loginForm() {
		//머스테치 기본폴더 src/main/resources/
		return "loginForm";	//뷰리졸버 설정 : templates(prefix),.mustache(suffix)생략 가능!!
	}
	
	@GetMapping({"/joinForm"})
	public String joinForm() {
		//머스테치 기본폴더 src/main/resources/
		return "joinForm";	//뷰리졸버 설정 : templates(prefix),.mustache(suffix)생략 가능!!
	}
	
	@PostMapping("/join")
	public String join(User user) {
		user.setRole("ROLE_USER");
	
		//회원가입은 되나 비밀번호가 1234 일경우 시큐리티에 로그인이 불가.
		//왜냐하면 패스워드가 암호화가 되지 않았기 떄문이다
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user);
		return "redirect:/loginForm";
		
	}
	
	@GetMapping("/admin")
	@ResponseBody
	public String admin() {
		//머스테치 기본폴더 src/main/resources/
		return "admin";	//뷰리졸버 설정 : templates(prefix),.mustache(suffix)생략 가능!!
	}
	
//	@GetMapping({"/joinProc"})
//	@ResponseBody
//	public String joinProc() {
//		//머스테치 기본폴더 src/main/resources/
//		return "회원가입 완료됨";	//뷰리졸버 설정 : templates(prefix),.mustache(suffix)생략 가능!!
//	}
	
	@Secured("ROLE_ADMIN")			//하나의 대상 권한 설정할 떄 쓰임
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")	//메서드 실행 직전에 실행됨. 여러개 대상 권한 설정할 때 쓰임
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터";
	}
}
