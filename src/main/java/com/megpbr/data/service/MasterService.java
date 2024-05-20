package com.megpbr.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.megpbr.data.entity.AuditTrail;
import com.megpbr.data.entity.MasterLocallanguage;
import com.megpbr.data.entity.MasterManagementRegime;
import com.megpbr.data.entity.MasterPosition;
import com.megpbr.data.entity.MasterSeason;
import com.megpbr.data.entity.MasterStatus;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.repository.AuditRepository;
import com.megpbr.data.repository.LocalLanguageRepository;
import com.megpbr.data.repository.MasterManagementRegimeRepository;
import com.megpbr.data.repository.MasterPositionRepository;
import com.megpbr.data.repository.MasterSeasonRepository;
import com.megpbr.data.repository.MasterStatusRepository;
import com.megpbr.data.repository.UserRepository;

@Service
public class MasterService {
	@Autowired
	UserRepository userrepo;
	@Autowired
	MasterPositionRepository positionrepo;
	@Autowired
	MasterSeasonRepository seasonrepo;
	@Autowired
	MasterStatusRepository statusrepo;
	@Autowired
	MasterManagementRegimeRepository regimerepo;
	@Autowired
	LocalLanguageRepository languagerepo;
	@Autowired
	AuditRepository auditrepo;
	public List<UserLogin> getUsers() {
		//return userrepo.findAll();
		return userrepo.findByUserNameNot("superadmin");
	}
	
	public List<MasterPosition> getPositions(){
		return positionrepo.findAll();
	}
	public List<MasterSeason> getSeasons(){
		return seasonrepo.findAll();
	}
	public List<MasterStatus> getStatus(){
		return statusrepo.findAll();
	}
	public List<MasterManagementRegime> getMasterManagementRegime(){
		return regimerepo.findAll();
	}
	public List<MasterLocallanguage> getMasterLocalLanguages(){
		return languagerepo.findAll();
	}
	public void savePosition(MasterPosition position) {
		positionrepo.save(position);
	}
	public void deletePosition(MasterPosition position) {
		positionrepo.delete(position);
	}
	public void saveStatus(MasterStatus status) {
		statusrepo.save(status);
	}
	public void deleteStatus(MasterStatus status) {
		statusrepo.delete(status);
	}
	public void saveSeason(MasterSeason status) {
		seasonrepo.save(status);
	}
	public void deleteSeason(MasterSeason status) {
		seasonrepo.delete(status);
	}
	public void saveRegime(MasterManagementRegime status) {
		regimerepo.save(status);
	}
	public void deleteRegime(MasterManagementRegime status) {
		regimerepo.delete(status);
	}
	public void saveLanguage(MasterLocallanguage status) {
		languagerepo.save(status);
	}
	public void deleteLanguage(MasterLocallanguage status) {
		languagerepo.delete(status);
	}
	public void saveUser(UserLogin status) {
		userrepo.save(status);
	}
	public void deleteUser(UserLogin status) {
		userrepo.delete(status);
	}
	public List<AuditTrail> getAuditTrail() {
		return auditrepo.findAllByOrderByIdDesc();
	}
}
