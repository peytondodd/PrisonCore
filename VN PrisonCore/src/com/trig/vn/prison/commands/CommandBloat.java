package com.trig.vn.prison.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trig.vn.prison.listeners.BloatChatEvent;

public class CommandBloat implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("bloat")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("vn.bloat")) {
					if(BloatChatEvent.toggleBloat(p)) {
						p.sendMessage("§7Your chat is now §lbloated");
						return true;
					} else {
						p.sendMessage("§7You chat has returned to normal.");
						return true;
					}
				}
			} else {
				sender.sendMessage("§4This command is not available from console.");
				return true;
			}
		}
		return true;
	}
}
