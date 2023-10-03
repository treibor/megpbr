package com.megpbr.data.entity;

import java.util.List;

import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Crowd;

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
@Table(name="CrowdFormat", schema = "megpbr")
public class CrowdFormat {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crowdformat_generator")
	@SequenceGenerator(name="crowdformat_generator", sequenceName = "crowdformat_seq", allocationSize=1)
	private long id;
	private int format;
	private String formatName;
	@ManyToOne
	@JoinColumn(name="category", referencedColumnName = "id")
	@NotNull
	private CrowdCategory category;
	
	//change this
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "crowdformat")
	private List<Crowd> crowd;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getFormat() {
		return format;
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public CrowdCategory getCategory() {
		return category;
	}

	public void setCategory(CrowdCategory category) {
		this.category = category;
	}

	public List<Crowd> getCrowd() {
		return crowd;
	}

	public void setCrowd(List<Crowd> crowd) {
		this.crowd = crowd;
	}

	
	
}
