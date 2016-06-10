package com.trig.vn.prison.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class DoubleJump implements Listener {

	@EventHandler
	public void onFly(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(p.getGameMode() != GameMode.CREATIVE) {
			if(p.isFlying()) {
				p.setFlying(false);
				p.setAllowFlight(false);
				Vector v = p.getEyeLocation().getDirection();
				v.multiply(1.3F);
				p.setVelocity(v);
			}
		}
		
		if(p.isOnGround()) {
			p.setAllowFlight(true);
		}
	}
}
