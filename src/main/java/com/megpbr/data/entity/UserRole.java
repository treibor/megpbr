package com.megpbr.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="UserRoles", schema = "megpbr")
public class UserRole {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userroles_generator")
	@SequenceGenerator(name="userroles_generator", sequenceName = "userroles_seq", allocationSize=1)
	private long id;
	private String  roleName;
	@ManyToOne
	@JoinColumn(name="users", referencedColumnName = "id")
	
	private UserLogin user;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public UserLogin getUser() {
		return user;
	}
	public void setUser(UserLogin user) {
		this.user = user;
	}
	
	
}
