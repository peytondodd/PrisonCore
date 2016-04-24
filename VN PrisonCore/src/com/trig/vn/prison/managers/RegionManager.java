package com.trig.vn.prison.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.vn.core.utils.Region;

public class RegionManager {
	
	private static HashMap<String, Region> regions = new HashMap<String, Region>();
	
	public static Region getRegion(String name) {
		return regions.get(name);
	}
	
	public static void cleanup() {
		regions.clear();
	}
	
	public static void addRegion(String name, Region region) {
		regions.put(name, region);
	}
	
	public static String getPlayerRegion(Player p) {
		for(String region : regions.keySet()) {
			if(regions.get(region).inRegion(p.getLocation())) {
				return region;
			}
		}
		return null;
	}

}
