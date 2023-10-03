package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.CrowdCategory;
import com.megpbr.data.entity.MasterCategory;

public interface CrowdCategoryRepository extends JpaRepository<CrowdCategory, Long>{
	List<MasterCategory> findById(long id);
}
