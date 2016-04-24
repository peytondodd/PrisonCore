package com.trig.vn.prison.managers;

import java.util.HashMap;

import org.bukkit.Location;

public class LocationManager {

private static HashMap<String, Location> locations = new HashMap<String, Location>();
	
	public static Location getLocation(String name) {
		return locations.get(name);
	}
	
	public static void addLocation(String name, Location loc) {
		locations.put(name, loc);
	}
	
	public static String getLocationName(Location loc) {
		for(String location : locations.keySet()) {
			if(locations.get(location).equals(loc)) {
				return location;
			}
		}
		return null;
	}

}
