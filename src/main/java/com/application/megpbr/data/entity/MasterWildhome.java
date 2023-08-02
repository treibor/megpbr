package com.application.megpbr.data.entity;

import java.util.List;

import com.application.megpbr.data.entity.pbr.Crops;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MasterWildhome {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wildid_generator")
	@SequenceGenerator(name="wildid_generator", sequenceName = "wildid_seq", allocationSize=1)
	private long id;
	private String wname;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wildhome")
	private List<Crops> crops;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getWname() {
		return wname;
	}
	public void setWname(String wname) {
		this.wname = wname;
	}
	public List<Crops> getCrops() {
		return crops;
	}
	public void setCrops(List<Crops> crops) {
		this.crops = crops;
	}
	
	
	
}
