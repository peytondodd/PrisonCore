package com.trig.vn.prison.kingdoms.tasks;

import java.util.Random;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import com.trig.vn.prison.kingdoms.WorldEvent;

public class WorldEventTask extends TimerTask {

	public void run() {
		if(Bukkit.getServer().getOnlinePlayers().size() == 0) {
			Bukkit.getServer().broadcastMessage("§6No players online. Skipping world event.");
			return;
		}
		Random rand = new Random();
		int event = rand.nextInt(2) + 1;
		WorldEvent e = WorldEvent.values()[event];
		e.spawn();
	}
	
	

}
