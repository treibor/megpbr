package com.application.megpbr.data.repository.pbr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import java.util.List;


public interface CropsRepository extends JpaRepository<Crops, Long>{
	Crops findTopByScientificName(String scientificName);
	List<Crops> findByFormatOrderByScientificName(MasterFormat format);
	List<Crops> findByFormatAndMaster(MasterFormat format, boolean master);
	List<Crops> findAllByOrderByHabitat();
	List<Crops> findByFormat(MasterFormat format);
	List<Crops> findDistinctByFormat(MasterFormat format);
	@Query("select c from Crops c where c.master=true and c.format= :format and (lower(c.scientificName) like lower(concat('%', :searchTerm, '%'))or lower(c.localName) like lower(concat('%', :searchTerm, '%'))or lower(c.type) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Crops> search(@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	
	
	//District Data
	@Query("select  c from Crops c join c.village d join d.block e join e.district f where c.format= :format and  (district=:district or :district is null) and(lower(c.scientificName) like lower(concat('%', :searchTerm, '%'))or lower(c.localName) like lower(concat('%', :searchTerm, '%'))or lower(c.type) like lower(concat('%', :searchTerm, '%')))" )
	List<Crops> searchFilterCropsData(@Param("district") District district,@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	//Block Data
	@Query("select  c from Crops c join c.village d join d.block e  where c.format= :format and  (block=:block or :block is null) and(lower(c.scientificName) like lower(concat('%', :searchTerm, '%'))or lower(c.localName) like lower(concat('%', :searchTerm, '%'))or lower(c.type) like lower(concat('%', :searchTerm, '%')))" )
	List<Crops> searchFilterCropsData(@Param("block") Block block,@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	//Village Data
	@Query("select c from Crops c join c.village d where  c.format= :format  and (village=:village or :village is null )  and(lower(c.scientificName) like lower(concat('%', :searchTerm, '%'))or lower(c.localName) like lower(concat('%', :searchTerm, '%'))or lower(c.type) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Crops> searchFilterCropsData(@Param("village") Village village,@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format);
	
	
	@Query("select c from Crops c where  c.format= :format  and (c.village=:village or :village is null ) and c.master=:master and(lower(c.scientificName) like lower(concat('%', :searchTerm, '%'))or lower(c.localName) like lower(concat('%', :searchTerm, '%'))or lower(c.type) like lower(concat('%', :searchTerm, '%'))) order by c.id Desc")
	List<Crops> searchFilterByMaster(@Param("village") Village village,@Param("searchTerm") String searchTerm, @Param("format") MasterFormat format, @Param("master") boolean master);
	
	
	
	
	
	
	
	
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
	@Query("select distinct c.associatedTk from Crops c  where c.format= :format and c.associatedTk<>'' and c.associatedTk is not null")
	List<String> associatedtk(@Param("format") MasterFormat format);
	@Query("select distinct c.knowledgeHolder from Crops c  where c.format= :format and c.knowledgeHolder<>'' and c.knowledgeHolder is not null")
	List<String> knowledgeHolder(@Param("format") MasterFormat format);
	@Query("select distinct c.uses from Crops c  where c.format= :format and c.uses<>'' and c.uses is not null")
	List<String> uses(@Param("format") MasterFormat format);
	@Query("select distinct c.partsUsed from Crops c  where c.format= :format and c.partsUsed<>'' and c.partsUsed is not null")
	List<String> partsUsed(@Param("format") MasterFormat format);
}
