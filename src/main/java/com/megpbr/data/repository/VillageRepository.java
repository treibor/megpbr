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

public interface VillageRepository extends JpaRepository<Village, Long> {
	List<Village> findByBlock(Block block);

	List<Village> findById(long id);

	Village findByVillageCode(long id);

	List<Village> findByBlockAndInUse(Block block, boolean inUse);
	// List<Village> findByVillagedetailsIn(List<VillageDetails> villagedetails);

	@Query("select c from Village c  where c.villagedetails Not in :villagedetails")
	List<String> VillageList(@Param("villagedetails") List<VillageDetails> villagedetails);

	@Query("SELECT vv AS village, bb AS block, dd AS district, ss AS state, COALESCE(x.cnt, 0) + COALESCE(s.cnts, 0) + COALESCE(m.cntm, 0) AS sum "
			+ "FROM Village vv " + "JOIN vv.block bb " + "JOIN bb.district dd " + "JOIN dd.state ss "
			+ "LEFT OUTER JOIN (SELECT x.village.villageCode AS villageCode, COUNT(*) AS cnt FROM Crops x GROUP BY x.village.villageCode) x "
			+ "ON vv.villageCode = x.villageCode "
			+ "LEFT OUTER JOIN (SELECT s.village.villageCode AS villageCode, COUNT(*) AS cnts FROM Scapes s GROUP BY s.village.villageCode) s "
			+ "ON vv.villageCode = s.villageCode "
			+ "LEFT OUTER JOIN (SELECT m.village.villageCode AS villageCode, COUNT(*) AS cntm FROM Markets m GROUP BY m.village.villageCode) m "
			+ "ON vv.villageCode = m.villageCode " + "ORDER BY vv.villageName")
	List<Object[]> villageCount();

	@Query("SELECT b.blockName, COALESCE(vx.cropsSum, 0) + COALESCE(vy.scapesSum, 0) + COALESCE(vz.marketsSum, 0) AS Sum " +
		       "FROM Block b " +
		       "LEFT JOIN (SELECT v.block.blockCode AS blockCode, SUM(COALESCE(c.cropsCount, 0)) AS cropsSum " +
		                  "FROM Village v " +
		                  "LEFT JOIN (SELECT c.village.villageCode AS villageCode, COUNT(*) AS cropsCount " +
		                             "FROM Crops c " +
		                             "GROUP BY c.village.villageCode) c " +
		                  "ON v.villageCode = c.villageCode " +
		                  "GROUP BY v.block.blockCode) vx " +
		       "ON b.blockCode = vx.blockCode " +
		       "LEFT JOIN (SELECT v.block.blockCode AS blockCode, SUM(COALESCE(s.scapesCount, 0)) AS scapesSum " +
		                  "FROM Village v " +
		                  "LEFT JOIN (SELECT s.village.villageCode AS villageCode, COUNT(*) AS scapesCount " +
		                             "FROM Scapes s " +
		                             "GROUP BY s.village.villageCode) s " +
		                  "ON v.villageCode = s.villageCode " +
		                  "GROUP BY v.block.blockCode) vy " +
		       "ON b.blockCode = vy.blockCode " +
		       "LEFT JOIN (SELECT v.block.blockCode AS blockCode, SUM(COALESCE(m.marketsCount, 0)) AS marketsSum " +
		                  "FROM Village v " +
		                  "LEFT JOIN (SELECT m.village.villageCode AS villageCode, COUNT(*) AS marketsCount " +
		                             "FROM Markets m " +
		                             "GROUP BY m.village.villageCode) m " +
		                  "ON v.villageCode = m.villageCode " +
		                  "GROUP BY v.block.blockCode) vz " +
		       "ON b.blockCode = vz.blockCode " +
		       "ORDER BY b.blockName")
		List<Object[]> blockCount();

	@Query("SELECT d.districtName AS districtName, "
			+ "COALESCE(SUM(COALESCE(vx.cropsSum, 0)), 0) + COALESCE(SUM(COALESCE(vy.scapesSum, 0)), 0) + COALESCE(SUM(COALESCE(vz.marketsSum, 0)), 0) AS Sum "
			+ "FROM District d "
			+ "LEFT JOIN (SELECT b.district.districtCode AS districtCode, SUM(COALESCE(c.cropsCount, 0)) AS cropsSum "
			+ "FROM Block b " + "LEFT JOIN (SELECT v.block.blockCode AS blockCode, COUNT(*) AS cropsCount "
			+ "FROM Crops c " + "JOIN c.village v " + "GROUP BY v.block.blockCode) c " + "ON b.blockCode = c.blockCode "
			+ "GROUP BY b.district.districtCode) vx " + "ON d.districtCode = vx.districtCode "
			+ "LEFT JOIN (SELECT b.district.districtCode AS districtCode, SUM(COALESCE(s.scapesCount, 0)) AS scapesSum "
			+ "FROM Block b " + "LEFT JOIN (SELECT v.block.blockCode AS blockCode, COUNT(*) AS scapesCount "
			+ "FROM Scapes s " + "JOIN s.village v " + "GROUP BY v.block.blockCode) s "
			+ "ON b.blockCode = s.blockCode " + "GROUP BY b.district.districtCode) vy "
			+ "ON d.districtCode = vy.districtCode "
			+ "LEFT JOIN (SELECT b.district.districtCode AS districtCode, SUM(COALESCE(m.marketsCount, 0)) AS marketsSum "
			+ "FROM Block b " + "LEFT JOIN (SELECT v.block.blockCode AS blockCode, COUNT(*) AS marketsCount "
			+ "FROM Markets m " + "JOIN m.village v " + "GROUP BY v.block.blockCode) m "
			+ "ON b.blockCode = m.blockCode " + "GROUP BY b.district.districtCode) vz "
			+ "ON d.districtCode = vz.districtCode " + "GROUP BY d.districtName")
	List<Object[]> districtCount();

}
