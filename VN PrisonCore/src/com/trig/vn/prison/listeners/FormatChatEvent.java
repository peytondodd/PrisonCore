package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;

public class FormatChatEvent implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		PrisonPlayer p = Prison.getPrisonManager().getPrisonPlayer(e.getPlayer());
		e.setFormat("§8[§6" + p.getRank().getName() + "§8] " + p.getDisplayName() + "§7: ");
	}
}
