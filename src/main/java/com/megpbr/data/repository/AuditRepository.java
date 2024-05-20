package com.megpbr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.AuditTrail;

public interface AuditRepository extends JpaRepository<AuditTrail, Long> {
	List<AuditTrail> findAllByOrderByIdDesc();
}
