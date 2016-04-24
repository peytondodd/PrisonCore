package com.trig.vn.prison.utils;

import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.vn.core.utils.Region;

public class RegionConverter {

	public static Region convert(com.sk89q.worldedit.regions.Region rg) {
		Location min = new Location(null, rg.getMinimumPoint().getBlockX(), rg.getMinimumPoint().getBlockY(), rg.getMinimumPoint().getBlockZ());
		Location max = new Location(null, rg.getMaximumPoint().getBlockX(), rg.getMaximumPoint().getBlockY(), rg.getMaximumPoint().getBlockZ());
		return new Region(min, max);
	}
	
	public static Region getRegionFromSelection(Selection sel) {
		return new Region(sel.getMinimumPoint(), sel.getMaximumPoint());
	}
}
