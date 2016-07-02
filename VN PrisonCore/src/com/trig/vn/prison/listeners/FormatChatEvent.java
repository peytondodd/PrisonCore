package com.trig.vn.prison.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.ranks.PrisonRank;

public class FormatChatEvent implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		PrisonPlayer p = Prison.getPrisonManager().getPrisonPlayer(e.getPlayer()); //
		//e.setFormat(getColourForRank(p.getRank()) + p.getRank().getName() + " §a" + p.getDisplayName() + " §f" + e.getMessage());
		String prefix = PermissionsEx.getUser(e.getPlayer()).getPrefix();
		if(prefix == null || prefix.equalsIgnoreCase("")) {
			e.setFormat(getColourForRank(p.getRank()) + p.getRank().getName() + " §a" + p.getName() + " §f" + e.getMessage().replaceAll("\\%", ""));
		} else {
			e.setFormat(getColourForRank(p.getRank()) + p.getRank().getName() + " " + prefix.replaceAll("&", "§") + " §a" + p.getName() + " §f" + e.getMessage().replaceAll("\\%", ""));
		}
	}
	
	private String getColourForRank(PrisonRank rank) {
		int r = Integer.parseInt(rank.getName());
		switch(r) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
			return "§7";
		default:
			return "§7";
		}
	}
}
