package com.megpbr.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
@Entity
@Table(name="MasterPosition", schema = "megpbr")
public class MasterPosition {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "position_generator")
	@SequenceGenerator(name="position_generator", sequenceName = "position_seq", allocationSize=1)
	private long id;
	private String positionName;
	private String position;
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	
	
}
