package com.megpbr.data.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
@Entity
@Table(name="MasterGender", schema = "megpbr")
public class MasterGender {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gender_generator")
	@SequenceGenerator(name="gender_generator", sequenceName = "gender_seq", allocationSize=1)
	private long id;
	@Length(min=1, max = 255, message="Character Limit Exceeded")
	private String genderName;
	@Length(min=1, max = 255, message="Character Limit Exceeded")
	private String gender;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGenderName() {
		return genderName;
	}
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
}
