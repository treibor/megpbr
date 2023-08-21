package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.villages.VillageAnnexure1;
import com.application.megpbr.data.entity.villages.VillageAnnexure3;
import com.application.megpbr.data.entity.villages.VillageAnnexure4;

public interface VillageAnnexure4Repository extends JpaRepository<VillageAnnexure4, Long>{
	List<VillageAnnexure4> findByVillage(Village village);
}
