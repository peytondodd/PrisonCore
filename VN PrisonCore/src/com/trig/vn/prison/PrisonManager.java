package com.trig.vn.prison;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.trig.vn.prison.ranks.PrisonPlayer;
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
		String rank = main.getDatabaseManager().getPrisonRankName(player.getUniqueId().toString());
		PrisonRank prank = Prison.getPrisonRank(rank);
		player.setRank(prank);
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
}
