package com.megpbr.data.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.megpbr.data.dto.SampleDTO;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.pbr.Crops;
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
import com.megpbr.data.repository.pbr.CrowdRepository;
import com.megpbr.data.repository.pbr.MarketsRepository;
import com.megpbr.data.repository.pbr.ScapeRepository;
import com.megpbr.security.SecurityService;
@Service
public class DashboardService {
	@Autowired
	CrowdRepository crowdrepo;
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
	DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public  DashboardService () {
		
		
	}
	public long getVillageDetailsCount() {
		return vdrepo.getVillageDetailsCount();
	}
	public long getVillagesCount() {
		return vrepo.getVillagesCount();
	}
	public long getCrowdCount() {
		return crowdrepo.getCrowdCount();
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
	
	public long getCropsCountYear(District district, boolean master, String date) {
		LocalDateTime startdate=LocalDate.parse("01/01/"+date, df).atStartOfDay();
		LocalDateTime enddate=LocalDate.parse("31/12/"+date, df).atStartOfDay();
		return crepo.getCropsCountYear(district, master, startdate, enddate);
	}
	
	public long getAllCountByMaster(boolean master) {
		long cropscount=crepo.getCropsCountMaster(master);
		long marketcount=mrepo.getMarketsCountMaster(master);
		long scapecount=srepo.getScapesCountMaster(master);
		return cropscount+marketcount+scapecount;
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
	
	public SampleDTO getSample() {
		
		SampleDTO sampleDTO = new SampleDTO();
		
		sampleDTO.setCrops(crepo.findAll());
		sampleDTO.setMarkets(mrepo.findAll());
		
		for(Crops crop : sampleDTO.getCrops()) {
			
			System.out.println(crop.getArea());
		}
		
		return sampleDTO;
	}
	
	public List<String> getYearList(){
	    List<String> yearlist = new ArrayList<>();
	    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    int startYear = 2021;
	    //int endYear = 2030;
	    int endYear = Year.now().getValue();
	    for (int i = startYear; i <= endYear; i++) {
	        yearlist.add(String.valueOf(i));
			/*
			 * LocalDateTime startdate = LocalDate.parse("01/01/" + i, df).atStartOfDay();
			 * LocalDateTime enddate = LocalDate.parse("31/12/" + i, df).atTime(23, 59, 59);
			 * int count1 = crepo.getCropsCountYearly(false, startdate, enddate); int count2
			 * = mrepo.getMarketsCountYearly(false, startdate, enddate); int count3 =
			 * srepo.getScapesCountYearly(false, startdate, enddate);
			 * 
			 * int count=count1+count2+count3; if (count > 0) {
			 * //System.out.println("Year:"+i+"- Count:"+count1);
			 * yearlist.add(String.valueOf(i)); }
			 */
	    }
	    return yearlist;
	}
	public int getYearData(int year){
		//List<Integer> yearData = new ArrayList<>();
		int yeardata;
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime startdate = LocalDate.parse("01/01/" + year, df).atStartOfDay();
		LocalDateTime enddate = LocalDate.parse("31/12/" + year, df).atTime(23, 59, 59);
		int count1 = crepo.getCropsCountYearly(false, startdate, enddate);
		int count2 = mrepo.getMarketsCountYearly(false, startdate, enddate);
		int count3 = srepo.getScapesCountYearly(false, startdate, enddate);
		return count1+count2+count3;
	}
	public int getMonthData(){
		//List<Integer> yearData = new ArrayList<>();
		//int yeardata;
		//String month="";
		int year = Year.now().getValue();
		int currentMonth = YearMonth.now().getMonthValue();

        // Calculate the previous month
        int previousMonth = currentMonth - 1;

        // Adjust for wrap-around from January to December
        if (previousMonth == 0) {
            previousMonth = 12;
            year=year-1;
        }

        // Get the Month enum for the previous month
        Month monthEnum = Month.of(previousMonth);

        // Get the month name from the Month enum
        String monthName = monthEnum.name();

        // Format the month name to proper case (e.g., "January" instead of "JANUARY")
        monthName = monthName.charAt(0) + monthName.substring(1).toLowerCase();

        // Print the formatted month name
        //System.out.println("Previous month name: " + monthName);

        // Format the month number as a two-digit string with leading zero if necessary
        String month = (previousMonth < 10) ? "0" + previousMonth : String.valueOf(previousMonth);

        // Print the formatted month number
        //System.out.println("Formatted previous month number: " + formattedMonthNumber);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime startdate = LocalDate.parse("01/"+month+"/" + year, df).atStartOfDay();
		LocalDateTime enddate = LocalDate.parse("31/"+month+"/" + year, df).atTime(23, 59, 59);
		int count1 = crepo.getCropsCountYearly(false, startdate, enddate);
		int count2 = mrepo.getMarketsCountYearly(false, startdate, enddate);
		int count3 = srepo.getScapesCountYearly(false, startdate, enddate);
		return count1+count2+count3;
	}
	public int getCurrentMonthData(){
		//List<Integer> yearData = new ArrayList<>();
		//int yeardata;
		//String month="";
		int year = Year.now().getValue();
		int currentMonth = YearMonth.now().getMonthValue();

        

        // Get the Month enum for the previous month
        Month monthEnum = Month.of(currentMonth);

        // Get the month name from the Month enum
        String monthName = monthEnum.name();

        // Format the month name to proper case (e.g., "January" instead of "JANUARY")
        monthName = monthName.charAt(0) + monthName.substring(1).toLowerCase();

        // Print the formatted month name
        //System.out.println("Previous month name: " + monthName);

        // Format the month number as a two-digit string with leading zero if necessary
        String month = (currentMonth < 10) ? "0" + currentMonth : String.valueOf(currentMonth);

        // Print the formatted month number
        //System.out.println("Formatted previous month number: " + formattedMonthNumber);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime startdate = LocalDate.parse("01/"+month+"/" + year, df).atStartOfDay();
		LocalDateTime enddate = LocalDate.parse("31/"+month+"/" + year, df).atTime(23, 59, 59);
		int count1 = crepo.getCropsCountYearly(false, startdate, enddate);
		int count2 = mrepo.getMarketsCountYearly(false, startdate, enddate);
		int count3 = srepo.getScapesCountYearly(false, startdate, enddate);
		return count1+count2+count3;
	}
	
}
