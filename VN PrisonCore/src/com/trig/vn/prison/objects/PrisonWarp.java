package com.trig.vn.prison.objects;

import org.bukkit.entity.Player;

import com.trig.vn.prison.managers.LocationManager;

public class PrisonWarp {

	private String region;
	private String loc;
	
	public PrisonWarp(String region, String loc) {
		this.region = region;
		this.loc = loc;
	}
	
	public void warp(Player p) {
		p.teleport(LocationManager.getLocation(loc));
	}
	
	public String getRegion() {
		return region;
	}
	
	public String getLocation() {
		return loc;
	}

}
