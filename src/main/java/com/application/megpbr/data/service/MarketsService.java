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
import com.application.megpbr.data.entity.pbr.Markets;
import com.application.megpbr.data.repository.BlockRepository;
import com.application.megpbr.data.repository.DistrictRepository;
import com.application.megpbr.data.repository.StateRepository;
import com.application.megpbr.data.repository.VillageRepository;
import com.application.megpbr.data.repository.pbr.CropsRepository;
import com.application.megpbr.data.repository.pbr.MarketsRepository;
import com.application.megpbr.data.repository.pbr.ScapeRepository;

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
	
	
	public Markets getMarketByName(String market) {
		return srepo.findTopByName(market);
	}
	public List<Markets> getMarketsByFormat(MasterFormat format){
		return srepo.findByFormatOrderByName(format);
	}
	

	public List<Markets> searchMarketsFilter(Village village, String search, MasterFormat format){
		return srepo.searchFilterMarketsData(village,search, format);
	}
	public List<Markets> searchMarketsFilter(Village village, String search, MasterFormat format, boolean master){
		return srepo.searchFilterMarketsData(village,search, format, master);
	}
	
	public void saveMarket(Markets market) {
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
