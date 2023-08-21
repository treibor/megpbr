package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.villages.VillageAnnexure1;
import com.application.megpbr.data.entity.villages.VillageAnnexure2;

public interface VillageAnnexure2Repository extends JpaRepository<VillageAnnexure2, Long>{
	List<VillageAnnexure2> findByVillage(Village village);
}
