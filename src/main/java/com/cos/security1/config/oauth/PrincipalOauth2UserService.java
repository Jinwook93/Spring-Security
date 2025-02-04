package com.cos.security1.config.oauth;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.provider.FacebookUserInfo;
import com.cos.security1.config.provider.GoogleUserInfo;
import com.cos.security1.config.provider.NaverUserInfo;
import com.cos.security1.config.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;



@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	

  //private BCryptPasswordEncoder bCryptPasswordEncoder;  // 순환참조 에러남!!


	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private UserRepository userRepository;
	
	//함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override	//구글로부터 받은 userRequest 데이터에 대해 후처리할 메서드
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
	
//		System.out.println("userRequest: " + userRequest); 
		System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); 
		System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue()); 
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> Code를 리턴 (OAuth - Client 라이브러리) -> AccessToken 요청
		// userRequest 정보 -> 회원 프로필 받아야함(loadUser 함수 호출) -> 구글로부터 회원프로필 받아준다.
				System.out.println("getAttributes: " + oauth2User.getAttributes()); 
				
		//회원가입을 강제로 진행할 예정
				
				OAuth2UserInfo oAuth2UserInfo=null;
				
				if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
					oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
				}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
					oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
				}else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
					oAuth2UserInfo = new NaverUserInfo((Map<String, Object>)oauth2User.getAttributes().get("response"));
				}else {
					System.out.println("우리는 구글,페이스북 로그인만 지원합니다");
				}
				
				String provider = oAuth2UserInfo.getProvider(); //google,facebook
				String providerId = oAuth2UserInfo.getProviderId(); 
				String username = provider+"_"+providerId;	
				String email = oAuth2UserInfo.getEmail(); 
				String password = bCryptPasswordEncoder.encode("겟인데어");
				String role = "ROLE_USER";
				
				User userEntity =  userRepository.findByUsername(username);
				
				if(userEntity == null) {
					System.out.println("최초 Oauth 로그인입니다");
					userEntity = User.builder()
							.username(username)
							.password(password)
							.email(email)
							.role(role)
							.provider(provider)
							.providerId(providerId)
							.build();
					userRepository.save(userEntity);
				}else {
				System.out.println("로그인을 이미 한 적이 있습니다 당신은 회원가입이 되어 있습니다.");
				}
				
		//return super.loadUser(userRequest); 수정 전
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());		
	}
}








//@Service
//public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
//	
//
//  //private BCryptPasswordEncoder bCryptPasswordEncoder;  // 순환참조 에러남!!
//
//
//	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//	@Autowired
//	private UserRepository userRepository;
//	
//	//함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
//	@Override	//구글로부터 받은 userRequest 데이터에 대해 후처리할 메서드
//	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		
//		
////		System.out.println("userRequest: " + userRequest); 
//		System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); 
//		System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue()); 
//		
//		OAuth2User oauth2User = super.loadUser(userRequest);
//		// 구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> Code를 리턴 (OAuth - Client 라이브러리) -> AccessToken 요청
//		// userRequest 정보 -> 회원 프로필 받아야함(loadUser 함수 호출) -> 구글로부터 회원프로필 받아준다.
//				System.out.println("getAttributes: " + oauth2User.getAttributes()); 
//				
//		//회원가입을 강제로 진행할 예정
//				
//				String provider = userRequest.getClientRegistration().getRegistrationId(); //google
//				String providerId = oauth2User.getAttribute("sub");	//구글일 떄에는 sub가 id값이나, 페이스북의 경우 id로 해야한다
//				String username = provider+"_"+providerId;	//google_117787094047736878330
//				String email = oauth2User.getAttribute("email");
//				String password = bCryptPasswordEncoder.encode("겟인데어");
//				String role = "ROLE_USER";
//				
//				User userEntity =  userRepository.findByUsername(username);
//				
//				if(userEntity == null) {
//					System.out.println("구글 로그인이 최초입니다");
//					userEntity = User.builder()
//							.username(username)
//							.password(password)
//							.email(email)
//							.role(role)
//							.provider(provider)
//							.providerId(providerId)
//							.build();
//					userRepository.save(userEntity);
//				}else {
//				System.out.println("구글 로그인을 이미 한 적이 있습니다 당신은 회원가입이 되어 있습니다.");
//				}
//		
//				
//		//return super.loadUser(userRequest); 수정 전
//		return new PrincipalDetails(userEntity, oauth2User.getAttributes());		
//	}
//}
