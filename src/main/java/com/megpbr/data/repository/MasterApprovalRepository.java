package com.megpbr.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.MasterApproval;

public interface MasterApprovalRepository extends JpaRepository<MasterApproval, Long>{
	 MasterApproval findByApproval(String approval);
}
