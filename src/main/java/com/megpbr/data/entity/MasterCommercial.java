package com.megpbr.data.entity;

import java.util.List;

import com.megpbr.data.entity.pbr.Crops;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="MasterCommercial", schema = "megpbr")
public class MasterCommercial {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commercial_generator")
	@SequenceGenerator(name="commercial_generator", sequenceName = "commercial_seq", allocationSize=1)
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
