package com.trig.vn.prison.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.trig.vn.prison.Prison;

public class CommandAchievement implements CommandExecutor {

	private Prison main;
	public CommandAchievement(Prison main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("achievement")) {
			if(args.length == 0) {
				
			}
		}
		return true;
	}
}
