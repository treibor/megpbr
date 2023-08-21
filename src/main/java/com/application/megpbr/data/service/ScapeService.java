package com.application.megpbr.data.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.entity.pbr.Scapes;
import com.application.megpbr.data.repository.BlockRepository;
import com.application.megpbr.data.repository.DistrictRepository;
import com.application.megpbr.data.repository.StateRepository;
import com.application.megpbr.data.repository.VillageRepository;
import com.application.megpbr.data.repository.pbr.CropsRepository;
import com.application.megpbr.data.repository.pbr.ScapeRepository;

@Service
public class ScapeService {
	@Autowired
	private StateRepository st_repo; 
	@Autowired
	private ScapeRepository srepo;
	@Autowired
	private DistrictRepository drepo;
	@Autowired
	private BlockRepository brepo;
	@Autowired
	private VillageRepository vrepo;
	
	
	public Scapes getScapeByFauna(String fauna) {
		return srepo.findTopByFaunaPopulation(fauna);
	}
	public List<Scapes> getScapesByFormat(MasterFormat format){
		return srepo.findByFormatOrderByFaunaPopulation(format);
	}
	public void saveScape(Scapes scape) {
		srepo.save(scape);
	}
	public void deleteScape(Scapes scape) {
		srepo.delete(scape);
	}

	public List<Scapes> searchScapesFilter(Village village, String search, MasterFormat format){
		return srepo.searchFilterScapesData(village,search, format);
	}
	public List<Scapes> searchScapesFilter(Village village, String search, MasterFormat format, boolean master){
		return srepo.searchFilterScapesData(village,search, format, master);
	}
	
	
	
	
	//Populating Comboboxes for AutoComplete
	public List<String> getFaunaList(MasterFormat format) {
		return srepo.fauna(format);
	}
	public List<String> getFloraList(MasterFormat format) {
		return srepo.flora(format);
	}
	public List<String> getTypeList(MasterFormat format) {
		return srepo.type(format);
	}
	public List<String> getLandscapeList(MasterFormat format) {
		return srepo.landscape(format);
	}
	public List<String> getSubLandscapeList(MasterFormat format) {
		return srepo.sublandscape(format);
	}
	public List<String> getOwnerList(MasterFormat format) {
		return srepo.owner(format);
	}
	public List<String> getUserGroupList(MasterFormat format) {
		return srepo.usergroups(format);
	}
	public List<String> getManagementList(MasterFormat format) {
		return srepo.management(format);
	}
	public List<String> getSocialList(MasterFormat format) {
		return srepo.social(format);
	}
	public List<String> getGeneralList(MasterFormat format) {
		return srepo.general(format);
	}
	public List<String> getAssociatedTkList(MasterFormat format) {
		return srepo.associatedtk(format);
	}
	public List<String> getFallowList(MasterFormat format) {
		return srepo.fallow(format);
	}
	public List<String> getWetLandList(MasterFormat format) {
		return srepo.wetLand(format);
	}
	public List<String> getForestList(MasterFormat format) {
		return srepo.forest(format);
	}
}
