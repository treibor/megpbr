package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.villages.VillageAnnexure1;
import com.application.megpbr.data.entity.villages.VillageAnnexure3;

public interface VillageAnnexure3Repository extends JpaRepository<VillageAnnexure3, Long>{
	List<VillageAnnexure3> findByVillage(Village village);
}
