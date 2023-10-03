package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.MasterCategory;
import com.megpbr.data.entity.MasterFormat;

public interface MasterFormatRepository extends JpaRepository<MasterFormat, Long>{
	MasterFormat findBfindByformat(int format);
	List<MasterFormat> findByCategory(MasterCategory category);
}
