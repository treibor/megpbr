package com.application.megpbr.data.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="UserLevel", schema = "megpbr")
public class UserLoginLevel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "levelid_generator")
	@SequenceGenerator(name="levelid_generator", sequenceName = "levelid_seq", allocationSize=1)
	private long id;
	private int level;
	private String levelName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
	
}
