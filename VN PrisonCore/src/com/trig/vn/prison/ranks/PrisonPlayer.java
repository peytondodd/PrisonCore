package com.trig.vn.prison.ranks;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PrisonPlayer extends CraftPlayer {

	private PrisonRank rank;
	
	public PrisonPlayer(CraftServer server, EntityPlayer entity) {
		super(server, entity);
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
	
}
