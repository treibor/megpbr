package com.megpbr.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterApproval;
import com.megpbr.data.entity.MasterCategory;
import com.megpbr.data.entity.MasterCommercial;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.MasterGender;
import com.megpbr.data.entity.MasterLocallanguage;
import com.megpbr.data.entity.MasterManagementRegime;
import com.megpbr.data.entity.MasterPosition;
import com.megpbr.data.entity.MasterSeason;
import com.megpbr.data.entity.MasterStatus;
import com.megpbr.data.entity.MasterWildhome;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.UserLoginLevel;
import com.megpbr.data.entity.Village;
import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.villages.VillageAnnexure1;
import com.megpbr.data.entity.villages.VillageAnnexure2;
import com.megpbr.data.entity.villages.VillageAnnexure3;
import com.megpbr.data.entity.villages.VillageAnnexure4;
import com.megpbr.data.entity.villages.VillageAnnexure5;
import com.megpbr.data.entity.villages.VillageDetails;
import com.megpbr.data.repository.BlockRepository;
import com.megpbr.data.repository.DistrictRepository;
import com.megpbr.data.repository.LocalLanguageRepository;
import com.megpbr.data.repository.MasterApprovalRepository;
import com.megpbr.data.repository.MasterCategoryRepository;
import com.megpbr.data.repository.MasterCommercialRepository;
import com.megpbr.data.repository.MasterFormatRepository;
import com.megpbr.data.repository.MasterGenderRepository;
import com.megpbr.data.repository.MasterManagementRegimeRepository;
import com.megpbr.data.repository.MasterPositionRepository;
import com.megpbr.data.repository.MasterSeasonRepository;
import com.megpbr.data.repository.MasterStatusRepository;
import com.megpbr.data.repository.MasterWildhomeRepository;
import com.megpbr.data.repository.StateRepository;
import com.megpbr.data.repository.UserLevelRepository;
import com.megpbr.data.repository.UserRepository;
import com.megpbr.data.repository.VillageAnnexure1Repository;
import com.megpbr.data.repository.VillageAnnexure2Repository;
import com.megpbr.data.repository.VillageAnnexure3Repository;
import com.megpbr.data.repository.VillageAnnexure4Repository;
import com.megpbr.data.repository.VillageAnnexure5Repository;
import com.megpbr.data.repository.VillageDetailsRepository;
import com.megpbr.data.repository.VillageRepository;
import com.megpbr.data.repository.pbr.CropsRepository;
import com.megpbr.security.SecurityService;

import jakarta.transaction.Transactional;
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
	@Autowired
	private SecurityService secservice;
	@Autowired
	private UserService userservice;
	@Autowired
	private UserLevelRepository levelrepo;
	@Autowired
	private UserRepository repository;
	
	public UserDetails getloggeduser() {
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return secservice.getAuthenticatedUser();
	}

	public UserDetails getUser() {
		return secservice.getAuthenticatedUser();
	}
	public String getLoggedUserName() {
    	return getUser().getUsername();
    }
	 
    public UserLogin getLoggedUser() {
    	return repository.findByUserName(getLoggedUserName());
    }
	public State getState() {
		return getLoggedUser().getState();
		//return st_repo.findByStateName("Meghalaya");
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
	public List <Village> getVillages(long id){
		return vrepo.findById(id);
	}
	public Village getVillagesByVillageCode(long id){
		return vrepo.findByVillageCode(id);
	}
	public List <Village> getVillages(Block block){
		return vrepo.findByBlock(block);
	}
	public List <Village> getVillages(Block block, boolean inUse){
		return vrepo.findByBlockAndInUse(block, inUse);
	}
	public List <Object[]> getVillageCount(){
		return vrepo.villageCount();
	}
	public List <Village> getVillagesTest(Block block){
		return vrepo.findByBlock(block);
	}
	public List <VillageDetails> getVillageDetail(Village village){
		return vdrepo.findByVillage(village);
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
	public List<MasterFormat> getFormatByCategory(MasterCategory category){
		return formatrepo.findByCategory(category);
	}
	public List<MasterFormat> getSelectedFormatByCategory(MasterCategory category){
		return formatrepo.findSelectiveByCategory(category);
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
	
	public MasterApproval getMasterApprovalApproved(){
		return marepo.findByApproval("Approved");
	}
	
	public void updateCrop(Crops entity) {
		entity.setState(getState());
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
	
	public void saveState(State state) {
		st_repo.save(state);
	}
	public void saveDistrict(District district) {
		drepo.save(district);
	}
	public void saveBlock(Block block) {
		brepo.save(block);
	}
	public void saveVillage(Village village) {
		vrepo.save(village);
	}
	public void deleteState(State state) {
		st_repo.delete(state);
	}
	public void deleteDistrict(District dist) {
		drepo.delete(dist);
	}
	public void deleteBlock(Block block) {
		brepo.delete(block);
	}
	public void deleteVillage(Village vill) {
		vrepo.delete(vill);
	}
}
