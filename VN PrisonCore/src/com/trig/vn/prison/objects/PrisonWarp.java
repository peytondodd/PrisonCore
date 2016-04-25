package com.trig.vn.prison.objects;

import org.bukkit.entity.Player;

import com.trig.vn.prison.managers.LocationManager;

public class PrisonWarp {

	private String region;
	private String loc;
	private String permission;
	
	public PrisonWarp(String region, String loc) {
		this.region = region;
		this.loc = loc;
	}
	
	public PrisonWarp(String region, String loc, String permission) {
		this.region = region;
		this.loc = loc;
		this.permission = permission;
	}
	
	public void warp(Player p) {
		p.teleport(LocationManager.getLocation(loc));
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public String getRegion() {
		return region;
	}
	
	public String getLocation() {
		return loc;
	}

}
