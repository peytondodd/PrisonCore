package com.trig.vn.prison.eggs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;

public class ClickEggEvent implements Listener {

	private Prison main;
	
	public ClickEggEvent(Prison main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.DRAGON_EGG) {
				e.setCancelled(true);
				if(EasterEgg.isEasterEgg(e.getClickedBlock().getLocation())) {
					EasterEgg egg = EasterEgg.getEasterEgg(e.getClickedBlock().getLocation());
					PrisonPlayer player = main.getPrisonManager().getPrisonPlayer(e.getPlayer());
					if(!player.getAchievements().hasAchievement(egg)) {
						player.getAchievements().unlockAchievement(egg);
						player.sendMessage("§a§lYou have unlocked the achievement: §6§l" + egg.getName());
					}
				}
			}
		}
	}
}
