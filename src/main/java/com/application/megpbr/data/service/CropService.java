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

import com.application.megpbr.data.repository.BlockRepository;
import com.application.megpbr.data.repository.DistrictRepository;
import com.application.megpbr.data.repository.StateRepository;
import com.application.megpbr.data.repository.VillageRepository;
import com.application.megpbr.data.repository.pbr.CropsRepository;

@Service
public class CropService {
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
	
	public List<Crops> findCrops(){
		return crepo.findAll();
	}
	
	public List<Crops> findCropsByFormat(MasterFormat format){
		return crepo.findByFormat(format);
	}
	public List<Crops> searchCropsByFormat(String search, MasterFormat format, boolean master, boolean village){
		if (master && village) {
			return crepo.search(search, format);
		}else if (master){
			return crepo.searchMasterData(search, format);
		}else if (village){
			return crepo.searchVillageData(search, format);
		}else {
			return Collections.emptyList();
		}
	}
	public List<Crops> findCropsOrderByScientificName(MasterFormat format){
		//System.out.println(crepo.findByFormatOrderByScientificName(format));
		return crepo.findByFormatOrderByScientificName(format);
	}
	public List<String> findScientificNamesAsString(MasterFormat format){
		return crepo.scientificNames(format);
	}
	public List<String> findtypesAsString(MasterFormat format){
		return crepo.type(format);
	}
	public List<String> findHabitatAsString(MasterFormat format){
		return crepo.habitat(format);
	}
	
	public List<Crops> findDistinctScientificName(MasterFormat format){
		//System.out.println(crepo.findByFormatOrderByScientificName(format));
		return crepo.findDistinctByFormat(format);
	}
	public List<Crops> findCropsOrderByHabitat(){
		return crepo.findAllByOrderByHabitat();
	}
	
	public Crops findCropBySientificName(String sname) {
		return crepo.findTopByScientificName(sname);
		
	}
	public void deleteCrop(Crops crop) {
		crepo.delete(crop);
	}
}
