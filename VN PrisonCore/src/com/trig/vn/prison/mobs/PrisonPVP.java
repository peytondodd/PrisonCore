package com.trig.vn.prison.mobs;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import com.nametagedit.plugin.NametagEdit;
import com.trig.vn.prison.economy.MineShop;

public class PrisonPVP {

	private static World pvpWorld;
	private static HashMap<Player, Long> lastCombat = new HashMap<Player, Long>();
	private static final long combatCooldown = 45;
	private static Scoreboard board;
	
	private static final String SKULL = "\u2620";
	
	public static void init() {
		pvpWorld = Bukkit.getServer().createWorld(new WorldCreator("prisonpvp"));
		pvpWorld.setPVP(true);
		pvpWorld.setDifficulty(Difficulty.HARD);
		pvpWorld.setGameRuleValue("keepInventory", "false");
	}
	
	public static World getHellWorld() {
		return pvpWorld;
	}
	
	public static List<Player> getPlayers() {
		return pvpWorld.getPlayers();
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
	
	public static void updateNametag(Player p) {
		int skulls = getWealth(p);
		NametagEdit.getApi().setPrefix(p, getStringForSkulls(skulls / 2));
		NametagEdit.getApi().setSuffix(p, getStringForSkulls(skulls % 2));
	}
	
	private static String getStringForSkulls(int a) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < a; i++) {
			builder.append(SKULL);
		}
		return builder.toString();
	}
	
	public static int getWealth(Player p) {
		int goldOre = MineShop.getAmountOfItem(new ItemStack(Material.GOLD_ORE), p.getInventory());
		if(goldOre >= 256) {
			return 6;
		}
		if(goldOre >= 128) {
			return 5;
		}
		if(goldOre >= 100) {
			return 4;
		}
		if(goldOre >= 64) {
			return 3;
		}
		if(goldOre >= 32) {
			return 2;
		}
		if(goldOre > 0) {
			return 1;
		}
		return 0;
	}
}
