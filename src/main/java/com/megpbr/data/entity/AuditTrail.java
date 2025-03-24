package com.megpbr.data.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="AuditTrail", schema = "megpbr")
public class AuditTrail {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_generator")
	@SequenceGenerator(name="audit_generator", sequenceName = "audit_seq", allocationSize=1)
	private long id;
	private String action;
	private String ipAddress;
	private String actionBy;
	private LocalDateTime actionOn;
	@Column(length = 1000)
	private String details;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getActionBy() {
		return actionBy;
	}
	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}
	public LocalDateTime getActionOn() {
		return actionOn;
	}
	public void setActionOn(LocalDateTime actionOn) {
		this.actionOn = actionOn;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	
}
