package com.trig.vn.prison.managers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_10_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.config.Config;
import com.trig.vn.prison.economy.MineShop;
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
				Prison.getStaffOnline().next();
				for(PrisonPlayer p : prisonPlayers) {
					if(p.isUseScoreboard()) {
						p.updateScoreboard();
					}
				}
			}
		}, 20 * 2, 20 * 2);
	}
	
	public PrisonPlayer getPrisonPlayer(Player p) {
		for(PrisonPlayer player : prisonPlayers) {
			if(player.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
				return player;
			}
		}
		return null;
	}
	
	public static void sell(PrisonPlayer player, String[] args) {
		PrisonRank rank = player.getRank();
		if(args.length == 0) {				
			//We'll attempt to sell at every shop they have access to.
			while(rank != null) {
				MineShop shop = Prison.instance().getMineShop(rank.getName());
				shop.sell(player);
				rank = PrisonRank.getPreviousRank(rank);
			}
		} else {
			String mineshop = args[0];
			MineShop shop = Prison.instance().getMineShop(mineshop);
			//Check if they can sell to that shop
			PrisonRank attempt = PrisonRank.getPrisonRank(mineshop);
			if(attempt != null) {
				if(attempt.isAheadOf(rank)) {
					player.sendMessage(Config.MESSAGE_PREFIX + "§4You cannot sell to this shop!");
					return;
				} else {
					shop.sell(player);
					return;
				}
			}
		}
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
