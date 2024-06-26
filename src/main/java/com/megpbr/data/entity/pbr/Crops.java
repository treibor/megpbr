package com.megpbr.data.entity.pbr;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

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
@Table(name="Crops", schema = "megpbr")
public class Crops {
	//For format 11-15
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crop_generator")
	@SequenceGenerator(name="crop_generator", sequenceName = "crop_seq", allocationSize=1)
	private long id;
	@Length(max = 255, message="Character Limit Exceeded")
	private String type;
	@NotBlank(message = "Scientific Name Cannot Be Blank")
	@Length(max = 255, message="Character Limit Exceeded")
	private String scientificName;
	@Length(max = 255, message="Character Limit Exceeded")
	private String localName;
	@Length(max = 255, message="Character Limit Exceeded")
	private String variety;
	@Length(max = 255, message="Character Limit Exceeded")
	private String habitat;
	@Length(max = 255, message="Character Limit Exceeded")
	private String partsUsed;
	//private String presentStatus;
	@Length(max = 255, message="Character Limit Exceeded")
	private String otherDetails;
	@Length(max = 255, message="Character Limit Exceeded")
	private String localLanguage;
	@Length(max = 255, message="Character Limit Exceeded")
	private String area;
	@Length(max = 255, message="Character Limit Exceeded")
	private String fruitSeason;
	@Length(max = 255, message="Character Limit Exceeded")
	//private String commercial;
	@Length(max = 255, message="Character Limit Exceeded")
	private String uses;
	 @Column(length = 1000)
	 @Length(max = 999, message="Character Limit Exceeded")
	private String specialFeatures;
	//common fields
	 @Column(length = 1000)
	 @Length(max = 999, message="Character Limit Exceeded")
	private String associatedTk;
	 @Length(max = 255, message="Character Limit Exceeded")
	private String knowledgeHolder;
	 @Length(max = 255, message="Character Limit Exceeded")
	private String source;
	private byte[] photo1;
	@Length(max = 255, message="Character Limit Exceeded")
	private String photo1Source;
	private byte[] photo2;
	@Length(max = 255, message="Character Limit Exceeded")
	private String photo2Source;
	private byte[] photo3;
	@Length(max = 255, message="Character Limit Exceeded")
	private String photo3Source;
	private byte[] photo4;
	@Length(max = 255, message="Character Limit Exceeded")
	private String photo4Source;
	private boolean master;
	private boolean crowdData;
	//private String enteredBy;
	private LocalDateTime enteredOn;
	@Length(max = 255, message="Character Limit Exceeded")
	private String latitude;
	@Length(max = 255, message="Character Limit Exceeded")
	private String longitude;
	@Length(max = 255, message="Character Limit Exceeded")
	private String management;
	@Length(max = 255, message="Character Limit Exceeded")
	private String xfield1;
	@Length(max = 255, message="Character Limit Exceeded")
	private String xfield2;
	@Length(max = 255, message="Character Limit Exceeded")
	private String remarks;
	@ManyToOne
	@JoinColumn(name = "format", referencedColumnName = "id")
	private MasterFormat format;
	@ManyToOne
	@JoinColumn(name = "pastStatus", referencedColumnName = "id")
	private MasterStatus pastStatus;
	@ManyToOne
	@JoinColumn(name = "presentStatus", referencedColumnName = "id")
	private MasterStatus presentStatus;
	@ManyToOne
	@JoinColumn(name = "village", referencedColumnName = "villageCode")
	private Village village;
	@ManyToOne
	@JoinColumn(name = "state", referencedColumnName = "stateCode")
	private State state;
	@ManyToOne
	@JoinColumn(name = "commercial", referencedColumnName = "id")
	private MasterCommercial commercial;
	@ManyToOne
	@JoinColumn(name = "wildhome", referencedColumnName = "id")
	private MasterWildhome wildhome;
	
	@ManyToOne
	@JoinColumn(name = "approved", referencedColumnName = "id")
	private MasterApproval approved;
	@ManyToOne
	@JoinColumn(name = "enteredBy", referencedColumnName = "id")
	private UserLogin enteredBy;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScientificName() {
		return scientificName;
	}
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public String getVariety() {
		return variety;
	}
	public void setVariety(String variety) {
		this.variety = variety;
	}
	public String getHabitat() {
		return habitat;
	}
	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}
	public String getPartsUsed() {
		return partsUsed;
	}
	public void setPartsUsed(String partsUsed) {
		this.partsUsed = partsUsed;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getFruitSeason() {
		return fruitSeason;
	}
	public void setFruitSeason(String fruitSeason) {
		this.fruitSeason = fruitSeason;
	}
	public String getUses() {
		return uses;
	}
	public void setUses(String uses) {
		this.uses = uses;
	}
	public String getSpecialFeatures() {
		return specialFeatures;
	}
	public void setSpecialFeatures(String specialFeatures) {
		this.specialFeatures = specialFeatures;
	}
	public String getAssociatedTk() {
		return associatedTk;
	}
	public void setAssociatedTk(String associatedTk) {
		this.associatedTk = associatedTk;
	}
	public String getKnowledgeHolder() {
		return knowledgeHolder;
	}
	public void setKnowledgeHolder(String knowledgeHolder) {
		this.knowledgeHolder = knowledgeHolder;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public boolean isCrowdData() {
		return crowdData;
	}
	public void setCrowdData(boolean crowdData) {
		this.crowdData = crowdData;
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
	public String getManagement() {
		return management;
	}
	public void setManagement(String management) {
		this.management = management;
	}
	public String getXfield1() {
		return xfield1;
	}
	public void setXfield1(String xfield1) {
		this.xfield1 = xfield1;
	}
	public String getXfield2() {
		return xfield2;
	}
	public void setXfield2(String xfield2) {
		this.xfield2 = xfield2;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public MasterFormat getFormat() {
		return format;
	}
	public void setFormat(MasterFormat format) {
		this.format = format;
	}
	public MasterStatus getPastStatus() {
		return pastStatus;
	}
	public void setPastStatus(MasterStatus pastStatus) {
		this.pastStatus = pastStatus;
	}
	public MasterStatus getPresentStatus() {
		return presentStatus;
	}
	public void setPresentStatus(MasterStatus presentStatus) {
		this.presentStatus = presentStatus;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public MasterCommercial getCommercial() {
		return commercial;
	}
	public void setCommercial(MasterCommercial commercial) {
		this.commercial = commercial;
	}
	public MasterWildhome getWildhome() {
		return wildhome;
	}
	public void setWildhome(MasterWildhome wildhome) {
		this.wildhome = wildhome;
	}
	public MasterApproval getApproved() {
		return approved;
	}
	public void setApproved(MasterApproval approved) {
		this.approved = approved;
	}
	public UserLogin getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(UserLogin enteredBy) {
		this.enteredBy = enteredBy;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	
	
	
	
}
