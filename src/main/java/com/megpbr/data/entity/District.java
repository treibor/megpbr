package com.megpbr.data.entity;


import java.util.List;

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
import jakarta.validation.constraints.Size;


@Entity
@Table(name="District", schema = "megpbr")
public class District {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "district_generator")
	@SequenceGenerator(name="district_generator", sequenceName = "district_seq", allocationSize=1)
	private long id;
	@NotBlank
	private String districtName;
	@Column(unique=true)
	private long districtCode;
	@NotNull
	@ManyToOne
	@JoinColumn(name="state", referencedColumnName = "stateCode")
	
	private State state;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "district")
	List<Block> block;
	
	public int getVillageCount() {
        return block.stream().mapToInt(block -> block.getVillage().size()).sum();
    }
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public long getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(long districtCode) {
		this.districtCode = districtCode;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public List<Block> getBlock() {
		return block;
	}
	public void setBlock(List<Block> block) {
		this.block = block;
	}
	
	
	
	
}
