package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.villages.VillageAnnexure1;
import com.megpbr.data.entity.villages.VillageAnnexure3;
import com.megpbr.data.entity.villages.VillageAnnexure4;

import java.util.List;

public interface VillageAnnexure4Repository extends JpaRepository<VillageAnnexure4, Long>{
	List<VillageAnnexure4> findByVillage(Village village);
}
