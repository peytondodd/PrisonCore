package com.trig.vn.prison.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;

import com.trig.vn.prison.Prison;

public class WorldEvent {
	
	private static Prison main = Prison.instance();
	private static List<Entity> entities = new ArrayList<Entity>(); //We'll need to keep track of everything so we can cleanup later
	private static long startTime = 0L;
	private static WorldEventLocation loc;
	
	public static void init() {
		cleanup();
		startTime = System.currentTimeMillis();
		Random rand = new Random();
		int n = rand.nextInt(2) + 1;
		switch(n) {
		case 1:
			loc = WorldEventLocation.CEMETARY;
			break;
		case 2:
			loc = WorldEventLocation.LAKE;
			break;
		}
	}
	
	public static void cleanup() {
		loc = null;
		for(Entity e : entities) {
			if(e.isValid()) {
				e.remove();
			}
		}
	}

}

enum WorldEventLocation {
	CEMETARY, LAKE;
}
