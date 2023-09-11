package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.State;

import java.util.List;


public interface StateRepository extends JpaRepository<State, Long>{
	State findByStateName(String stateName);
}
