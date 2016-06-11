package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;

public class FormatChatEvent implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		PrisonPlayer p = Prison.getPrisonManager().getPrisonPlayer(e.getPlayer());
		e.setFormat("§8[§6" + p.getRank().toString() + "§8] " + PermissionsEx.getUser(p).getPrefix() + " " + p.getDisplayName() + "§7:");
	}
}
