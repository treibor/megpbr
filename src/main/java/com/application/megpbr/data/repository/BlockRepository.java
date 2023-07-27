package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;

import java.util.List;


public interface BlockRepository extends JpaRepository<Block, Long>{
	List<Block> findByDistrict(District district);
}
