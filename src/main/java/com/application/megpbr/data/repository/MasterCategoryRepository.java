package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.megpbr.data.entity.MasterCategory;
import java.util.List;


public interface MasterCategoryRepository extends JpaRepository<MasterCategory, Long>{
	MasterCategory  findById(long id);
}
