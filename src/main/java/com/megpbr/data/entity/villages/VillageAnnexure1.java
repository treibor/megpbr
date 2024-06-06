package com.megpbr.data.entity.villages;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;

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
@Table(name="Annexure1", schema = "megpbr")
public class VillageAnnexure1 {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "annex1_generator")
	@SequenceGenerator(name="annex1_generator", sequenceName = "annex1_seq", allocationSize=1)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="village", referencedColumnName = "villageCode")
	@NotNull(message = "Please Select a Village")
	private Village village;
	@NotNull(message = "Please Enter The Name")
	@Length(min=1, max = 255, message="Character Limit Exceeded")
	private String name;
	private int age;
	@ManyToOne
	@NotNull(message = "Please Select the Gender")
	@JoinColumn(name="gender", referencedColumnName = "id")
	private MasterGender gender;
	@Length(max = 255, message="Character Limit Exceeded")
	private String address;
	@Length( max = 255, message="Character Limit Exceeded")
	private String specialization;
	@Length(max = 255, message="Character Limit Exceeded")
	private String remarks;
	@ManyToOne
	@JoinColumn(name="position", referencedColumnName = "id")
	private MasterPosition position;
	private LocalDate tenureDate;
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
	public MasterPosition getPosition() {
		return position;
	}
	public void setPosition(MasterPosition position) {
		this.position = position;
	}
	public LocalDate getTenureDate() {
		return tenureDate;
	}
	public void setTenureDate(LocalDate tenureDate) {
		this.tenureDate = tenureDate;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
}
