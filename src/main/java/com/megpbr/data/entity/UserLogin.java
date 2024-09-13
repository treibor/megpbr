package com.megpbr.data.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "UserLogin", schema = "megpbr")
public class UserLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userlogin_generator")
	@SequenceGenerator(name = "userlogin_generator", sequenceName = "userlogin_seq", allocationSize = 1)
	private long id;
	@Column(unique = true)
	@Length(min = 1, max = 255, message = "Character Limit Exceeded")
	private String userName;
	@Length(min = 1, max = 255, message = "Character Limit Exceeded")
	private String name;
	@Length(min = 1, max = 255, message = "Character Limit Exceeded")
	@Column(unique = true)
	//@NotEmpty
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email address")
	@Length(min = 0, max = 30, message = "Character Limit Exceeded")
	private String email;
	private boolean enabled;
	// @JsonIgnore
	private String hashedPassword;
	private String oldPassword1;
	private String oldPassword2;
	private String oldPassword3;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private List<UserRole> Roles;
	@Column(length = 1000000)
	private byte[] profilePicture;
	@ManyToOne
	@JoinColumn(name = "level", referencedColumnName = "id")
	@NotNull
	private UserLoginLevel level;
	@ManyToOne
	@JoinColumn(name = "village", referencedColumnName = "villageCode")
	private Village village;
	@ManyToOne
	@JoinColumn(name = "block", referencedColumnName = "blockCode")
	private Block block;
	@ManyToOne
	@JoinColumn(name = "district", referencedColumnName = "districtCode")
	private District district;
	@ManyToOne
	@JoinColumn(name = "state", referencedColumnName = "stateCode")
	private State state;
	private LocalDate pwdChangedDate;

	public LocalDate getPwdChangedDate() {
		return pwdChangedDate;
	}

	public void setPwdChangedDate(LocalDate pwdChangedDate) {
		this.pwdChangedDate = pwdChangedDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<UserRole> getRoles() {
		return Roles;
	}

	public void setRoles(List<UserRole> roles) {
		Roles = roles;
	}

	public UserLoginLevel getLevel() {
		return level;
	}

	public void setLevel(UserLoginLevel level) {
		this.level = level;
	}

	public Village getVillage() {
		return village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getOldPassword1() {
		return oldPassword1;
	}

	public void setOldPassword1(String oldPassword1) {
		this.oldPassword1 = oldPassword1;
	}

	public String getOldPassword2() {
		return oldPassword2;
	}

	public void setOldPassword2(String oldPassword2) {
		this.oldPassword2 = oldPassword2;
	}

	public String getOldPassword3() {
		return oldPassword3;
	}

	public void setOldPassword3(String oldPassword3) {
		this.oldPassword3 = oldPassword3;
	}

}
