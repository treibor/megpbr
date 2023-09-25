package com.megpbr.data.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Markets;
import com.megpbr.data.repository.BlockRepository;
import com.megpbr.data.repository.DistrictRepository;
import com.megpbr.data.repository.MasterApprovalRepository;
import com.megpbr.data.repository.StateRepository;
import com.megpbr.data.repository.VillageRepository;
import com.megpbr.data.repository.pbr.CropsRepository;
import com.megpbr.data.repository.pbr.MarketsRepository;
import com.megpbr.data.repository.pbr.ScapeRepository;

@Service
public class MarketsService {
	@Autowired
	private StateRepository st_repo; 
	@Autowired
	private MarketsRepository srepo;
	@Autowired
	private DistrictRepository drepo;
	@Autowired
	private BlockRepository brepo;
	@Autowired
	private VillageRepository vrepo;
	@Autowired
	private Dbservice dbservice;
	@Autowired
	private MasterApprovalRepository apprrepo;
	public Markets getMarketByName(String market) {
		return srepo.findTopByName(market);
	}
	public List<Markets> getMarketsByFormatAndVillage(int formatint, Village village){
		MasterFormat format=dbservice.getFormat(formatint);
		return srepo.findByFormatAndVillage(format, village);
	}
	public List<Markets> getMarketsByVillage(Village village, boolean approved){
		//return srepo.findByVillageAndApproved(village, approved);
		if(approved) {
			//apprrepo.findByApproval("Approved");
			return srepo.findByVillageAndApproved(village, apprrepo.findByApproval("Approved"));
		}else {
			return srepo.findByVillage(village);
		}
	}
	public List<Markets> getMarketsByFormat(MasterFormat format){
		return srepo.findByFormatOrderByName(format);
	}
	
	public List<Markets> getMarketsByFormat(MasterFormat format, boolean master){
		return srepo.findByStateAndFormatAndMaster(dbservice.getState(),format,  master);
	}

	public List<Markets> searchMarketsFilter(String search, MasterFormat format){
		return srepo.search(dbservice.getState(),search, format);
	}
	public List<Markets> searchMarketsFilter(Village villages, String search, MasterFormat format, boolean master){
		return srepo.searchFilterMarketsData(villages, search, format, master);
	}
	public List<Markets> searchMarketsFilter(District district, String search, MasterFormat format){
		return srepo.searchFilterMarketsData(district,search, format);
	}
	public List<Markets> searchMarketsFilter(Block block, String search, MasterFormat format){
		return srepo.searchFilterMarketsData(block,search, format);
	}
	public List<Markets> searchMarketsFilter(Village village, String search, MasterFormat format){
		return srepo.searchFilterMarketsData(village,search, format);
	}
	
	public void saveMarket(Markets market) {
		market.setState(dbservice.getState());
		srepo.save(market);
	}
	public void deleteMarket(Markets market) {
		srepo.delete(market);
	}
	
	//Populating Comboboxes for AutoComplete
	public List<String> getMarketNameList(MasterFormat format) {
		return srepo.name(format);
	}
	public List<String> getFrequencyList(MasterFormat format) {
		return srepo.frequency(format);
	}
	public List<String> getMonthList(MasterFormat format) {
		return srepo.month(format);
	}
	public List<String> getDayList(MasterFormat format) {
		return srepo.day(format);
	}
	public List<String> getAnimalTypeList(MasterFormat format) {
		return srepo.animalType(format);
	}
	public List<String> getTransactionsList(MasterFormat format) {
		return srepo.transactions(format);
	}
	public List<String> getPlacesFromList(MasterFormat format) {
		return srepo.placesFrom(format);
	}
	public List<String> getPlacesToList(MasterFormat format) {
		return srepo.placesTo(format);
	}
	public List<String> getFishLocationList(MasterFormat format) {
		return srepo.fishLocation(format);
	}
	public List<String> getFishTypeList(MasterFormat format) {
		return srepo.fishType(format);
	}
	public List<String> getFishSourceList(MasterFormat format) {
		return srepo.fishSource(format);
	}
}
