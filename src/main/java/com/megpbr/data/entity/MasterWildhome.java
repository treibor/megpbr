package com.megpbr.data.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

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
@Table(name="MasterWildhome", schema = "megpbr")
public class MasterWildhome {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wildhome_generator")
	@SequenceGenerator(name="wildhome_generator", sequenceName = "wildhome_seq", allocationSize=1)
	private long id;
	@Length(min=1, max = 255, message="Character Limit Exceeded")
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
