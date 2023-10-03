package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.CrowdCategory;

import com.megpbr.data.entity.CrowdType;

public interface CrowdTypeRepository extends JpaRepository<CrowdType, Long>{
	List<CrowdType> findByCategory(CrowdCategory category);
	
}
