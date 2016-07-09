package com.trig.vn.prison.mobs;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class Hell {

	private static World hellWorld;
	private static HashMap<Player, Long> lastCombat = new HashMap<Player, Long>();
	private static final long combatCooldown = 45;
	
	public static void init() {
		hellWorld = Bukkit.getServer().createWorld(new WorldCreator("hell"));
		hellWorld.setPVP(true);
		hellWorld.setDifficulty(Difficulty.HARD);
		
	}
	
	public static World getHellWorld() {
		return hellWorld;
	}
	
	public static List<Player> getPlayers() {
		return hellWorld.getPlayers();
	}
	
	public static long getCombatCooldown() {
		return combatCooldown;
	}
	
	public static void tagCombat(Player p) {
		lastCombat.put(p, System.currentTimeMillis() / 1000);
	}
	
	public static long getTimeSinceCombat(Player p) {
		if(!lastCombat.containsKey(p)) {
			lastCombat.put(p, 0L);
		}
		return (System.currentTimeMillis() / 1000) - lastCombat.get(p);
	}
	
	public static boolean isOutOfCombat(Player p) {
		return getTimeSinceCombat(p) >= combatCooldown;
	}
	
	public static boolean contains(Player p) {
		return getPlayers().contains(p);
	}
	
	public static void unregister(Player p) {
		if(lastCombat.containsKey(p)) {
			lastCombat.remove(p);
		}
	}
}
