package com.megpbr.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.megpbr.data.entity.CrowdCategory;
import com.megpbr.data.entity.CrowdFormat;
import com.megpbr.data.entity.CrowdType;
import com.megpbr.data.entity.pbr.Crowd;
import com.megpbr.data.repository.CrowdCategoryRepository;
import com.megpbr.data.repository.CrowdFormatRepository;
import com.megpbr.data.repository.CrowdTypeRepository;
import com.megpbr.data.repository.pbr.CrowdRepository;

@Service
public class CrowdService {
	@Autowired
	CrowdCategoryRepository catrepo;
	@Autowired
	CrowdFormatRepository formatrepo;
	@Autowired
	CrowdTypeRepository typerepo;
	@Autowired
	CrowdRepository crepo;
	public List<CrowdCategory> getCategory(){
		return catrepo.findAll();
	}
	public List<CrowdFormat> getFormat(){
		return formatrepo.findAll();
	}
	public List<CrowdFormat> getFormatByCategory(CrowdCategory category){
		return formatrepo.findByCategory(category);
	}
	public List<CrowdType> getTypeByCategory(CrowdCategory category){
		return typerepo.findByCategory(category);
	}
	
	public List<Crowd> getCrowd(){
		return crepo.findAll();
	}
	public void deleteCrowd(Crowd crowd) {
		crepo.delete(crowd);
	}
	public void saveCrowd(Crowd crowd) {
		crepo.save(crowd);
	}
}
