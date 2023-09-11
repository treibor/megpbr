package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.villages.VillageAnnexure1;
import com.megpbr.data.entity.villages.VillageAnnexure2;

import java.util.List;

public interface VillageAnnexure2Repository extends JpaRepository<VillageAnnexure2, Long>{
	List<VillageAnnexure2> findByVillage(Village village);
}
