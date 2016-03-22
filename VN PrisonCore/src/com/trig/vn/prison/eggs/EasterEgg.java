package com.trig.vn.prison.eggs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class EasterEgg {

	private static List<EasterEgg> eggs = new ArrayList<EasterEgg>();
	
	private Location loc;
	private String name;
	private List<String> lore = new ArrayList<String>();
	
	public EasterEgg(String name, List<String> lore, Location loc) {
		this.name = name;
		this.lore = lore;
		this.loc = loc;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public static void addEasterEgg(EasterEgg egg) {
		eggs.add(egg);
	}
	
	public static boolean isEasterEgg(Location loc) {
		for(EasterEgg egg : eggs) {
			if(egg.getLocation().equals(loc)) {
				return true;
			}
		}
		return false;
	}
	
	public static EasterEgg getEasterEgg(Location loc) {
		for(EasterEgg egg : eggs) {
			if(egg.getLocation().equals(loc)) {
				return egg;
			}
		}
		return null;
	}
}
