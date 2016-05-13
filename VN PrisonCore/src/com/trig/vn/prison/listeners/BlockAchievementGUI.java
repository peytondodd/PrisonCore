package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.trig.vn.prison.utils.Constant;

public class BlockAchievementGUI implements Listener {

	@EventHandler
	public void onClickAchievement(InventoryClickEvent e) {
		if(e.getInventory().getName().equalsIgnoreCase(Constant.ACHIEVEMENT_GUI_NAME)) {
			e.setCancelled(true);
		}
	}
}
