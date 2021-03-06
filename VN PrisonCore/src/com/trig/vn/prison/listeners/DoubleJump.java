package com.trig.vn.prison.listeners;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.trig.vn.prison.managers.RegionManager;
import com.trig.vn.prison.mobs.PrisonPVP;
import com.vn.core.utils.Region;
import com.vn.core.utils.Region.RegionFlag;

public class DoubleJump implements Listener {

	@EventHandler
	public void onFly(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(p.getGameMode() != GameMode.CREATIVE) {
			Region rg = RegionManager.getRegion(RegionManager.getPlayerRegion(p));
			if(rg != null && rg.hasFlag(RegionFlag.DENY_DOUBLE_JUMP.getFlag()) && rg.getMin().getWorld().getName().equalsIgnoreCase(p.getWorld().getName())) {
				p.setFlying(false);
				p.setAllowFlight(false);
				return;
			}
			if(PrisonPVP.contains(p)) {
				p.setFlying(false);
				p.setAllowFlight(false);
				return;
			}
			if(p.isFlying()) {
				p.setFlying(false);
				p.setAllowFlight(false);
				Vector v = p.getEyeLocation().getDirection();
				v.multiply(1.3F);
				p.setVelocity(v);
				p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 0.25F, 0.25F);
			}
		}
		
		if(p.isOnGround()) {
			p.setAllowFlight(true);
		}
	}
}
