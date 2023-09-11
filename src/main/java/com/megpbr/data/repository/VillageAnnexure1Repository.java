package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.villages.VillageAnnexure1;

import java.util.List;

public interface VillageAnnexure1Repository extends JpaRepository<VillageAnnexure1, Long>{
	List<VillageAnnexure1> findByVillage(Village village);
}
