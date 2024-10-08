package com.cos.security1.config.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

	private Map<String, Object> attributes;	//getAttributes()
	
	
	//{id=pujpt5IXHu875Nj2nLKH0YF_8rTnlsTEDVjkEglfJ_E, email=jinucklee93@naver.com, name=이진욱}
	public NaverUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return (String) attributes.get("id");
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

}
