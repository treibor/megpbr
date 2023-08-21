package com.application.megpbr.data.repository.pbr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.entity.pbr.Scapes;

public interface ScapeRepository extends JpaRepository<Scapes, Long>{
	
	Scapes findTopByFaunaPopulation(String fauna);
	List<Scapes> findByFormatOrderByFaunaPopulation(MasterFormat format);
	
	@Query("select c from Scapes c where  c.format= :format  and (c.village=:village or :village is null )  and(lower(c.faunaPopulation) like lower(concat('%', :searchTerm, '%'))or lower(c.floraOccupation) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Scapes> searchFilterScapesData(@Param("village") Village village,@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	@Query("select c from Scapes c where  c.format= :format  and (c.village=:village or :village is null ) and c.master=:master and(lower(c.faunaPopulation) like lower(concat('%', :searchTerm, '%'))or lower(c.floraOccupation) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Scapes> searchFilterScapesData(@Param("village") Village village,@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format, @Param("master") boolean master);
	
	
	
	
	//Populating Comboboxes for AutoComplete
	@Query("select distinct c.faunaPopulation from Scapes c  where c.format= :format and c.faunaPopulation<>'' and c.faunaPopulation is not null")
	List<String> fauna(@Param("format") MasterFormat format);
	@Query("select distinct c.floraOccupation from Scapes c  where c.format= :format and c.floraOccupation<>'' and c.floraOccupation is not null")
	List<String> flora(@Param("format") MasterFormat format);
	@Query("select distinct c.typeAgriOccupation from Scapes c  where c.format= :format and c.typeAgriOccupation<>'' and c.typeAgriOccupation is not null")
	List<String> type(@Param("format") MasterFormat format);
	@Query("select distinct c.landscape from Scapes c  where c.format= :format and c.landscape<>'' and c.landscape is not null")
	List<String> landscape(@Param("format") MasterFormat format);
	@Query("select distinct c.subLandscape from Scapes c  where c.format= :format and c.subLandscape<>'' and c.subLandscape is not null")
	List<String> sublandscape(@Param("format") MasterFormat format);
	@Query("select distinct c.ownerHouse from Scapes c  where c.format= :format and c.ownerHouse<>'' and c.ownerHouse is not null")
	List<String> owner(@Param("format") MasterFormat format);
	@Query("select distinct c.userGroups from Scapes c  where c.format= :format and c.userGroups<>'' and c.userGroups is not null")
	List<String> usergroups(@Param("format") MasterFormat format);
	@Query("select distinct c.management from Scapes c  where c.format= :format and c.management<>'' and c.userGroups is not null")
	List<String> management(@Param("format") MasterFormat format);
	@Query("select distinct c.socialCommunity from Scapes c  where c.format= :format and c.socialCommunity<>'' and c.socialCommunity is not null")
	List<String> social(@Param("format") MasterFormat format);
	@Query("select distinct c.generalResources from Scapes c  where c.format= :format and c.generalResources<>'' and c.generalResources is not null")
	List<String> general(@Param("format") MasterFormat format);
	@Query("select distinct c.associatedTk from Scapes c  where c.format= :format and c.associatedTk<>'' and c.associatedTk is not null")
	List<String> associatedtk(@Param("format") MasterFormat format);
	@Query("select distinct c.fallow from Scapes c  where c.format= :format and c.fallow<>'' and c.fallow is not null")
	List<String> fallow(@Param("format") MasterFormat format);
	@Query("select distinct c.wetLand from Scapes c  where c.format= :format and c.wetLand<>'' and c.wetLand is not null")
	List<String> wetLand(@Param("format") MasterFormat format);
	@Query("select distinct c.forest from Scapes c  where c.format= :format and c.forest<>'' and c.forest is not null")
	List<String> forest(@Param("format") MasterFormat format);
}
