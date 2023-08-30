package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.application.megpbr.data.entity.UserLogin;


public interface UserRepository extends JpaRepository<UserLogin, Long>{
	UserLogin findByUserName(String username);
	
}
