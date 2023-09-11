package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;

import java.util.List;


public interface BlockRepository extends JpaRepository<Block, Long>{
	List<Block> findByDistrict(District district);
}
