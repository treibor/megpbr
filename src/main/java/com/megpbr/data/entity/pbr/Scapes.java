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

import jakarta.persistence.Column;
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
@Table(name="Scapes", schema = "megpbr")
public class Scapes {
	//For format 7-10
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scape_generator")
	@SequenceGenerator(name="scape_generator", sequenceName = "scape_seq", allocationSize=1)
	private long id;

	@NotBlank(message = "This Cannot Be Blank")
	//private String scientificName;
	@Column(length =1000)
	private String faunaPopulation;
	@Column(length = 1000)
	private String floraOccupation;
	@Column(length = 1000)
	private String typeAgriOccupation;
	@Column(length = 2000)
	private String landscape;
	private String subLandscape;
	@Column(length = 500)
	private String fallow;
	@Column(length = 500)
	private String forest;
	private String wetLand;
	private String features;
	private String ownerHouse;
	private String userGroups;
	 @Column(length = 1000)
	private String management;
	 @Column(length = 1000)
	private String generalResources;
	private String socialCommunity;
	private String otherDetails;
	private String localLanguage;
	//private String otherLanguage;
	private String remarks;
	@Column(length = 1000)
	private String associatedTk;
	private byte[] photo1;
	private String photo1Source;
	private byte[] photo2;
	private String photo2Source;
	private byte[] photo3;
	private String photo3Source;
	private byte[] photo4;
	private String photo4Source;
	private boolean master;
	private boolean crowdData;
	@ManyToOne
	@JoinColumn(name = "enteredBy", referencedColumnName = "id")
	private UserLogin enteredBy;
	private LocalDateTime enteredOn;
	private String latitude;
	private String longitude;
	@ManyToOne
	@JoinColumn(name = "format", referencedColumnName = "id")
	private MasterFormat format;
	@ManyToOne
	@JoinColumn(name = "state", referencedColumnName = "stateCode")
	private State state;
	@ManyToOne
	@JoinColumn(name = "village", referencedColumnName = "villageCode")
	private Village village;
	
	@ManyToOne
	@JoinColumn(name = "approved", referencedColumnName = "id")
	private MasterApproval approved;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	

	public String getFaunaPopulation() {
		return faunaPopulation;
	}

	public void setFaunaPopulation(String faunaPopulation) {
		this.faunaPopulation = faunaPopulation;
	}

	public String getFloraOccupation() {
		return floraOccupation;
	}

	public void setFloraOccupation(String floraOccupation) {
		this.floraOccupation = floraOccupation;
	}

	public String getTypeAgriOccupation() {
		return typeAgriOccupation;
	}

	public void setTypeAgriOccupation(String typeAgriOccupation) {
		this.typeAgriOccupation = typeAgriOccupation;
	}

	public String getLandscape() {
		return landscape;
	}

	public void setLandscape(String landscape) {
		this.landscape = landscape;
	}

	public String getSubLandscape() {
		return subLandscape;
	}

	public void setSubLandscape(String subLandscape) {
		this.subLandscape = subLandscape;
	}

	public String getFallow() {
		return fallow;
	}

	public void setFallow(String fallow) {
		this.fallow = fallow;
	}

	public String getForest() {
		return forest;
	}

	public void setForest(String forest) {
		this.forest = forest;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getOwnerHouse() {
		return ownerHouse;
	}

	public void setOwnerHouse(String ownerHouse) {
		this.ownerHouse = ownerHouse;
	}

	public String getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(String userGroups) {
		this.userGroups = userGroups;
	}

	public String getManagement() {
		return management;
	}

	public void setManagement(String management) {
		this.management = management;
	}

	public String getGeneralResources() {
		return generalResources;
	}

	public void setGeneralResources(String generalResources) {
		this.generalResources = generalResources;
	}

	public String getSocialCommunity() {
		return socialCommunity;
	}

	public void setSocialCommunity(String socialCommunity) {
		this.socialCommunity = socialCommunity;
	}

	public String getOtherDetails() {
		return otherDetails;
	}

	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	public String getLocalLanguage() {
		return localLanguage;
	}

	public void setLocalLanguage(String localLanguage) {
		this.localLanguage = localLanguage;
	}

	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAssociatedTk() {
		return associatedTk;
	}

	public void setAssociatedTk(String associatedTk) {
		this.associatedTk = associatedTk;
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

	

	

	public boolean isCrowdData() {
		return crowdData;
	}

	public void setCrowdData(boolean crowdData) {
		this.crowdData = crowdData;
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

	public MasterApproval getApproved() {
		return approved;
	}

	public void setApproved(MasterApproval approved) {
		this.approved = approved;
	}

	public String getWetLand() {
		return wetLand;
	}

	public void setWetLand(String wetLand) {
		this.wetLand = wetLand;
	}
	
	
	
}
