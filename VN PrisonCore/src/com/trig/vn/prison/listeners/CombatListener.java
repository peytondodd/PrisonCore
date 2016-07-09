package com.trig.vn.prison.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.trig.vn.prison.mobs.Hell;

public class CombatListener implements Listener {

	@EventHandler
	public void onHurt(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(Hell.contains(p)) {
				Hell.tagCombat(p);
			}
		}
	}
	
	@EventHandler
	public void onDealDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(Hell.contains(p)) {
				Hell.tagCombat(p);
			}
		}
	}
}
