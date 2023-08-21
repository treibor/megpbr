package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.villages.VillageAnnexure1;

public interface VillageAnnexure1Repository extends JpaRepository<VillageAnnexure1, Long>{
	List<VillageAnnexure1> findByVillage(Village village);
}
