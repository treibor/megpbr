package com.application.megpbr.data.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="UserLogin", schema = "megpbr")


public class UserLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "state_generator")
	@SequenceGenerator(name="state_generator", sequenceName = "state_seq", allocationSize=1)
	private long id;
	@Column(unique=true)
	private String  userName;
	@NotEmpty
	private String userPassword;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "userlogin")
	public List<UserRole> userrole;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public List<UserRole> getUserrole() {
		return userrole;
	}
	public void setUserrole(List<UserRole> userrole) {
		this.userrole = userrole;
	}
	
	
	
	
}
