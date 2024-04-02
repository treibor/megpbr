package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.megpbr.data.entity.District;
import com.megpbr.data.entity.State;

import java.util.List;


public interface DistrictRepository extends JpaRepository<District, Long>{
	List <District> findByState(State state); 
	
}
