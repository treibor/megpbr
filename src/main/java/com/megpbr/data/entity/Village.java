package com.megpbr.data.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Crowd;
import com.megpbr.data.entity.pbr.Markets;
import com.megpbr.data.entity.pbr.Scapes;
import com.megpbr.data.entity.villages.VillageAnnexure1;
import com.megpbr.data.entity.villages.VillageAnnexure2;
import com.megpbr.data.entity.villages.VillageAnnexure3;
import com.megpbr.data.entity.villages.VillageAnnexure4;
import com.megpbr.data.entity.villages.VillageAnnexure5;
import com.megpbr.data.entity.villages.VillageDetails;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="Village", schema = "megpbr")
public class Village {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "village_generator")
	@SequenceGenerator(name="village_generator", sequenceName = "village_seq", allocationSize=1)
	private long id;
	@Column(unique=true)

	private long villageCode;
	@Length(min=1, max = 255, message="Character Limit Exceeded")
	private String villageName;
	@ManyToOne
	@JoinColumn(name="block", referencedColumnName = "blockCode")
	private Block block;
	private boolean inUse;
	@OneToMany(mappedBy = "village")
	private List<VillageDetails> villagedetails;
	@OneToMany(mappedBy = "village")
	private List<VillageAnnexure1> annexure1;
	@OneToMany(mappedBy = "village")
	private List<VillageAnnexure2> annexure2;
	@OneToMany(mappedBy = "village")
	private List<VillageAnnexure3> annexure3;
	@OneToMany(mappedBy = "village")
	private List<VillageAnnexure4> annexure4;
	@OneToMany(mappedBy = "village")
	private List<VillageAnnexure5> annexure5;
	@OneToMany(mappedBy = "village")
	private List<Crops> crops;
	@OneToMany(mappedBy = "village")
	private List<Scapes> scapes;
	@OneToMany(mappedBy = "village")
	private List<Markets> markets;
	
	@OneToMany(mappedBy = "village")
	private List<Crowd> crowd;
	
	
	
	public List<Crowd> getCrowd() {
		return crowd;
	}
	public void setCrowd(List<Crowd> crowd) {
		this.crowd = crowd;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getVillageCode() {
		return villageCode;
	}
	public void setVillageCode(long villageCode) {
		this.villageCode = villageCode;
	}
	public String getVillageName() {
		return villageName;
	}
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	public Block getBlock() {
		return block;
	}
	public void setBlock(Block block) {
		this.block = block;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	public List<VillageDetails> getVillagedetails() {
		return villagedetails;
	}
	public void setVillagedetails(List<VillageDetails> villagedetails) {
		this.villagedetails = villagedetails;
	}
	public List<VillageAnnexure1> getAnnexure1() {
		return annexure1;
	}
	public void setAnnexure1(List<VillageAnnexure1> annexure1) {
		this.annexure1 = annexure1;
	}
	public List<VillageAnnexure2> getAnnexure2() {
		return annexure2;
	}
	public void setAnnexure2(List<VillageAnnexure2> annexure2) {
		this.annexure2 = annexure2;
	}
	public List<VillageAnnexure3> getAnnexure3() {
		return annexure3;
	}
	public void setAnnexure3(List<VillageAnnexure3> annexure3) {
		this.annexure3 = annexure3;
	}
	public List<VillageAnnexure4> getAnnexure4() {
		return annexure4;
	}
	public void setAnnexure4(List<VillageAnnexure4> annexure4) {
		this.annexure4 = annexure4;
	}
	public List<VillageAnnexure5> getAnnexure5() {
		return annexure5;
	}
	public void setAnnexure5(List<VillageAnnexure5> annexure5) {
		this.annexure5 = annexure5;
	}
	public List<Crops> getCrops() {
		return crops;
	}
	public void setCrops(List<Crops> crops) {
		this.crops = crops;
	}
	public List<Scapes> getScapes() {
		return scapes;
	}
	public void setScapes(List<Scapes> scapes) {
		this.scapes = scapes;
	}
	public List<Markets> getMarkets() {
		return markets;
	}
	public void setMarkets(List<Markets> markets) {
		this.markets = markets;
	}
	
	
	
	
}
