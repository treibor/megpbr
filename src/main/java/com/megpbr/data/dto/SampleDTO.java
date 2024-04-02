package com.megpbr.data.dto;

import java.util.List;

import com.megpbr.data.entity.pbr.Crops;
import com.megpbr.data.entity.pbr.Markets;

public class SampleDTO {

	List<Crops> crops;
	
	List<Markets> markets;

	public List<Crops> getCrops() {
		return crops;
	}

	public void setCrops(List<Crops> crops) {
		this.crops = crops;
	}

	public List<Markets> getMarkets() {
		return markets;
	}

	public void setMarkets(List<Markets> markets) {
		this.markets = markets;
	}
	
	
}
