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
@Table(name="Annexure4", schema = "megpbr")
public class VillageAnnexure4 {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "annex4_generator")
	@SequenceGenerator(name="annex4_generator", sequenceName = "annex4_seq", allocationSize=1)
	private long id;
	@ManyToOne
	@JoinColumn(name="village", referencedColumnName = "villageCode")
	@NotNull(message = "Please Select a Village")
	private Village village;
	@NotEmpty(message = "Please Enter The Name")
	private String name;
	private String address;
	private String institute;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInstitute() {
		return institute;
	}
	public void setInstitute(String institute) {
		this.institute = institute;
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
	
	
	
	
	
}
