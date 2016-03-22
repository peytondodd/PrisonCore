package com.trig.vn.prison;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.inventory.Inventory;

import com.trig.vn.prison.achievements.PrisonAchievements;
import com.trig.vn.prison.ranks.PrisonRank;

public class PrisonPlayer extends CraftPlayer {

	private PrisonRank rank;
	private Inventory altInv;
	private PrisonAchievements achievements = new PrisonAchievements();
	
	public PrisonPlayer(CraftServer server, EntityPlayer entity) {
		super(server, entity);
		altInv = Bukkit.getServer().createInventory(null, 54, "" + getName());
		Prison.instance().getDatabaseManager().loadAchievements(this);
		achievements.init();
	}

	public PrisonRank getRank() {
		return rank;
	}

	public void setRank(PrisonRank rank) {
		this.rank = rank;
	}
	
	public Inventory getAlternativeInventory() {
		return altInv;
	}
	
	public PrisonAchievements getAchievements() {
		return achievements;
	}
	
}
