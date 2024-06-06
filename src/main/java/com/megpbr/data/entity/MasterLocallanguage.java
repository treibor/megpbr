package com.megpbr.data.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="MasterLocallanguage", schema = "megpbr")
public class MasterLocallanguage {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "llanguage_generator")
	@SequenceGenerator(name="llanguage_generator", sequenceName = "llanguage_seq", allocationSize=1)
	private long id;
	@NotBlank
	@Length(min=1, max = 255, message="Character Limit Exceeded")
	private String languageName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	
}
