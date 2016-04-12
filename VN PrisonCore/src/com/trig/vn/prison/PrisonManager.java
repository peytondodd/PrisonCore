package com.trig.vn.prison;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.trig.vn.prison.ranks.PrisonRank;

public class PrisonManager {

	private Prison main;
	
	private List<PrisonPlayer> prisonPlayers = new ArrayList<PrisonPlayer>();
	
	public PrisonManager(Prison main) {
		this.main = main;
	}
	
	public void handleLogin(PrisonPlayer player) {
		if(prisonPlayers.contains(player)) {
			System.err.println("Player " + player.getName() + " [" + player.getUniqueId().toString() + "] is already online?");
			return;
		}
		prisonPlayers.add(player);
		//Get rank
		try {			
			String rank = main.getDatabaseManager().getPrisonRankName(player.getUniqueId().toString());
			PrisonRank prank = PrisonRank.getPrisonRank(rank);
			player.setRank(prank);
		} catch (Exception e) {
			main.getDatabaseManager().registerPlayer(player);
		}
	}
	
	public PrisonPlayer getPrisonPlayer(Player p) {
		for(PrisonPlayer player : prisonPlayers) {
			if(player.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
				return player;
			}
		}
		return null;
	}
	
	public PrisonPlayer getPrisonPlayer(String uuid) {
		for(PrisonPlayer player : prisonPlayers) {
			if(player.getUniqueId().toString().equalsIgnoreCase(uuid)) {
				return player;
			}
		}
		return null;
	}
	
	public void setupPlayer(Player p) {
		if(getPrisonPlayer(p) != null) {
			return;
		}
		EntityPlayer player = ((CraftPlayer) p).getHandle();
		CraftServer server = (CraftServer) Bukkit.getServer();
		PrisonPlayer pp = new PrisonPlayer(server, player);
		handleLogin(pp);
	}
}
