package com.cos.security1.config.provider;

public interface OAuth2UserInfo {

	String getProviderId();		//primary key의 ID
	String getProvider();		//구글 or 페이스북
	String getEmail();
	String getName();
}
