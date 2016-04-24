package com.trig.vn.prison.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.trig.vn.prison.Prison;

public class SpawnTools {

	private static int temp = 100;
	
	public static void lightningCircle() {
		temp = 100;
		final Location center = new Location(Bukkit.getServer().getWorld("prison"), -60, 200, -60);
		int iterations = 0;
		for(int i = 0; i < 50; i++) {
			iterations++;
			Bukkit.getServer().getScheduler().runTaskLater(Prison.instance(), new Runnable() {
				public void run() {
					center.getWorld().strikeLightningEffect(new Location(center.getWorld(), center.getX() - temp, center.getY(), center.getZ()));
					center.getWorld().strikeLightningEffect(new Location(center.getWorld(), center.getX() + temp, center.getY(), center.getZ()));
					
				}
			}, 10 * iterations);
			temp -= 2;
		}
		
	}
}
