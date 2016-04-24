package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.mobs.WorldEvent;

public class PlayerJoin implements Listener {

	private Prison main;
	
	public PlayerJoin(Prison main) {
		this.main = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		main.getPrisonManager().setupPlayer(e.getPlayer());
		
		if(WorldEvent.inProgress() && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("prison")) {
			WorldEvent.givePlayerBar(e.getPlayer());
		}
	}
}
