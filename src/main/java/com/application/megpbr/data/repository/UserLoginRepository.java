package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.megpbr.data.entity.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long>{
	UserLogin findByUserName(String user);
}
