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
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.utils.MobAssistant;
import com.trig.vn.prison.utils.SpawnTools;

public class WorldEvent {
	
	private static Prison main = Prison.instance();
	private static List<Entity> entities = new ArrayList<Entity>(); //We'll need to keep track of everything so we can cleanup later
	private static long startTime = 0L;
	private static final long RUNTIME = 60000 * 5; //10 Minutes
	private static WorldEventLocation loc;
	private static Location center;
	private static Location bossLoc;
	private static BossBar bar;
	private static boolean inProgress = false;
	private static final double DRAIN_RATE = 0.0003;
	
	private static Thread damageThread;
	private static Thread mobThread;
	
	public static void init() {
		//cleanup();
		startTime = System.currentTimeMillis();
		Random rand = new Random();
		int n = rand.nextInt(1) + 1;
		switch(n) {
		case 1:
			loc = WorldEventLocation.CEMETARY;
			break;
		case 2:
			loc = WorldEventLocation.LAKE;
			break;
		}
		
		bar = Bukkit.getServer().createBossBar("�6King's Soul", BarColor.GREEN, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG, BarFlag.DARKEN_SKY);
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
		entities.clear();
		Bukkit.getServer().broadcastMessage("�6�lA World Event has started at �5�l" + loc.toString());
		damageThread = new Thread() {
			public void run() {
				while(inProgress()) {
					verifyEntities();
					//TODO check boss health
					if(bar.getProgress() - (DRAIN_RATE * entities.size()) <= 0) {
						bar.setProgress(0.0);
					} else {						
						bar.setProgress(bar.getProgress() - (DRAIN_RATE * entities.size()));
					}
					System.out.println("Damaging King for " + (DRAIN_RATE * entities.size()));
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
				
				if(bar.getProgress() <= 0.0) {
					lose();
				} else {
					win();
				}
			}
		};
		
		mobThread = new Thread() {
			public void run() {
				while(inProgress()) {					
					MobAssistant.syncMassEntity(center, 30, 25, EntityType.ZOMBIE);
					MobAssistant.syncMassEntity(center, 30, 25, EntityType.SKELETON);
					try {
						Thread.sleep(60010 * 1); //There is a delay to avoid concurrentmod
					} catch (InterruptedException e) {
						//e.printStackTrace();
					}
				}
			}
		};
		
		mobThread.start();
		damageThread.start();
	}
	
	private static void win() {
		
		stop();
	}
	
	private static void lose() {
		SpawnTools.lightning();
		stop();
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
			if(e.isDead() || !e.isValid()) {
				it.remove();
			}
		}
	}
	
	public static void stop() {
		inProgress = false;
		System.out.println("Stopped World Event.");
		cleanup();
		mobThread.stop();
		damageThread.stop();
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
		if(inProgress == false) {
			return false;
		}
		if(bar.getProgress() <= 0.0) {
			return (inProgress = false);
		}
		if(!timeLimit()) {
			return (inProgress = false);
		}
		return (inProgress = true);
	}
	
	private static boolean timeLimit() {
		return timeElapsed() <= RUNTIME;
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
