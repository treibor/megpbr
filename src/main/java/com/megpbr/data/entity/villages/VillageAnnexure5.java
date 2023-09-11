package com.megpbr.data.entity.villages;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.megpbr.data.entity.MasterGender;
import com.megpbr.data.entity.MasterPosition;
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
@Table(name="Annexure5", schema = "megpbr")
public class VillageAnnexure5 {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "annex5_generator")
	@SequenceGenerator(name="annex5_generator", sequenceName = "annex5_seq", allocationSize=1)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="village", referencedColumnName = "villageCode")
	@NotNull(message = "Please Select a Village")
	private Village village;
	@NotEmpty(message = "Please Enter The Name")
	private String name;
	private String address;
	private String localName;
	private String scientificName;
	//private String specialization;
	private int quantity=0; 
	private LocalDate resolutionDate;
	private String feeCollection;
	private String anticipatedMode;
	private byte[] feeCollectionPhoto;
	private byte[] resolutionPhoto;
	@ManyToOne
	@JoinColumn(name = "enteredBy", referencedColumnName = "id")
	private UserLogin enteredBy;
    private LocalDateTime enteredOn;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public String getScientificName() {
		return scientificName;
	}
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public LocalDate getResolutionDate() {
		return resolutionDate;
	}
	public void setResolutionDate(LocalDate resolutionDate) {
		this.resolutionDate = resolutionDate;
	}
	public String getFeeCollection() {
		return feeCollection;
	}
	public void setFeeCollection(String feeCollection) {
		this.feeCollection = feeCollection;
	}
	public String getAnticipatedMode() {
		return anticipatedMode;
	}
	public void setAnticipatedMode(String anticipatedMode) {
		this.anticipatedMode = anticipatedMode;
	}
	public byte[] getFeeCollectionPhoto() {
		return feeCollectionPhoto;
	}
	public void setFeeCollectionPhoto(byte[] feeCollectionPhoto) {
		this.feeCollectionPhoto = feeCollectionPhoto;
	}
	public byte[] getResolutionPhoto() {
		return resolutionPhoto;
	}
	public void setResolutionPhoto(byte[] resolutionPhoto) {
		this.resolutionPhoto = resolutionPhoto;
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
	
	
	
	
}
