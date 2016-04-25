package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.trig.vn.prison.mobs.WorldEvent;

public class ChangeWorld implements Listener {

	@EventHandler
	public void onEnterSpawn(PlayerChangedWorldEvent e) {
		if(e.getPlayer().getWorld().getName().equalsIgnoreCase("prison")) {
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, Integer.MAX_VALUE));
			if(WorldEvent.inProgress()) {
				WorldEvent.givePlayerBar(e.getPlayer());
			}
		} else {
			WorldEvent.removePlayerBar(e.getPlayer()); //Try to remove the bar
			e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
		}
	}
}
