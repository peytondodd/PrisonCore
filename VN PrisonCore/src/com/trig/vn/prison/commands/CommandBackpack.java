package com.trig.vn.prison.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;

public class CommandBackpack implements CommandExecutor {

	private Prison main;
	
	public CommandBackpack(Prison main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("backpack")) {			
			if(!(sender instanceof Player)) {
				sender.sendMessage("Cannot use this command from console!");
				return true;
			}
			if(args.length == 0) { //Open their own backpack
				Player player = (Player) sender;
				PrisonPlayer p = main.getPrisonManager().getPrisonPlayer(player);
				p.openInventory(p.getAlternativeInventory());
				return true;
			}
			//ADMIN command - check another player's backpack
			if(args.length == 1) {
				String playerName = args[0];
				Player self = (Player) sender;
				Player other = Bukkit.getServer().getPlayer(playerName);
				PrisonPlayer o = main.getPrisonManager().getPrisonPlayer(other);
				self.openInventory(o.getAlternativeInventory());
				return true;
			}
		}
		return true;
	}

}
