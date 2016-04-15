package com.trig.vn.prison.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.utils.Constant;

public class CaravanDriver implements Listener {

	private Prison main;
	public CaravanDriver(Prison main) {
		this.main = main;
	}
	
	@EventHandler
	public void clickVillager(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof Villager) {
			Villager v = (Villager) e.getRightClicked();
			if(v.isCustomNameVisible()) {
				if(v.getCustomName().equalsIgnoreCase(Constant.CARAVAN_DRIVER)) {
					e.setCancelled(true);
					PrisonPlayer p = main.getPrisonManager().getPrisonPlayer(e.getPlayer());
					p.openWarpGUI();
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void clickWarp(InventoryClickEvent e) {
		if(e.getInventory().getName().equalsIgnoreCase(Constant.WARP_GUI_NAME)) {
			if(e.getCurrentItem() == null) { 
				e.setCancelled(true);
				return; 
			}
			if(e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().getDisplayName() == null) { 
				e.setCancelled(true);
				return; 
			}
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			String warpName = e.getCurrentItem().getItemMeta().getDisplayName();
			warpName = warpName.substring(4, warpName.length()); //Should trim §a§l
			try {
				p.teleport(main.getEssentials().getWarps().getWarp(warpName));
			} catch (Exception exc) {
				p.sendMessage(ChatColor.DARK_RED + "Error warping to: " + warpName);
				exc.printStackTrace();
			}
			return;
		}
	}

}
