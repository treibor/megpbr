package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.villages.VillageAnnexure1;
import com.megpbr.data.entity.villages.VillageAnnexure3;

import java.util.List;

public interface VillageAnnexure3Repository extends JpaRepository<VillageAnnexure3, Long>{
	List<VillageAnnexure3> findByVillage(Village village);
}
