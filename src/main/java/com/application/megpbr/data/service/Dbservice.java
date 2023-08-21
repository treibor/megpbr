package com.application.megpbr.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.application.megpbr.data.entity.Block;
import com.application.megpbr.data.entity.District;
import com.application.megpbr.data.entity.MasterLocallanguage;
import com.application.megpbr.data.entity.MasterApproval;
import com.application.megpbr.data.entity.MasterCategory;
import com.application.megpbr.data.entity.MasterCommercial;
import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.MasterGender;
import com.application.megpbr.data.entity.MasterManagementRegime;
import com.application.megpbr.data.entity.MasterPosition;
import com.application.megpbr.data.entity.MasterSeason;
import com.application.megpbr.data.entity.MasterStatus;
import com.application.megpbr.data.entity.MasterWildhome;
import com.application.megpbr.data.entity.State;
import com.application.megpbr.data.entity.Village;
import com.application.megpbr.data.entity.pbr.Crops;
import com.application.megpbr.data.entity.villages.VillageAnnexure1;
import com.application.megpbr.data.entity.villages.VillageAnnexure2;
import com.application.megpbr.data.entity.villages.VillageAnnexure3;
import com.application.megpbr.data.entity.villages.VillageAnnexure4;
import com.application.megpbr.data.entity.villages.VillageAnnexure5;
import com.application.megpbr.data.entity.villages.VillageDetails;
import com.application.megpbr.data.repository.BlockRepository;
import com.application.megpbr.data.repository.DistrictRepository;
import com.application.megpbr.data.repository.LocalLanguageRepository;
import com.application.megpbr.data.repository.MasterApprovalRepository;
import com.application.megpbr.data.repository.MasterCategoryRepository;
import com.application.megpbr.data.repository.MasterCommercialRepository;
import com.application.megpbr.data.repository.MasterSeasonRepository;
import com.application.megpbr.data.repository.MasterStatusRepository;
import com.application.megpbr.data.repository.MasterWildhomeRepository;
import com.application.megpbr.data.repository.MasterFormatRepository;
import com.application.megpbr.data.repository.MasterGenderRepository;
import com.application.megpbr.data.repository.MasterManagementRegimeRepository;
import com.application.megpbr.data.repository.MasterPositionRepository;
import com.application.megpbr.data.repository.StateRepository;
import com.application.megpbr.data.repository.VillageAnnexure1Repository;
import com.application.megpbr.data.repository.VillageAnnexure2Repository;
import com.application.megpbr.data.repository.VillageAnnexure3Repository;
import com.application.megpbr.data.repository.VillageAnnexure4Repository;
import com.application.megpbr.data.repository.VillageAnnexure5Repository;
import com.application.megpbr.data.repository.VillageDetailsRepository;
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
	private MasterFormatRepository formatrepo;
	@Autowired
	private MasterStatusRepository statusrepo;
	@Autowired
	private LocalLanguageRepository llrepo;
	@Autowired
	private MasterSeasonRepository mfsrepo;
	@Autowired
	private MasterCommercialRepository mcrepo;
	@Autowired
	private MasterWildhomeRepository mwhrepo;
	@Autowired
	private MasterCategoryRepository mcatrepo;
	@Autowired
	private VillageDetailsRepository vdrepo;
	@Autowired
	private VillageAnnexure1Repository va1repo;
	@Autowired
	private VillageAnnexure2Repository va2repo;
	@Autowired
	private VillageAnnexure3Repository va3repo;
	@Autowired
	private VillageAnnexure4Repository va4repo;
	@Autowired
	private VillageAnnexure5Repository va5repo;
	@Autowired
	private MasterManagementRegimeRepository mmrp;
	@Autowired
	private MasterGenderRepository mgrrepo;
	@Autowired
	private MasterPositionRepository mprepo;
	@Autowired
	private MasterApprovalRepository marepo;
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
		return vrepo.findByBlockAndInUse(block, inUse);
	}
	
	public List <Village> getVillagesTest(Block block){
		return vrepo.findByBlock(block);
	}
	public List <VillageDetails> getVillageDetails(){
		return vdrepo.findAll();
	}
	public List <VillageDetails> getVillageDetails(District district,Block block, Village village, String search){
		return vdrepo.getFilterVillageDetails( village, block, district, search);
	}
	public List <VillageDetails> getVillageDetails( Village village, String search){
		return vdrepo.getFilterVillageDetails( village,  search);
	}
	public List <VillageDetails> getVillageDetails(Block block,  String search){
		return vdrepo.getFilterVillageDetails( block, search);
	}
	public List <VillageDetails> getVillageDetails(District district, String search){
		return vdrepo.getFilterVillageDetails(district, search);
	}
	public List <VillageDetails> getVillageDetails(District district){
		return vdrepo.getFilterVillageDetails(district);
	}
	public List<MasterLocallanguage> getLocalLanguage(){
		return llrepo.findAll();
	}
	
	public List<MasterFormat> getFormats(){
		return formatrepo.findAll();
	}
	public MasterFormat getFormat(int format){
		return formatrepo.findBfindByformat(format);
	}
	public List<MasterCategory> getCategory(){
		return mcatrepo.findAll();
	}
	public MasterCategory getCategory(int id){
		return mcatrepo.findById(id);
	}
	public List<Crops> getCrops(){
		return crepo.findAll();
	}
	public List<MasterStatus> getStatus(){
		return statusrepo.findAll();
	}
	
	public List<MasterSeason> getMasterFruitSeason(){
		return mfsrepo.findAll();
	}
	
	public List<MasterCommercial> getMasterCommercial(){
		return mcrepo.findAll();
	}
	
	public List<MasterWildhome> getMasterWildhome(){
		return mwhrepo.findAll();
	}
	public List<MasterApproval> getMasterApproval(){
		return marepo.findAll();
	}
	
	public void updateCrop(Crops entity) {
		crepo.save(entity);
	}
	
	public void updateVillageDetails(VillageDetails entity) {
		vdrepo.save(entity);
	}
	public void updateVillage(Village entity) {
		vrepo.save(entity);
	}
	public void deleteVillageDetails(VillageDetails entity) {
		vdrepo.delete(entity);
	}
	public void updateAnnexure1Details(VillageAnnexure1 entity) {
		va1repo.save(entity);
	}
	public void deleteAnnexure1Details(VillageAnnexure1 annex1) {
		va1repo.delete(annex1);
	}
	public void updateAnnexure2Details(VillageAnnexure2 entity2) {
		va2repo.save(entity2);
	}
	public void deleteAnnexure2Details(VillageAnnexure2 annex2) {
		va2repo.delete(annex2);
	}
	public void updateAnnexure3Details(VillageAnnexure3 entity3) {
		va3repo.save(entity3);
	}
	public void deleteAnnexure3Details(VillageAnnexure3 annex3) {
		va3repo.delete(annex3);
	}
	public void updateAnnexure4Details(VillageAnnexure4 entity4) {
		va4repo.save(entity4);
	}
	public void deleteAnnexure4Details(VillageAnnexure4 annex4) {
		va4repo.delete(annex4);
	}
	public void updateAnnexure5Details(VillageAnnexure5 entity5) {
		va5repo.save(entity5);
	}
	public void deleteAnnexure5Details(VillageAnnexure5 annex5) {
		va5repo.delete(annex5);
	}
	public List<VillageAnnexure1> getAnnexure1Details(Village village) {
		return va1repo.findByVillage(village);
	}
	public List<VillageAnnexure2> getAnnexure2Details(Village village) {
		return va2repo.findByVillage(village);
	}
	public List<VillageAnnexure3> getAnnexure3Details(Village village) {
		return va3repo.findByVillage(village);
	}
	public List<VillageAnnexure4> getAnnexure4Details(Village village) {
		return va4repo.findByVillage(village);
	}
	public List<VillageAnnexure5> getAnnexure5Details(Village village) {
		return va5repo.findByVillage(village);
	}
	public List<MasterManagementRegime> getMasterManagementRegime(){
		return mmrp.findAll();
	}
	
	public List<MasterGender> getGenders(){
		return mgrrepo.findAll();
	}
	public List<MasterPosition> getPositions(){
		return mprepo.findAll();
	}
}
