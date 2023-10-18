package com.megpbr.data.entity.pbr;

import java.time.LocalDateTime;

import com.megpbr.data.entity.CrowdFormat;
import com.megpbr.data.entity.CrowdType;
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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Crowd", schema = "megpbr")
public class Crowd {
	//For format 11-15
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crowd_generator")
	@SequenceGenerator(name="crowd_generator", sequenceName = "crowd_seq", allocationSize=1)
	private long id;
	private String localName;
	private String localLanguage;
	private String season;
	private String uses;
	private byte[] photo1;
	private byte[] photo2;
	private byte[] photo3;
	private byte[] photo4;
	private boolean preverified;
	private boolean verified;
	private LocalDateTime enteredOn;
	private String latitude;
	private String longitude;
	private String userName;
	private String userAddress;
	private String userPhone;
	private String userEmail;
	
	@ManyToOne
	@JoinColumn(name = "crowdformat", referencedColumnName = "id")
	private CrowdFormat crowdformat;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "village", referencedColumnName = "villageCode")
	private Village village;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "type", referencedColumnName = "id")
	private CrowdType type;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public String getLocalLanguage() {
		return localLanguage;
	}
	public void setLocalLanguage(String localLanguage) {
		this.localLanguage = localLanguage;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getUses() {
		return uses;
	}
	public void setUses(String uses) {
		this.uses = uses;
	}
	public byte[] getPhoto1() {
		return photo1;
	}
	public void setPhoto1(byte[] photo1) {
		this.photo1 = photo1;
	}
	public byte[] getPhoto2() {
		return photo2;
	}
	public void setPhoto2(byte[] photo2) {
		this.photo2 = photo2;
	}
	public byte[] getPhoto3() {
		return photo3;
	}
	public void setPhoto3(byte[] photo3) {
		this.photo3 = photo3;
	}
	public byte[] getPhoto4() {
		return photo4;
	}
	public void setPhoto4(byte[] photo4) {
		this.photo4 = photo4;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public CrowdFormat getCrowdformat() {
		return crowdformat;
	}
	public void setCrowdformat(CrowdFormat crowdformat) {
		this.crowdformat = crowdformat;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public CrowdType getType() {
		return type;
	}
	public void setType(CrowdType type) {
		this.type = type;
	}
	public boolean isPreverified() {
		return preverified;
	}
	public void setPreverified(boolean preverified) {
		this.preverified = preverified;
	}
	
	
	
	
}
