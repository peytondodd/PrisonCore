package com.trig.vn.prison.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrisonAchievements {

	private static List<Achievement> possibleAchievements = new ArrayList<Achievement>(); //TODO optimize by only using achievement hash
	private List<Achievement> achievements = new ArrayList<Achievement>();
	private Inventory inv;
	
	public void init() { //MUST be called after achievements are loaded into array
		inv = Bukkit.createInventory(null, 54, "§aAchievements");
		update();
	}
	
	public void update() {
		inv.clear();
		for(Achievement achievement : possibleAchievements) {
			Material mat = Material.REDSTONE_BLOCK; //Default to redstone block
			if(achievements.contains(achievement)) {
				mat = Material.EMERALD_BLOCK;
			}
			ItemStack item = new ItemStack(mat);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(achievement.getName());
			meta.setLore(achievement.getLore());
			item.setItemMeta(meta);
			inv.addItem(item);
		}
	}
	
	public boolean hasAchievement(Achievement a) {
		return achievements.contains(a);
	}
	
	public Inventory getGUI() {
		return inv;
	}
	
	public void unlockAchievement(Achievement achievement) { //Should only be used when a player actually unlocks an achievement
		achievements.add(achievement); 						 //As it forces the inventory to refresh.
		update();
	}
	
	public void addAchievement(Achievement achievement) { //Should be used when first loading achievements (aka on login)
		achievements.add(achievement);
	}
	
	public static void addPossibleAchievement(Achievement achievement) {
		possibleAchievements.add(achievement);
	}
	
	public static List<Achievement> getPossibleAchievements() {
		return possibleAchievements;
	}
	
	public static Achievement getAchievement(String name) {
		for(Achievement a : possibleAchievements) {
			if(a.getName().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}
	
	public static Achievement getAchievement(int id) {
		for(Achievement a : possibleAchievements) {
			if(a.getId() == id) {
				return a;
			}
		}
		return null;
	}
}
