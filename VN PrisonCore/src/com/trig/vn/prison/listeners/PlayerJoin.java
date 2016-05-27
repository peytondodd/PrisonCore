package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		Prison.getStaffOnline().update();
		if(WorldEvent.inProgress() && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("prison")) {
			WorldEvent.givePlayerBar(e.getPlayer());
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, Integer.MAX_VALUE));
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		main.getPrisonManager().unregisterPlayer(e.getPlayer());
		Prison.getStaffOnline().update();
		System.out.println("Unregistered " + e.getPlayer().getName());
	}
}
