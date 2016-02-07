package com.trig.vn.prison.listeners;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.trig.vn.prison.Prison;

public class ClickSellSignEvent implements Listener {

	private Prison main;
	
	public ClickSellSignEvent(Prison main) {
		this.main = main;
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			if(e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				if(sign.getLine(0).equalsIgnoreCase("§2§lSELL")) {
					e.setCancelled(true);
					String mineID = sign.getLine(1);
					main.getMineShop(mineID).sell(p);
					//TODO test this
					return;
				}
			}
		}
	}
}
