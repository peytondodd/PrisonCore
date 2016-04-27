package com.trig.vn.prison.mobs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.utils.MobAssistant;

public class WorldEvent {
	
	private static Prison main = Prison.instance();
	private static List<Entity> entities = new ArrayList<Entity>(); //We'll need to keep track of everything so we can cleanup later
	private static long startTime = 0L;
	private static final long RUNTIME = 60000 * 10; //10 Minutes
	private static WorldEventLocation loc;
	private static Location center;
	private static Location bossLoc;
	private static BossBar bar;
	private static boolean inProgress = false;
	private static final double DRAIN_RATE = 0.0003;
	
	public static void init() {
		//cleanup();
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
		
		bar = Bukkit.getServer().createBossBar("§6King's Soul", BarColor.GREEN, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG, BarFlag.DARKEN_SKY);
		for(Player p : Bukkit.getServer().getWorld("prison").getPlayers()) {
			bar.addPlayer(p);
		}
		bar.show();
		bar.setProgress(1);
		inProgress = true;
		switch(loc) {
		case CEMETARY:
			center = new Location(Bukkit.getServer().getWorld("prison"), 312, 135, -20);
			bossLoc = new Location(Bukkit.getServer().getWorld("prison"), 396, 134, -19);
			break;
		case LAKE:
			
			break;
		}
		start();
	}
	
	private static void start() {
		Bukkit.getServer().broadcastMessage("§6§lA World Event has started at §5§l" + loc.toString());
		Thread damageThread = new Thread() {
			public void run() {
				while(inProgress && !over()) {
					verifyEntities();
					//TODO check boss health
					bar.setProgress(bar.getProgress() - (DRAIN_RATE * entities.size()));
					System.out.println("Damaging King for " + (DRAIN_RATE * entities.size()));
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		Thread mobThread = new Thread() {
			public void run() {
				while(inProgress && !over()) {					
					MobAssistant.syncMassEntity(center, 30, 25, EntityType.ZOMBIE);
					MobAssistant.syncMassEntity(center, 30, 25, EntityType.SKELETON);
					try {
						Thread.sleep(60000 * 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		mobThread.start();
		damageThread.start();
	}
	
	public static void cleanup() {
		System.out.println("Cleaning up World Event...");
		loc = null;
		bar.hide();
		bar.removeAll();
		bar = null;
		center = null;
		bossLoc = null;
		inProgress = false;
		System.out.println("Clearing " + entities.size() + " entities.");
		for(Entity e : entities) {
			if(e.isValid()) {
				e.remove();
			}
		}
		System.out.println("Cleanup done.");
	}
	
	private static void verifyEntities() {
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext()) {
			Entity e = it.next();
			if(e.isDead()) {
				it.remove();
			}
		}
	}
	
	public static void stop() {
		inProgress = false;
		System.out.println("Stopped World Event.");
		cleanup();
	}
	
	public static void givePlayerBar(Player p) {
		if(bar != null) {
			bar.addPlayer(p);
		}
	}
	
	public static void removePlayerBar(Player p) {
		if(bar != null) {
			bar.removePlayer(p);
		}
	}
	
	public static boolean inProgress() {
		return inProgress;
	}
	
	private static boolean over() {
		if(bar.getProgress() <= 0) {
			return true;
		}
		if(timeElapsed() >= RUNTIME) {
			return true;
		}
		return false;
	}
	
	public static void addEntity(Entity e) {
		entities.add(e);
	}
	
	public static long timeElapsed() {
		return System.currentTimeMillis() - startTime;
	}

}

enum WorldEventLocation {
	CEMETARY, LAKE;
}
