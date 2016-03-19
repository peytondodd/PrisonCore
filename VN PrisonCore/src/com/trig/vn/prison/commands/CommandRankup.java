package com.trig.vn.prison.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.ranks.PrisonPlayer;
import com.trig.vn.prison.ranks.PrisonRank;

public class CommandRankup implements CommandExecutor {

	private Prison main;
	
	public CommandRankup(Prison main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("rankup")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				PrisonPlayer player = main.getPrisonManager().getPrisonPlayer(p);	
				if(PrisonRank.canRankup(player)) {
					main.getPrisonManager().rankup(player);
				}
			}
		}
		return true;
	}
}
