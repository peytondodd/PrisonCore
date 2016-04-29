package com.trig.vn.prison.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.trig.vn.prison.Prison;

public class BloatChatEvent implements Listener {

	private static List<Player> bloat = new ArrayList<Player>();
	
	private Prison main;
	
	public BloatChatEvent(Prison main) {
		this.main = main;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(bloat.contains(e.getPlayer())) {			
			StringBuilder st = new StringBuilder(e.getMessage());
			st.insert(0, "§l");
			e.setMessage(st.toString());
		}
	}
	
	public static void addBloat(Player p) {
		bloat.add(p);
	}
	
	public static void removeBloat(Player p) {
		bloat.remove(p);
	}
	
	public static boolean toggleBloat(Player p) {
		if(bloat.contains(p)) {
			removeBloat(p);
			return false;
		} else {
			addBloat(p);
			return true;
		}
	}
}
