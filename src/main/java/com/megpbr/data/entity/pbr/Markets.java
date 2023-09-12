package com.megpbr.data.entity.pbr;

import java.time.LocalDateTime;

import com.megpbr.data.entity.MasterApproval;
import com.megpbr.data.entity.MasterCommercial;
import com.megpbr.data.entity.MasterFormat;
import com.megpbr.data.entity.MasterStatus;
import com.megpbr.data.entity.MasterWildhome;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.UserLogin;
import com.megpbr.data.entity.Village;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="Markets", schema = "megpbr")
public class Markets {
	//For format 7-10
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "market_generator")
	@SequenceGenerator(name="market_generator", sequenceName = "market_seq", allocationSize=1)
	private long id;
	
	@NotBlank(message = "This Cannot Be Blank")
	//private String scientificName;
	private String name;
	private String frequency;
	private String month;
	private String day;
	private String animalType;
	private String transactions;
	private String placesFrom;
	private String placesTo;
	private String fishLocation;
	private String fishType;
	private String fishSource;
	private String remarks;
	//private String associatedTk;
	private byte[] photo1;
	private String photo1Source;
	private byte[] photo2;
	private String photo2Source;
	private byte[] photo3;
	private String photo3Source;
	private byte[] photo4;
	private String photo4Source;
	private boolean master;
	private boolean verified;
	@ManyToOne
	@JoinColumn(name = "enteredBy", referencedColumnName = "id")
	private UserLogin enteredBy;
	private LocalDateTime enteredOn;
	private String latitude;
	private String longitude;
	@ManyToOne
	@JoinColumn(name = "approved", referencedColumnName = "id")
	private MasterApproval approved;
	@ManyToOne
	@JoinColumn(name = "format", referencedColumnName = "id")
	private MasterFormat format;
	@ManyToOne
	@JoinColumn(name = "village", referencedColumnName = "villageCode")
	private Village village;
	@ManyToOne
	@JoinColumn(name = "state", referencedColumnName = "stateCode")
	private State state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getAnimalType() {
		return animalType;
	}
	public void setAnimalType(String animalType) {
		this.animalType = animalType;
	}
	public String getTransactions() {
		return transactions;
	}
	public void setTransactions(String transactions) {
		this.transactions = transactions;
	}
	public String getPlacesFrom() {
		return placesFrom;
	}
	public void setPlacesFrom(String placesFrom) {
		this.placesFrom = placesFrom;
	}
	public String getPlacesTo() {
		return placesTo;
	}
	public void setPlacesTo(String placesTo) {
		this.placesTo = placesTo;
	}
	public String getFishLocation() {
		return fishLocation;
	}
	public void setFishLocation(String fishLocation) {
		this.fishLocation = fishLocation;
	}
	public String getFishType() {
		return fishType;
	}
	public void setFishType(String fishType) {
		this.fishType = fishType;
	}
	public String getFishSource() {
		return fishSource;
	}
	public void setFishSource(String fishSource) {
		this.fishSource = fishSource;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public byte[] getPhoto1() {
		return photo1;
	}
	public void setPhoto1(byte[] photo1) {
		this.photo1 = photo1;
	}
	public String getPhoto1Source() {
		return photo1Source;
	}
	public void setPhoto1Source(String photo1Source) {
		this.photo1Source = photo1Source;
	}
	public byte[] getPhoto2() {
		return photo2;
	}
	public void setPhoto2(byte[] photo2) {
		this.photo2 = photo2;
	}
	public String getPhoto2Source() {
		return photo2Source;
	}
	public void setPhoto2Source(String photo2Source) {
		this.photo2Source = photo2Source;
	}
	public byte[] getPhoto3() {
		return photo3;
	}
	public void setPhoto3(byte[] photo3) {
		this.photo3 = photo3;
	}
	public String getPhoto3Source() {
		return photo3Source;
	}
	public void setPhoto3Source(String photo3Source) {
		this.photo3Source = photo3Source;
	}
	public byte[] getPhoto4() {
		return photo4;
	}
	public void setPhoto4(byte[] photo4) {
		this.photo4 = photo4;
	}
	public String getPhoto4Source() {
		return photo4Source;
	}
	public void setPhoto4Source(String photo4Source) {
		this.photo4Source = photo4Source;
	}
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
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
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public MasterApproval getApproved() {
		return approved;
	}
	public void setApproved(MasterApproval approved) {
		this.approved = approved;
	}
	public MasterFormat getFormat() {
		return format;
	}
	public void setFormat(MasterFormat format) {
		this.format = format;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	
		
	
}
