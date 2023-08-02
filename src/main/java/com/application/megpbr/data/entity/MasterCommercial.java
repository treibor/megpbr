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
public class MasterCommercial {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comid_generator")
	@SequenceGenerator(name="comid_generator", sequenceName = "comid_seq", allocationSize=1)
	private long id;
	private String cname;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "commercial")
	private List<Crops> crops;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public List<Crops> getCrops() {
		return crops;
	}
	public void setCrops(List<Crops> crops) {
		this.crops = crops;
	}
	
	
}
