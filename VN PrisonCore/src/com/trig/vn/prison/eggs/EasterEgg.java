package com.trig.vn.prison.eggs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.trig.vn.prison.achievements.Achievement;
import com.trig.vn.prison.achievements.PrisonAchievements;

public class EasterEgg extends Achievement {

	private static List<EasterEgg> eggs = new ArrayList<EasterEgg>();
	private Location loc;
	
	public EasterEgg(String name, List<String> lore, Location loc, int id) {
		super(name, lore, id);
		this.loc = loc;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public static void addEasterEgg(EasterEgg egg) {
		eggs.add(egg);
		PrisonAchievements.addPossibleAchievement(egg);
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
