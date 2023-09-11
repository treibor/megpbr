package com.megpbr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megpbr.data.entity.AuditTrail;

public interface AuditRepository extends JpaRepository<AuditTrail, Long> {

}
