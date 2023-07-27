package com.application.megpbr.data.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class District {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "district_generator")
	@SequenceGenerator(name="district_generator", sequenceName = "district_seq", allocationSize=1)
	private long id;
	@NotBlank
	private String districtName;
	private long districtCode;
	@NotBlank
	@ManyToOne
	@JoinColumn(name="state", referencedColumnName = "id")
	@NotNull
	private State state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public long getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(long districtCode) {
		this.districtCode = districtCode;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	
	
}
