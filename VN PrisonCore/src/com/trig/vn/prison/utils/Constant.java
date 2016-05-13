package com.trig.vn.prison.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.vn.core.utils.Region;


public class Constant {

	public static final String CARAVAN_DRIVER = "§2§lCaravan Driver";
	public static final String WARP_GUI_NAME = "§6Locations";
	public static final String ACHIEVEMENT_GUI_NAME = "§aAchievements";
	
	public static final Location OUTSIDE_CASTLE = new Location(null, -61, 141, 147);
	public static final Location INSIDE_CASTLE = new Location(null, -60, 141, 127);
	
	public static final Region LEAVE_CASTLE_REGION = new Region(new Location(null, -52, 140, 137), new Location(null, -68, 169, 139));
	public static final Region ENTER_CASTLE_REGION = new Region(new Location(null, -70, 140, 141), new Location(null, -52, 158, 140));
	
	public static void init() {
		OUTSIDE_CASTLE.setWorld(Bukkit.getServer().getWorld("prison"));
		INSIDE_CASTLE.setWorld(Bukkit.getServer().getWorld("prison"));
		LEAVE_CASTLE_REGION.setWorld(Bukkit.getServer().getWorld("prison"));
		ENTER_CASTLE_REGION.setWorld(Bukkit.getServer().getWorld("prison"));
		
	}

}
