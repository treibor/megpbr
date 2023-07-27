package com.application.megpbr.data.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MasterCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "catid_generator")
	@SequenceGenerator(name="catid_generator", sequenceName = "catid_seq", allocationSize=1)
	private long id;
	private String category;
	@OneToMany(mappedBy = "category")
	private List<MasterFormat> format;
}
