package com.trig.vn.prison.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.mobs.WorldEvent;

public class MobAssistant {

	public static void spawnMassEntity(Location center, double radius, int amount, EntityType type) {
		for(int i = 0; i < amount; i++) {
			Location loc = randomLocation(center, radius);
			WorldEvent.addEntity(loc.getWorld().spawnEntity(loc, type));
		}
	}
	
	public static void syncMassEntity(final Location center, final double radius, final int amount, final EntityType type) {
		Bukkit.getServer().getScheduler().runTask(Prison.instance(), new Runnable() {
			public void run() {
				spawnMassEntity(center, radius, amount, type);
			}
		});
	}
	
	public static Location randomLocation(Location center, double radius) {
		Random rand = new Random();
		double x = center.getX();
		double y = center.getY();
		double z = center.getZ();
		
		switch(rand.nextInt(2) + 1) {
		case 1:
			x += rand.nextInt((int)radius);
			break;
		case 2:
			x -= rand.nextInt((int)radius);
		}
		
		//We don't want them spawning in the air.
//		switch(rand.nextInt(2) + 1) {
//		case 1:
//			y += rand.nextInt((int)radius);
//			break;
//		case 2:
//			y -= rand.nextInt((int)radius);
//		}
		
		switch(rand.nextInt(2) + 1) {
		case 1:
			z += rand.nextInt((int)radius);
			break;
		case 2:
			z -= rand.nextInt((int)radius);
		}
		
		return new Location(center.getWorld(), x, y, z);
	}
}
