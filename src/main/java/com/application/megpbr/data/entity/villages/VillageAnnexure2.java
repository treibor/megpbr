package com.application.megpbr.data.entity.villages;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.application.megpbr.data.entity.MasterGender;
import com.application.megpbr.data.entity.MasterPosition;
import com.application.megpbr.data.entity.Village;

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
@Table(name="Annexure2", schema = "megpbr")
public class VillageAnnexure2 {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "annex2_generator")
	@SequenceGenerator(name="annex2_generator", sequenceName = "annex2_seq", allocationSize=1)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="village", referencedColumnName = "villageCode")
	@NotNull(message = "Please Select a Village")
	private Village village;
	@NotEmpty(message = "Please Enter The Name")
	private String name;
	private int age;
	@ManyToOne
	@NotNull(message = "Please Select the Gender")
	@JoinColumn(name="gender", referencedColumnName = "id")
	private MasterGender gender;
	private String address;
	private String specialization;
	private String location;
	private String perception;
	private String medicinalUse;
	
	private String remarks;
	private String enteredBy;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	
	public MasterGender getGender() {
		return gender;
	}
	public void setGender(MasterGender gender) {
		this.gender = gender;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPerception() {
		return perception;
	}
	public void setPerception(String perception) {
		this.perception = perception;
	}
	public String getMedicinalUse() {
		return medicinalUse;
	}
	public void setMedicinalUse(String medicinalUse) {
		this.medicinalUse = medicinalUse;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
}
