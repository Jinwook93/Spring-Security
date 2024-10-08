package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;


//CRUD 함수를 JPARepository가 들고 있음

//@Repository 어노테이션이 없어도 IOC됨 . JPARepository를 상속했기 떄문이다.
public interface UserRepository extends JpaRepository<User, Integer>{
	
	//findBy규칙 -> Username 문법
	//select * from user where username =1?
	public User findByUsername(String username);	//JPA Query Method
}
