package com.trig.vn.prison.ranks;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PrisonPlayer extends CraftPlayer {

	private PrisonRank rank;
	private Inventory altInv;
	
	public PrisonPlayer(CraftServer server, EntityPlayer entity) {
		super(server, entity);
		altInv = Bukkit.getServer().createInventory(null, 54, "" + getName());
	}

	public Player getPlayer() { //If you for some reason need a Bukkit Player
		return this.getPlayer();
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
	
}
