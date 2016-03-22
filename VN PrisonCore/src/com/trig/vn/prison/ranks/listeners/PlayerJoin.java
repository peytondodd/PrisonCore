package com.trig.vn.prison.ranks.listeners;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;

public class PlayerJoin implements Listener {

	private Prison main;
	
	public PlayerJoin(Prison main) {
		this.main = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		EntityPlayer player = ((CraftPlayer) e.getPlayer()).getHandle();
		CraftServer server = (CraftServer) Bukkit.getServer();
		PrisonPlayer pp = new PrisonPlayer(server, player);
		main.getPrisonManager().handleLogin(pp);
	}
}
