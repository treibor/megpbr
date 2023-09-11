package com.megpbr.data.entity.villages;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.megpbr.data.entity.MasterManagementRegime;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.Village;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="VillageDetails", schema = "megpbr")
public class VillageDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "villagedetails_generator")
	@SequenceGenerator(name="villagedetails_generator", sequenceName = "villagedetails_seq", allocationSize=1)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="village", referencedColumnName = "villageCode")
	@NotNull(message = "Please Select a Village")
	private Village village;
	//@Column(columnDefinition="Decimal(10,2) default '0.00'")
    private BigDecimal geographicArea;
    
    private Integer malePopn=0;
    private Integer femalePopn=0;
    @Column(length = 1000)
    private String habitat;
    private String rainfall;
    private String temperature;
    private String weatherPatterns;
    private BigDecimal  forestArea;
    private BigDecimal nonagriArea;
    private BigDecimal barrenArea;
    private BigDecimal pastureArea;
    private BigDecimal miscArea;
    private BigDecimal wasteArea;
    private BigDecimal fallowArea;
    private BigDecimal currentFallowArea;
    private BigDecimal sownArea;
    private BigDecimal landUse;
    private String geoUnits;
    @ManyToOne
	@JoinColumn(name="managementregime", referencedColumnName = "id")
    private MasterManagementRegime managementregime;
    private LocalDate pbrDate;
    @ManyToOne
	@JoinColumn(name = "enteredBy", referencedColumnName = "id")
	private UserLogin enteredBy;
    private LocalDateTime enteredOn;
	private boolean inUse;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public BigDecimal getGeographicArea() {
		return geographicArea;
	}
	public void setGeographicArea(BigDecimal geographicArea) {
		this.geographicArea = geographicArea;
	}
	public Integer getMalePopn() {
		return malePopn;
	}
	public void setMalePopn(Integer malePopn) {
		this.malePopn = malePopn;
	}
	public Integer getFemalePopn() {
		return femalePopn;
	}
	public void setFemalePopn(Integer femalePopn) {
		this.femalePopn = femalePopn;
	}
	public String getHabitat() {
		return habitat;
	}
	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}
	
	
	
	public String getRainfall() {
		return rainfall;
	}
	public void setRainfall(String rainfall) {
		this.rainfall = rainfall;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWeatherPatterns() {
		return weatherPatterns;
	}
	public void setWeatherPatterns(String weatherPatterns) {
		this.weatherPatterns = weatherPatterns;
	}
	public BigDecimal getForestArea() {
		return forestArea;
	}
	public void setForestArea(BigDecimal forestArea) {
		this.forestArea = forestArea;
	}
	public BigDecimal getNonagriArea() {
		return nonagriArea;
	}
	public void setNonagriArea(BigDecimal nonagriArea) {
		this.nonagriArea = nonagriArea;
	}
	public BigDecimal getBarrenArea() {
		return barrenArea;
	}
	public void setBarrenArea(BigDecimal barrenArea) {
		this.barrenArea = barrenArea;
	}
	public BigDecimal getPastureArea() {
		return pastureArea;
	}
	public void setPastureArea(BigDecimal pastureArea) {
		this.pastureArea = pastureArea;
	}
	public BigDecimal getMiscArea() {
		return miscArea;
	}
	public void setMiscArea(BigDecimal miscArea) {
		this.miscArea = miscArea;
	}
	public BigDecimal getWasteArea() {
		return wasteArea;
	}
	public void setWasteArea(BigDecimal wasteArea) {
		this.wasteArea = wasteArea;
	}
	public BigDecimal getFallowArea() {
		return fallowArea;
	}
	public void setFallowArea(BigDecimal fallowArea) {
		this.fallowArea = fallowArea;
	}
	public BigDecimal getCurrentFallowArea() {
		return currentFallowArea;
	}
	public void setCurrentFallowArea(BigDecimal currentFallowArea) {
		this.currentFallowArea = currentFallowArea;
	}
	public BigDecimal getSownArea() {
		return sownArea;
	}
	public void setSownArea(BigDecimal sownArea) {
		this.sownArea = sownArea;
	}
	public BigDecimal getLandUse() {
		return landUse;
	}
	public void setLandUse(BigDecimal landUse) {
		this.landUse = landUse;
	}
	public String getGeoUnits() {
		return geoUnits;
	}
	public void setGeoUnits(String geoUnits) {
		this.geoUnits = geoUnits;
	}
	
	
	
	public MasterManagementRegime getManagementregime() {
		return managementregime;
	}
	public void setManagementregime(MasterManagementRegime managementregime) {
		this.managementregime = managementregime;
	}
	
	public UserLogin getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(UserLogin enteredBy) {
		this.enteredBy = enteredBy;
	}
	public LocalDateTime getEnteredOn() {
		return enteredOn;
	}
	public void setEnteredOn(LocalDateTime enteredOn) {
		this.enteredOn = enteredOn;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	public LocalDate getPbrDate() {
		return pbrDate;
	}
	public void setPbrDate(LocalDate pbrDate) {
		this.pbrDate = pbrDate;
	}
	
	
	
	
	
	
	
	
}
