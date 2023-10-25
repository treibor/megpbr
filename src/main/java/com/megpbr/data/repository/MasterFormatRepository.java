package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.megpbr.data.entity.MasterCategory;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.pbr.Crops;

public interface MasterFormatRepository extends JpaRepository<MasterFormat, Long>{
	MasterFormat findBfindByformat(int format);
	List <MasterFormat> findAllByOrderByFormat();
	List<MasterFormat> findByCategory(MasterCategory category);
	@Query("select c from MasterFormat c where c.category=:category and (c.format<>6 and c.format<>7 and c.format<>8 and c.format<>9 and c.format<>10 and c.format<>17) order by c.id Desc")
	List<MasterFormat> findSelectiveByCategory(@Param("category") MasterCategory category);
}
