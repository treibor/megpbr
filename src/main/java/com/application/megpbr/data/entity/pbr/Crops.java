package com.application.megpbr.data.entity.pbr;

import java.time.LocalDateTime;

import com.application.megpbr.data.entity.MasterFormat;
import com.application.megpbr.data.entity.MasterStatus;
import com.application.megpbr.data.entity.Village;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Crops {
	//For format 11-15
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cropid_generator")
	@SequenceGenerator(name="cropid_generator", sequenceName = "cropid_seq", allocationSize=1)
	private long id;
	private String type;
	@NotBlank(message = "Scientific Name Cannot Be Blank")
	private String scientificName;
	private String localName;
	private String variety;
	private String habitat;
	//private String pastStatus;
	//private String presentStatus;
	private String otherDetails;
	private String localLanguage;
	private String area;
	private String fruitSeason;
	private String commercial;
	private String uses;
	private String specialFeatures;
	//common fields
	private String associatedTdk;
	private String knowledgeHolder;
	private String source;
	private String photo1;
	private String photo1Source;
	private String photo2;
	private String photo2Source;
	private String photo3;
	private String photo3Source;
	private String photo4;
	private String photo4Source;
	private boolean master;
	private boolean verified;
	private String enteredBy;
	private LocalDateTime enteredOn;
	private String latitude;
	private String longitude;
	
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
	@JoinColumn(name = "village", referencedColumnName = "id")
	private Village village;
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
	public String getCommercial() {
		return commercial;
	}
	public void setCommercial(String commercial) {
		this.commercial = commercial;
	}
	public String getUses() {
		return uses;
	}
	public void setUses(String uses) {
		this.uses = uses;
	}
	public String getAssociatedTdk() {
		return associatedTdk;
	}
	public void setAssociatedTdk(String associatedTdk) {
		this.associatedTdk = associatedTdk;
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
	public String getPhoto1() {
		return photo1;
	}
	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}
	public String getPhoto1Source() {
		return photo1Source;
	}
	public void setPhoto1Source(String photo1Source) {
		this.photo1Source = photo1Source;
	}
	public String getPhoto2() {
		return photo2;
	}
	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}
	public String getPhoto2Source() {
		return photo2Source;
	}
	public void setPhoto2Source(String photo2Source) {
		this.photo2Source = photo2Source;
	}
	public String getPhoto3() {
		return photo3;
	}
	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}
	public String getPhoto3Source() {
		return photo3Source;
	}
	public void setPhoto3Source(String photo3Source) {
		this.photo3Source = photo3Source;
	}
	public String getPhoto4() {
		return photo4;
	}
	public void setPhoto4(String photo4) {
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
	public String getSpecialFeatures() {
		return specialFeatures;
	}
	public void setSpecialFeatures(String specialFeatures) {
		this.specialFeatures = specialFeatures;
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
	
	
		
}
