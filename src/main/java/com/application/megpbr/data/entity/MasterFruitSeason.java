package com.application.megpbr.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
@Entity
public class MasterFruitSeason {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "season_generator")
	@SequenceGenerator(name="season_generator", sequenceName = "season_seq", allocationSize=1)
	private long id;
	private String fruitseason;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFruitseason() {
		return fruitseason;
	}
	public void setFruitseason(String fruitseason) {
		this.fruitseason = fruitseason;
	}
	
}
