package com.application.megpbr.data.repository.pbr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.pbr.Crops;
import java.util.List;


public interface CropsRepository extends JpaRepository<Crops, Long>{
	Crops findTopByScientificName(String scientificName);
	List<Crops> findByFormatOrderByScientificName(MasterFormat format);
	List<Crops> findAllByOrderByHabitat();
	List<Crops> findByFormat(MasterFormat format);
	List<Crops> findDistinctByFormat(MasterFormat format);
	@Query("select c from Crops c where  c.format= :format and (lower(c.scientificName) like lower(concat('%', :searchTerm, '%'))or lower(c.localName) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Crops> search(@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	@Query("select c from Crops c where  c.format= :format and c.village is null and(lower(c.scientificName) like lower(concat('%', :searchTerm, '%'))or lower(c.localName) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Crops> searchMasterData(@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	@Query("select c from Crops c where  c.format= :format and c.village is not null and(lower(c.scientificName) like lower(concat('%', :searchTerm, '%'))or lower(c.localName) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Crops> searchVillageData(@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	//List<Work> search(@Param("searchTerm") String searchTerm, @Param("district") District district);
	
	
	
	//Populating Comboboxes for AutoComplete
	@Query("select distinct c.scientificName from Crops c  where c.format= :format and c.scientificName<>'' and c.scientificName is not null")
	List<String> scientificNames(@Param("format") MasterFormat format);
	@Query("select distinct c.type from Crops c  where c.format= :format and c.type<>'' and  c.type is not null")
	List<String> type(@Param("format") MasterFormat format);
	@Query("select distinct c.habitat from Crops c  where c.format= :format and c.habitat<>'' and c.habitat is not null")
	List<String> habitat(@Param("format") MasterFormat format);
	@Query("select distinct c.localName from Crops c  where c.format= :format and c.localName<>'' and c.localName is not null")
	List<String> localNames(@Param("format") MasterFormat format);
	@Query("select distinct c.variety from Crops c  where c.format= :format and c.variety<>'' and c.variety is not null")
	List<String> variety(@Param("format") MasterFormat format);
	@Query("select distinct c.source from Crops c  where c.format= :format and c.source<>'' and c.source is not null")
	List<String> source(@Param("format") MasterFormat format);
	@Query("select distinct c.source from Crops c  where c.format= :format and c.associatedTk<>'' and c.associatedTk is not null")
	List<String> associatedtk(@Param("format") MasterFormat format);
	@Query("select distinct c.source from Crops c  where c.format= :format and c.knowledgeHolder<>'' and c.knowledgeHolder is not null")
	List<String> knowledgeHolder(@Param("format") MasterFormat format);
	@Query("select distinct c.source from Crops c  where c.format= :format and c.uses<>'' and c.uses is not null")
	List<String> uses(@Param("format") MasterFormat format);
	@Query("select distinct c.source from Crops c  where c.format= :format and c.partsUsed<>'' and c.partsUsed is not null")
	List<String> partsUsed(@Param("format") MasterFormat format);
}
