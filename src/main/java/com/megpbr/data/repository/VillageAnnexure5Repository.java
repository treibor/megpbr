package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.villages.VillageAnnexure1;
import com.megpbr.data.entity.villages.VillageAnnexure3;
import com.megpbr.data.entity.villages.VillageAnnexure4;
import com.megpbr.data.entity.villages.VillageAnnexure5;

import java.util.List;

public interface VillageAnnexure5Repository extends JpaRepository<VillageAnnexure5, Long>{
	List<VillageAnnexure5> findByVillage(Village village);
}
