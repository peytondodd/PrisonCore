package com.trig.vn.prison.managers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_9_R2.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.economy.Multiplier;
import com.trig.vn.prison.listeners.BloatChatEvent;
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
			main.getDatabaseManager().registerPlayer(player);
			String rank = main.getDatabaseManager().getPrisonRankName(player.getUniqueId().toString());
			PrisonRank prank = PrisonRank.getPrisonRank(rank);
			player.setRank(prank);
			
			player.updateScoreboard();
			player.showScoreboard();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cleanup() {
		prisonPlayers.clear();
	}
	
	public void scoreboardLoop() {
		Bukkit.getServer().getScheduler().runTaskTimer(main, new Runnable() {
			public void run() {
				Multiplier.checkMultiplier();
				for(PrisonPlayer p : prisonPlayers) {
					if(p.isUseScoreboard()) {
						p.updateScoreboard();
					}
				}
			}
		}, 20, 20);
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
	
	public void unregisterPlayer(PrisonPlayer p) {
		prisonPlayers.remove(p);
		BloatChatEvent.removeBloat(p);
	}
	
	public void unregisterPlayer(Player p) {
		PrisonPlayer pp = getPrisonPlayer(p);
		unregisterPlayer(pp);
	}
}
