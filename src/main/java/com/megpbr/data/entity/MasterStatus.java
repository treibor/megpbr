package com.megpbr.data.entity;

import java.util.List;

import com.megpbr.data.entity.pbr.Crops;

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
@Table(name="MasterStatus", schema = "megpbr")
public class MasterStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_generator")
	@SequenceGenerator(name="status_generator", sequenceName = "status_seq", allocationSize=1)
	private long id;
	private String status;
	private String statusName;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	
}
