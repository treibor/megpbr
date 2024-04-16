package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
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
	
	@Query("SELECT vv AS village, bb AS block, dd AS district, ss AS state, COALESCE(x.cnt, 0) + COALESCE(s.cnts, 0) + COALESCE(m.cntm, 0) AS sum "
		    + "FROM Village vv "
		    + "JOIN vv.block bb "
		    + "JOIN bb.district dd "
		    + "JOIN dd.state ss "
		    + "LEFT OUTER JOIN (SELECT x.village.villageCode AS villageCode, COUNT(*) AS cnt FROM Crops x GROUP BY x.village.villageCode) x "
		    + "ON vv.villageCode = x.villageCode "
		    + "LEFT OUTER JOIN (SELECT s.village.villageCode AS villageCode, COUNT(*) AS cnts FROM Scapes s GROUP BY s.village.villageCode) s "
		    + "ON vv.villageCode = s.villageCode "
		    + "LEFT OUTER JOIN (SELECT m.village.villageCode AS villageCode, COUNT(*) AS cntm FROM Markets m GROUP BY m.village.villageCode) m "
		    + "ON vv.villageCode = m.villageCode "
		    + "ORDER BY vv.villageName")

	List<Object[]> villageCount();

	

}
