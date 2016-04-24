package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.managers.PrisonLinks;
import com.trig.vn.prison.managers.RegionManager;

public class PlayerLinkEvent implements Listener {

	private Prison main;
	
	public PlayerLinkEvent(Prison main) {
		this.main = main;
	}
	
	@EventHandler
	public void onEnterLeaveRegion(PlayerMoveEvent e) {
		//TODO optimize this.
		String rg = null;
		if((rg = RegionManager.getPlayerRegion(e.getPlayer())) != null) {
			if(PrisonLinks.linkValid(rg)) {
				PrisonLinks.link(rg, e.getPlayer());
				System.out.println("Linking " + e.getPlayer().getName() + " [" + rg + "->" + PrisonLinks.getLinkForRegion(rg).getLocation() + "]");
				return;
			}
		}
	}

}
