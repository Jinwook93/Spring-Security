package com.cos.security1.model;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import com.cos.security1.repository.UserRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String email;
	private String role; //ROLE_USER, ROLE_ADMIN
	
	
	private String provider;	//auth 로그인 추가
	private String providerId;  //auth 로그인 추가
	@CreationTimestamp			//생성 시간이 자동으로 만들어진다
	private Timestamp createDate;	//sql.timestamp
	
		//ex)휴면 계정 코드 
	//private Timestamp loginDate;	//sql.timestamp
	
	@Builder
	public User(String username, String password, String email, String role, String provider, String providerId,
			Timestamp createDate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.createDate = createDate;
	}


	
}
