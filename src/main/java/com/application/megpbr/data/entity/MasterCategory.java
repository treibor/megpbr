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
@Table(name="MasterCategory", schema = "megpbr")
public class MasterCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "catid_generator")
	@SequenceGenerator(name="catid_generator", sequenceName = "catid_seq", allocationSize=1)
	private long id;
	private String category;
	@OneToMany(mappedBy = "category")
	private List<MasterFormat> format;
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
	public List<MasterFormat> getFormat() {
		return format;
	}
	public void setFormat(List<MasterFormat> format) {
		this.format = format;
	}
	
	
	
}
