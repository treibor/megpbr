package com.application.megpbr.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.megpbr.data.entity.MasterApproval;

public interface MasterApprovalRepository extends JpaRepository<MasterApproval, Long>{
	 MasterApproval findByApproval(String approval);
}
