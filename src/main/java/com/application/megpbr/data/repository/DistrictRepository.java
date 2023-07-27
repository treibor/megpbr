package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.State;

import java.util.List;


public interface DistrictRepository extends JpaRepository<District, Long>{
	List <District> findByState(State state); 
}
