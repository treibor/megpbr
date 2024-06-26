package com.megpbr.data.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.villages.VillageDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="MasterManagementRegime", schema = "megpbr")
public class MasterManagementRegime {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regime_generator")
	@SequenceGenerator(name="regime_generator", sequenceName = "regime_seq", allocationSize=1)
	private long id;
	@Length(min=1, max = 255, message="Character Limit Exceeded")
	private String managementregime;
	@Length(min=1, max = 255, message="Character Limit Exceeded")
	private String managerregime;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "managementregime")
	private List<VillageDetails> villagedetails;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getManagementregime() {
		return managementregime;
	}
	public void setManagementregime(String managementregime) {
		this.managementregime = managementregime;
	}
	public List<VillageDetails> getVillagedetails() {
		return villagedetails;
	}
	public void setVillagedetails(List<VillageDetails> villagedetails) {
		this.villagedetails = villagedetails;
	}
	public String getManagerregime() {
		return managerregime;
	}
	public void setManagerregime(String managerregime) {
		this.managerregime = managerregime;
	}
	
	
	
	
}
