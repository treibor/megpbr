package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.megpbr.data.entity.UserLogin;


public interface UserRepository extends JpaRepository<UserLogin, Long>{
	UserLogin findByUserNameAndEnabled(String username, boolean enabled);
	UserLogin findByUserName(String username);
	UserLogin findByEmailAndEnabled(String email, boolean enabled);
	List <UserLogin> findByUserNameNot(String username);
}
