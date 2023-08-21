package com.application.megpbr.data.entity;

import java.util.List;

import com.application.megpbr.data.entity.pbr.Crops;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="MasterFormat", schema = "megpbr")
public class MasterFormat {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forid_generator")
	@SequenceGenerator(name="forid_generator", sequenceName = "forid_seq", allocationSize=1)
	private long id;
	private int format;
	private String formatName;
	@ManyToOne
	@JoinColumn(name="category", referencedColumnName = "id")
	@NotNull
	private MasterCategory category;
	
	//change this
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "format")
	private List<Crops> crops;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	

	public int getFormat() {
		return format;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public MasterCategory getCategory() {
		return category;
	}

	public void setCategory(MasterCategory category) {
		this.category = category;
	}

	public List<Crops> getCrops() {
		return crops;
	}

	public void setCrops(List<Crops> crops) {
		this.crops = crops;
	}
	
	
	
}
