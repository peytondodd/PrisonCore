package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.utils.Constant;

public class LeaveCastle implements Listener {

	private Prison main;
	
	public LeaveCastle(Prison main) {
		this.main = main;
	}
	
	@EventHandler
	public void onEnterLeaveRegion(PlayerMoveEvent e) {
		if(Constant.LEAVE_CASTLE_REGION.compareWorld(e.getPlayer().getWorld())) {
			if(Constant.LEAVE_CASTLE_REGION.inRegion(e.getPlayer().getLocation())) {
				//TODO check if they can leave
				e.getPlayer().teleport(Constant.OUTSIDE_CASTLE);
				return;
			}
		}
	}
	
	@EventHandler
	public void onEnterCastle(PlayerMoveEvent e) {
		if(Constant.ENTER_CASTLE_REGION.compareWorld(e.getPlayer().getWorld())) {
			if(Constant.ENTER_CASTLE_REGION.inRegion(e.getPlayer().getLocation())) {
				//They can always enter the castle, no need for checks.
				//Unless we want to ban players from spawn for some reason....
				e.getPlayer().teleport(Constant.INSIDE_CASTLE);
				return;
			}
		}
	}

}
