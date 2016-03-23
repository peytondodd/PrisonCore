package com.trig.vn.prison.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;

public class CommandAchievement implements CommandExecutor {

	private Prison main;
	public CommandAchievement(Prison main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("achievement")) {
			if(args.length == 0) {
				if(sender instanceof Player) {
					PrisonPlayer player = main.getPrisonManager().getPrisonPlayer((Player) sender);
					player.openAchievements();
					return true;
				}
			} else {
				Player p = (Player) sender;
				String name = args[0];
				Player other = Bukkit.getServer().getPlayer(name);
				if(other != null) {					
					PrisonPlayer op = main.getPrisonManager().getPrisonPlayer(other);
					if(op != null) {						
						p.openInventory(op.getAchievements().getGUI());
						return true;
					} else {
						p.sendMessage("§3§lError! §cPrisonPlayer for " + other.getName() + " could not be found.");
						return true;
					}
				} else {
					p.sendMessage("§7Could not find §c" + args[0]);
					return true;
				}
			}
			
		}
		return true;
	}
}
