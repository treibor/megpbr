package com.application.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.Village;
import java.util.List;


public interface VillageRepository extends JpaRepository<Village, Long>{
	List<Village> findByBlock(Block block);
	List<Village> findByBlockAndInUse(Block block, boolean inUse);
	//List<Village> findByVillagedetailsIn(List<VillageDetails> villagedetails);
}
