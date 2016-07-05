package com.trig.vn.prison.economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.trig.ct.ClockTimer;

public class Multiplier {

	private static double multiplier = 1.0;
	private static long multiplierDuration = 0L;
	private static long startTime = 0L;
	
	public static void setMultiplier(double multiplierX, long duration, Player p) {
		multiplier = multiplierX;
		startTime = System.currentTimeMillis();
		multiplierDuration = duration;
		Bukkit.getServer().broadcastMessage("§6§lA sell multiplier of §4§l" + multiplierX + "x §6§lhas been activated for §4§l" + ClockTimer.formatTimeMillis(duration, false) + " §6§lby §4§l" + p.getName());
	}
	
	public static double getMultiplier() {
		return multiplier;
	}
	
	public static long getMultiplierDuration() {
		return multiplierDuration;
	}
	
	public static void checkMultiplier() {
		if(System.currentTimeMillis() > startTime + multiplierDuration) {
			multiplier = 1.0;
			multiplierDuration = 0L;
		}
	}
//	public static double getMultiplier(Player p) {
//		if(p.hasPermission("vn.prison.multiplier.1.8")) {
//			return 1.8;
//		}
//		if(p.hasPermission("vn.prison.multiplier.1.7")) {
//			return 1.7;
//		}
//		if(p.hasPermission("vn.prison.multiplier.1.6")) {
//			return 1.6;
//		}
//		if(p.hasPermission("vn.prison.multiplier.1.5")) {
//			return 1.5;
//		}
//		if(p.hasPermission("vn.prison.multiplier.1.4")) {
//			return 1.4;
//		}
//		if(p.hasPermission("vn.prison.multiplier.1.3")) {
//			return 1.3;
//		}
//		if(p.hasPermission("vn.prison.multiplier.1.2")) {
//			return 1.2;
//		}
//		if(p.hasPermission("vn.prison.multiplier.1.1")) {
//			return 1.1;
//		}
//		return 1.0;
//	}
}
