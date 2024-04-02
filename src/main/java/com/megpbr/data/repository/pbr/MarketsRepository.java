package com.megpbr.data.repository.pbr;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterApproval;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Markets;

public interface MarketsRepository extends JpaRepository<Markets, Long>{
	long countByFormat(MasterFormat format);
	Markets findTopByName(String fauna);
	List<Markets> findByFormatOrderByName(MasterFormat format);
	List<Markets> findByStateAndFormatAndMaster(State state, MasterFormat format, boolean master);
	List<Markets>  findByFormatAndVillage(MasterFormat format, Village village);
	List<Markets>  findByVillage(Village village);
	List<Markets>  findByVillageAndApproved(Village village, MasterApproval approved);
	//Markets Count
	@Query("select  count(*) from Markets c join c.village d join d.block e join e.district f where c.master=:master and district=:district " )
	int getMarketsCount(@Param("district") District district, @Param("master") boolean master);
	
	//Master Data
	@Query("select c from Markets c where c.state=:state and c.master=true and c.format= :format and (lower(c.name) like lower(concat('%', :searchTerm, '%'))or lower(c.frequency) like lower(concat('%', :searchTerm, '%'))or lower(c.animalType) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Markets> search(@Param("state") State state,@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	// District Data
	@Query("select  c from Markets c join c.village d join d.block e join e.district f where c.format= :format and  (district=:district or :district is null) and(lower(c.name) like lower(concat('%', :searchTerm, '%'))or lower(c.frequency) like lower(concat('%', :searchTerm, '%'))or lower(c.animalType) like lower(concat('%', :searchTerm, '%')))")
	List<Markets> searchFilterMarketsData(@Param("district") District district, @Param("searchTerm") String searchTerm,
			@Param("format") MasterFormat format);

	//  Count By Year
	@Query("select  count(*) from Markets c join c.village d join d.block e join e.district f where c.master =:master and c.enteredOn between :sdate and :edate")
	int getMarketsCountYearly(@Param("master") boolean master, @Param("sdate") LocalDateTime sdate,
			@Param("edate") LocalDateTime edate);

	// Block Data
	@Query("select  c from Markets c join c.village d join d.block e  where c.format= :format and  (block=:block or :block is null) and(lower(c.name) like lower(concat('%', :searchTerm, '%'))or lower(c.frequency) like lower(concat('%', :searchTerm, '%'))or lower(c.animalType) like lower(concat('%', :searchTerm, '%')))")
	List<Markets> searchFilterMarketsData(@Param("block") Block block, @Param("searchTerm") String searchTerm,
			@Param("format") MasterFormat format);

	// Village Data
	@Query("select c from Markets c join c.village d where  c.format= :format  and (village=:village or :village is null )  and(lower(c.name) like lower(concat('%', :searchTerm, '%'))or lower(c.frequency) like lower(concat('%', :searchTerm, '%'))or lower(c.animalType) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Markets> searchFilterMarketsData(@Param("village") Village village, @Param("searchTerm") String searchTerm,
			@Param("format") MasterFormat format);

	@Query("select c from Markets c where  c.format= :format  and (c.village=:village or :village is null ) and c.master=:master and(lower(c.name) like lower(concat('%', :searchTerm, '%'))or lower(c.frequency) like lower(concat('%', :searchTerm, '%'))or lower(c.animalType) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Markets> searchFilterMarketsData(@Param("village") Village village, @Param("searchTerm") String searchTerm,
			@Param("format") MasterFormat format, @Param("master") boolean master);
	
	
	
	//Populating Comboboxes for AutoComplete
	@Query("select distinct c.name from Markets c  where c.format= :format and c.name<>'' and c.name is not null")
	List<String> name(@Param("format") MasterFormat format);
	@Query("select distinct c.frequency from Markets c  where c.format= :format and c.frequency<>'' and c.frequency is not null")
	List<String> frequency(@Param("format") MasterFormat format);
	@Query("select distinct c.month from Markets c  where c.format= :format and c.month<>'' and c.month is not null")
	List<String> month(@Param("format") MasterFormat format);
	@Query("select distinct c.day from Markets c  where c.format= :format and c.day<>'' and c.day is not null")
	List<String> day(@Param("format") MasterFormat format);
	@Query("select distinct c.animalType from Markets c  where c.format= :format and c.animalType<>'' and c.animalType is not null")
	List<String> animalType(@Param("format") MasterFormat format);
	@Query("select distinct c.transactions from Markets c  where c.format= :format and c.transactions<>'' and c.transactions is not null")
	List<String> transactions(@Param("format") MasterFormat format);
	@Query("select distinct c.placesFrom from Markets c  where c.format= :format and c.placesFrom<>'' and c.placesFrom is not null")
	List<String> placesFrom(@Param("format") MasterFormat format);
	@Query("select distinct c.placesTo from Markets c  where c.format= :format and c.placesTo<>'' and c.placesTo is not null")
	List<String> placesTo(@Param("format") MasterFormat format);
	@Query("select distinct c.fishLocation from Markets c  where c.format= :format and c.fishLocation<>'' and c.fishLocation is not null")
	List<String> fishLocation(@Param("format") MasterFormat format);
	@Query("select distinct c.fishType from Markets c  where c.format= :format and c.fishType<>'' and c.fishType is not null")
	List<String> fishType(@Param("format") MasterFormat format);
	@Query("select distinct c.fishSource from Markets c  where c.format= :format and c.fishSource<>'' and c.fishSource is not null")
	List<String> fishSource(@Param("format") MasterFormat format);
}
