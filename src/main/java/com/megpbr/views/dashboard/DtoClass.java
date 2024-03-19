package com.megpbr.views.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

public class DtoClass {
	@Autowired
	
	long totalCrops;
	long totalScapes;
	long totalMarkets;
	public long getTotalCrops() {
		return totalCrops;
	}
	public void setTotalCrops(long totalCrops) {
		this.totalCrops = totalCrops;
	}
	public long getTotalScapes() {
		return totalScapes;
	}
	public void setTotalScapes(long totalScapes) {
		this.totalScapes = totalScapes;
	}
	public long getTotalMarkets() {
		return totalMarkets;
	}
	public void setTotalMarkets(long totalMarkets) {
		this.totalMarkets = totalMarkets;
	}
	
}
