package com.application.megpbr.data.entity;

import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Block {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "block_generator")
	@SequenceGenerator(name="block_generator", sequenceName = "block_seq", allocationSize=1)
	private long id;
	private long blockCode;
	@NotEmpty
	private String blockName;
	@ManyToOne
	@JoinColumn(name="district", referencedColumnName = "id")
	@NotBlank
	private District district;
	
	
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
	
	
}
