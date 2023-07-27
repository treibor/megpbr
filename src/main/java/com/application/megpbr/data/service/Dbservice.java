package com.application.megpbr.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.LocalLanguage;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.MasterFruitSeason;
import com.application.megpbr.data.entity.MasterStatus;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;

import com.application.megpbr.data.repository.BlockRepository;
import com.application.megpbr.data.repository.DistrictRepository;
import com.application.megpbr.data.repository.LocalLanguageRepository;
import com.application.megpbr.data.repository.MasterFruitSeasonRepository;
import com.application.megpbr.data.repository.MasterStatusRepository;
import com.application.megpbr.data.repository.MformatRepository;
import com.application.megpbr.data.repository.StateRepository;
import com.application.megpbr.data.repository.VillageRepository;
import com.application.megpbr.data.repository.pbr.CropsRepository;
@Service
public class Dbservice {
	@Autowired
	private StateRepository st_repo; 
	@Autowired
	private CropsRepository crepo;
	@Autowired
	private DistrictRepository drepo;
	@Autowired
	private BlockRepository brepo;
	@Autowired
	private VillageRepository vrepo;
	
	@Autowired
	MformatRepository formatrepo;
	@Autowired
	MasterStatusRepository statusrepo;
	@Autowired
	LocalLanguageRepository llrepo;
	@Autowired
	MasterFruitSeasonRepository mfsrepo;
	
	public boolean isSuperAdmin() {
		return false;
	}
	public State getState() {
		return st_repo.findByStateName("Meghalaya");
	}
	public List<State> getStates(){
		return st_repo.findAll();
	}
	
	public List<District> getDistricts(){
		return drepo.findAll();
	}
	
	public List<District> getDistricts(State state){
		return drepo.findByState(state);
	}
	public List<Block> getBlocks(){
		return brepo.findAll();
	}
	
	public List<Block> getBlocks(District district){
		return brepo.findByDistrict(district);
	}
	
	public List <Village> getVillages(){
		return vrepo.findAll();
	}
	public List <Village> getVillages(Block block){
		return vrepo.findByBlock(block);
	}
	public List <Village> getVillages(Block block, boolean inUse){
		return vrepo.findByBlock(block);
	}
	
	public List<LocalLanguage> getLocalLanguage(){
		return llrepo.findAll();
	}
	
	public List<MasterFormat> getFormats(){
		return formatrepo.findAll();
	}
	public MasterFormat getFormat(String format){
		return formatrepo.findBfindByformat(format);
	}
	public List<Crops> getCrops(){
		return crepo.findAll();
	}
	public List<MasterStatus> getStatus(){
		return statusrepo.findAll();
	}
	
	public List<MasterFruitSeason> getMasterFruitSeason(){
		return mfsrepo.findAll();
	}
	public void updateCrop(Crops entity) {
		crepo.save(entity);
	}
}
