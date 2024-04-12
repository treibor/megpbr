package com.megpbr.views.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import com.megpbr.data.entity.Block;
import com.megpbr.data.entity.District;
import com.megpbr.data.entity.State;
import com.megpbr.data.entity.Village;
import com.megpbr.data.service.Dbservice;

public class DtoClass {
	private String year;
    private int total;
    private String villageName;
    private String blockName;
    private String districtName;
    
    
    
    
	public DtoClass(String year, int total, String villageName, String blockName, String districtName) {
		super();
		this.year = year;
		this.total = total;
		this.villageName = villageName;
		this.blockName = blockName;
		this.districtName = districtName;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getVillageName() {
		return villageName;
	}
	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
    
   
	
}
