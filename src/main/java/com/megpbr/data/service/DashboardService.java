package com.megpbr.data.service;

import java.util.List;
import java.util.Objects;

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
import com.megpbr.data.repository.VillageAnnexure1Repository;
import com.megpbr.data.repository.VillageAnnexure2Repository;
import com.megpbr.data.repository.VillageAnnexure3Repository;
import com.megpbr.data.repository.VillageAnnexure4Repository;
import com.megpbr.data.repository.VillageAnnexure5Repository;
import com.megpbr.data.repository.VillageDetailsRepository;
import com.megpbr.data.repository.VillageRepository;
import com.megpbr.data.repository.pbr.CropsRepository;
import com.megpbr.data.repository.pbr.MarketsRepository;
import com.megpbr.data.repository.pbr.ScapeRepository;
import com.megpbr.security.SecurityService;

import jakarta.transaction.Transactional;
@Service
public class DashboardService {
	@Autowired
	private StateRepository st_repo; 
	@Autowired
	private CropsRepository crepo;
	@Autowired
	private MarketsRepository mrepo;
	@Autowired
	private ScapeRepository srepo;
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
	private Dbservice dbservice;
	List<District> districtlist;
	public  DashboardService () {
		
		
	}
	public State getState() {
		return dbservice.getState();
	}
	public List<District> getDistricts() {
		return dbservice.getDistricts(getState());
	}
	public List<Crops> getCrops(District district) {
		return crepo.searchFilterCropsData(district, null, null);
	}
	
	public long getCropsCount(District district, boolean master) {
		return crepo.getCropsCount(district, master);
	}
	public long getMarketsCount(District district, boolean master) {
		return mrepo.getMarketsCount(district, master);
	}
	public long getScapesCount(District district, boolean master) {
		return srepo.getScapesCount(district, master);
	}
	public long getDistrictCount(District district, boolean master) {
		return getCropsCount(district, master)+getMarketsCount(district, master)+getScapesCount(district, master);
	}
	
	public List<MasterFormat> getFormats() {
		return formatrepo.findAllByOrderByFormat();
	}
	public long getCropsCount(MasterFormat format) {
		return crepo.countByFormat(format);
	}
	public long getMarketsCount(MasterFormat format) {
		return mrepo.countByFormat(format);
	}
	public long getScapesCount(MasterFormat format) {
		return srepo.countByFormat(format);
	}
	public long getFormatCount(MasterFormat format, boolean master) {
		return getCropsCount(format)+getMarketsCount(format)+getScapesCount(format);
	}
	
	
	public long getChildCount(District parent) {
		System.out.println(brepo.findByDistrict(parent).size());
		return drepo.findAll().size();

	}
	public long getChildCount(State parent) {
		return srepo.findAll().size();

	}
	public Boolean hasChildren(District parent) {
		int a = brepo.findByDistrict(parent).size();
		if (a > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<District> fetchChildren(District parent) {
		return drepo.findAll();
	}

	
}
