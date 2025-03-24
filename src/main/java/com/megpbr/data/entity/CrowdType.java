package com.megpbr.data.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

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
@Table(name="CrowdType", schema = "megpbr")
public class CrowdType {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crowdtype_generator")
	@SequenceGenerator(name="crowdtype_generator", sequenceName = "crowdtype_seq", allocationSize=1)
	private long id;
	private int type;
	@Length(max = 255, message="Character Limit Exceeded")
	private String typeName;
	@ManyToOne
	@JoinColumn(name="category", referencedColumnName = "id")
	@NotNull
	private CrowdCategory category;
	
	//change this
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "type")
	private List<Crowd> crowd;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<Crowd> getCrowd() {
		return crowd;
	}

	public void setCrowd(List<Crowd> crowd) {
		this.crowd = crowd;
	}

	public CrowdCategory getCategory() {
		return category;
	}

	public void setCategory(CrowdCategory category) {
		this.category = category;
	}
	
		
}
