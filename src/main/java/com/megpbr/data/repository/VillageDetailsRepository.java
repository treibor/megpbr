package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.villages.VillageDetails;


public interface VillageDetailsRepository extends JpaRepository<VillageDetails, Long> {
	
	List <VillageDetails> findByVillage(Village village);
	
	@Query("select  count(*) from VillageDetails c ")
	int getVillageDetailsCount();
	
	@Query("select  c from VillageDetails c join c.village d join d.block e join e.district f where  (c.village=:village or :village is null ) and (d.block=:block or :block is null) and (district=:district or :district is null)  and(lower(d.villageName) like lower(concat('%', :searchTerm, '%')))" )
	List<VillageDetails> getFilterVillageDetails( @Param("village") Village village, @Param ("block") Block block, @Param("district") District district, @Param("searchTerm") String searchTerm);
	//Village Data
	@Query("select  c from VillageDetails c join c.village d where  (village=:village or :village is null )  and(lower(d.villageName) like lower(concat('%', :searchTerm, '%')))" )
	List<VillageDetails> getFilterVillageDetails( @Param("village") Village village,  @Param("searchTerm") String searchTerm);
	
	//Block Data
	@Query("select  c from VillageDetails c join c.village d join d.block e  where (block=:block or :block is null) and (lower(d.villageName) like lower(concat('%', :searchTerm, '%')))" )
	List<VillageDetails> getFilterVillageDetails(@Param ("block")Block block, @Param("searchTerm") String searchTerm);
	//District Data
	@Query("select  c from VillageDetails c join c.village d join d.block e join e.district f where  (district=:district or :district is null)  and(lower(d.villageName) like lower(concat('%', :searchTerm, '%')))" )
	List<VillageDetails> getFilterVillageDetails(District district, @Param("searchTerm") String searchTerm);
	
	@Query("select  c from VillageDetails c join c.village d join d.block e join e.district f where  (district=:district or :district is null)" )
	List<VillageDetails> getFilterVillageDetails(District district);
}
