package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.MasterFormat;

public interface MasterFormatRepository extends JpaRepository<MasterFormat, Long>{
	MasterFormat findBfindByformat(int format);
}