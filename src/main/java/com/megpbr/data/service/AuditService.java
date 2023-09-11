package com.megpbr.data.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.repository.AuditRepository;
import com.megpbr.data.repository.BlockRepository;
import com.megpbr.data.repository.DistrictRepository;
import com.megpbr.data.repository.StateRepository;
import com.megpbr.data.repository.VillageRepository;
import com.megpbr.data.repository.pbr.CropsRepository;

@Service
public class AuditService {
	@Autowired
	private AuditRepository auditrepo; 
	
	
	public void updateAudit(AuditTrail entity) {
		auditrepo.save(entity);
	}
	
	
	
}
