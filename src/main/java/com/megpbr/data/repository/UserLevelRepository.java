package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.UserLoginLevel;

public interface UserLevelRepository extends JpaRepository<UserLoginLevel, Long>{
	List<UserLoginLevel> findByIdGreaterThan(long a);
}
