package com.megpbr.data.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterApproval;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.repository.BlockRepository;
import com.megpbr.data.repository.DistrictRepository;
import com.megpbr.data.repository.MasterApprovalRepository;
import com.megpbr.data.repository.StateRepository;
import com.megpbr.data.repository.VillageRepository;
import com.megpbr.data.repository.pbr.CropsRepository;

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
	@Autowired
	private MasterApprovalRepository apprrepo;
	@Autowired
	private Dbservice dbservice;
	
	public List<Crops> findCrops(){
		return crepo.findAll();
	}
	public List<Crops> findCropsByFormatAndVillage(int formatint, Village village){
		MasterFormat format=dbservice.getFormat(formatint);
		return crepo.findByFormatAndVillage(format, village);
	}
	public List<Crops> findCropsByVillage(Village village, boolean approved){
		//MasterFormat format=dbservice.getFormat(formatint);
		if(approved) {
			
			return crepo.findByVillageAndApproved(village,apprrepo.findByApproval("Approved"));
		}else {
			return crepo.findByVillage(village);
		}
	}
	public List<Crops> findCropsByFormat(MasterFormat format){
		return crepo.findByFormat(format);
	}
	public List<Crops> findCropsByFormatAndMaster(MasterFormat format, boolean master){
		return crepo.findByStateAndFormatAndMaster(dbservice.getState(),format, master);
	}
	public List<Crops> searchCropsFilter(String search, MasterFormat format){
		return crepo.search(dbservice.getState(), search, format);
	}
	public List<Crops> searchCropsFilter(Village villages, String search, MasterFormat format, boolean master){
		return crepo.searchFilterByMaster(villages, search, format, master);
	}
	public List<Crops> searchCropsFilter(District district, String search, MasterFormat format){
		return crepo.searchFilterCropsData(district,search, format);
	}
	public List<Crops> searchCropsFilter(Block block, String search, MasterFormat format){
		return crepo.searchFilterCropsData(block,search, format);
	}
	public List<Crops> searchCropsFilter(Village village, String search, MasterFormat format){
		return crepo.searchFilterCropsData(village,search, format);
	}
	public List<Crops> findCropsOrderByScientificName(MasterFormat format){
		//System.out.println(crepo.findByFormatOrderByScientificName(format));
		return crepo.findByFormatOrderByScientificName(format);
	}
	public List<String> findScientificNamesAsString(MasterFormat format){
		return crepo.scientificNames(format);
	}
	public List<String> findLocalNamesAsString(MasterFormat format){
		return crepo.localNames(format);
	}
	public List<String> findtypesAsString(MasterFormat format){
		return crepo.type(format);
	}
	public List<String> findHabitatAsString(MasterFormat format){
		return crepo.habitat(format);
	}
	public List<String> findVarietyAsString(MasterFormat format){
		return crepo.variety( format);
	}
	public List<String> findSourcesAsString(MasterFormat format){
		return crepo.source( format);
	}
	public List<String> findAssociatedtkAsString(MasterFormat format){
		return crepo.associatedtk( format);
	}
	public List<String> findKnowledgeHolderAsString(MasterFormat format){
		return crepo.knowledgeHolder( format);
	}
	public List<String> findUsesAsString(MasterFormat format){
		return crepo.uses( format);
	}
	public List<String> findPartsUsedAsString(MasterFormat format){
		return crepo.partsUsed( format);
	}
	public List<String> findManagementAsString(MasterFormat format){
		return crepo.management( format);
	}
	public List<String> findXfield1AsString(MasterFormat format){
		return crepo.xfield1( format);
	}
	public List<String> findXfield2AsString(MasterFormat format){
		return crepo.xfield2( format);
	}
	public List<String> findOtherDetailsAsString(MasterFormat format){
		return crepo.otherDetails( format);
	}
	public List<String> findSpFeaturesAsString(MasterFormat format){
		return crepo.specialFeatures( format);
	}
	public List<Crops> findDistinctScientificName(MasterFormat format){
		//System.out.println(crepo.findByFormatOrderByScientificName(format));
		return crepo.findDistinctByFormat(format);
	}
	public List<Crops> findCropsOrderByHabitat(){
		return crepo.findAllByOrderByHabitat();
	}
	
	public Crops findCropBySientificName(String sname) {
		//return crepo.findTopByScientificName(sname);
		return crepo.findTopByScientificNameOrderByIdAsc(sname);
	}
	public void deleteCrop(Crops crop) {
		crepo.delete(crop);
	}
	
	
	
}
