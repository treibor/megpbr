package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.CrowdCategory;
import com.megpbr.data.entity.CrowdFormat;

public interface CrowdFormatRepository extends JpaRepository<CrowdFormat, Long> {
	List<CrowdFormat> findByCategory(CrowdCategory category);
}
