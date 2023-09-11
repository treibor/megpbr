package com.megpbr.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
@Entity
@Table(name="MasterApproval", schema = "megpbr")
public class MasterApproval {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approv_generator")
	@SequenceGenerator(name="approv_generator", sequenceName = "approv_seq", allocationSize=1)
	private long id;
	private String approvalName;
	private String approval;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getApprovalName() {
		return approvalName;
	}
	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}
	public String getApproval() {
		return approval;
	}
	public void setApproval(String approval) {
		this.approval = approval;
	}
	
	
	
	
	
}
