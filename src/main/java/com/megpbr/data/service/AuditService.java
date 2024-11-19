package com.megpbr.data.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.repository.AuditRepository;

@Service
public class AuditService implements Serializable{
	private static final long serialVersionUID = 1L;
	@Autowired
	private AuditRepository auditrepo; 
	
	
	public void updateAudit(AuditTrail entity) {
		auditrepo.save(entity);
	}
	
	
	
}
