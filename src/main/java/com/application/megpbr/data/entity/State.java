package com.application.megpbr.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity

public class State {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "state_generator")
	@SequenceGenerator(name="state_generator", sequenceName = "state_seq", allocationSize=1)
	private long id;
	private long stateCode;
	@Column(unique=true)
	private String stateName;
	public long getId() {
		return id;
	}
	public long getStateCode() {
		return stateCode;
	}
	public void setStateCode(long stateCode) {
		this.stateCode = stateCode;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	
}
