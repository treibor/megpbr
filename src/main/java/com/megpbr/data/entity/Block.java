package com.megpbr.data.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Block", schema = "megpbr")
public class Block {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "block_generator")
	@SequenceGenerator(name="block_generator", sequenceName = "block_seq", allocationSize=1)
	private long id;
	@Column(unique=true)
	
	private long blockCode;
	@NotEmpty
	@Length(max = 255, message="Character Limit Exceeded")
	private String blockName;
	@ManyToOne
	@JoinColumn(name="district", referencedColumnName = "districtCode")
	@NotNull
	private District district;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "block")
	private List<Village> village;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getBlockCode() {
		return blockCode;
	}
	public void setBlockCode(long blockCode) {
		this.blockCode = blockCode;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	public List<Village> getVillage() {
		return village;
	}
	public void setVillage(List<Village> village) {
		this.village = village;
	}
	
	
}
