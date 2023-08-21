package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.villages.VillageAnnexure1;
import com.application.megpbr.data.entity.villages.VillageAnnexure3;
import com.application.megpbr.data.entity.villages.VillageAnnexure4;
import com.application.megpbr.data.entity.villages.VillageAnnexure5;

public interface VillageAnnexure5Repository extends JpaRepository<VillageAnnexure5, Long>{
	List<VillageAnnexure5> findByVillage(Village village);
}
