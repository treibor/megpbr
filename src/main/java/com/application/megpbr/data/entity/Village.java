package com.application.megpbr.data.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


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
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Village {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "village_generator")
	@SequenceGenerator(name="village_generator", sequenceName = "village_seq", allocationSize=1)
	private long id;
	private long villageCode;
	@NotBlank
	private String villageName;
	@ManyToOne
	@JoinColumn(name="block", referencedColumnName = "id")
	private Block block;
	//@Column(columnDefinition="Decimal(10,2) default '0.00'")
    @Digits(integer=10, fraction=2)
	private BigDecimal geographicArea;
    private Integer malePopn;
    private Integer femalePopn;
    private String habitat;
    private String rainfall;
    private String temperature;
    private String weatherPatterns;
    @Digits(integer=10, fraction=2)
	private BigDecimal  forestArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal nonagriArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal barrenArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal pastureArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal miscArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal wasteArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal fallowArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal currentFallowArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal sownArea;
    @Digits(integer=10, fraction=2)
	private BigDecimal landUse;
    private String geoUnits;
    private String manageregime;
    private String enteredBy;
	private LocalDateTime enteredOn;
	private boolean inUse;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getVillageCode() {
		return villageCode;
	}
	public void setVillageCode(long villageCode) {
		this.villageCode = villageCode;
	}
	public String getVillageName() {
		return villageName;
	}
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	public Block getBlock() {
		return block;
	}
	public void setBlock(Block block) {
		this.block = block;
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
	public String getManageregime() {
		return manageregime;
	}
	public void setManageregime(String manageregime) {
		this.manageregime = manageregime;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
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
	
	
	
	
	
	
	
}
