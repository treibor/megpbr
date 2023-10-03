package com.megpbr.data.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="CrowdCategory", schema = "megpbr")
public class CrowdCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crowdCategory_generator")
	@SequenceGenerator(name="crowdCategory_generator", sequenceName = "CrowdCategory_seq", allocationSize=1)
	private long id;
	private String category;
	@OneToMany(mappedBy = "category")
	private List<CrowdFormat> crowdformat;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<CrowdFormat> getCrowdformat() {
		return crowdformat;
	}
	public void setCrowdformat(List<CrowdFormat> crowdformat) {
		this.crowdformat = crowdformat;
	}
	
	
	
}
