package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.villages.VillageDetails;

import java.util.List;


public interface VillageRepository extends JpaRepository<Village, Long>{
	List<Village> findByBlock(Block block);
	List<Village> findById(long id);
	Village findByVillageCode(long id);
	List<Village> findByBlockAndInUse(Block block, boolean inUse);
	//List<Village> findByVillagedetailsIn(List<VillageDetails> villagedetails);
	
	@Query("select c from Village c  where c.villagedetails Not in :villagedetails")
	List<String> VillageList(@Param("villagedetails") List<VillageDetails> villagedetails);
}
