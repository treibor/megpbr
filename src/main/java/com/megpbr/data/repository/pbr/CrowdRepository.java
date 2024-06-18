package com.megpbr.data.repository.pbr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.megpbr.data.entity.pbr.Crowd;

public interface CrowdRepository extends JpaRepository<Crowd, Long>{
	List<Crowd> findByVerified(boolean verified);
	List<Crowd> findByPreverified(boolean preverified);
	List<Crowd> findByPreverifiedAndVerified(boolean preverified, boolean verified);
	
	@Query("select  count(*) from Crowd c")
	int getCrowdCount();
}
