package com.trig.vn.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.utils.Constant;

public class CommandPrison implements CommandExecutor {

	private Prison main;
	public CommandPrison(Prison main) {
		this.main = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("prison")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("Command not available from console.");
				return true;
			}
			if(!sender.hasPermission("vn.admin")) {
				sender.sendMessage(ChatColor.RED + "This command is for admins only.");
				return true;
			}
			
			Player p = (Player) sender;
			
			if(args.length == 0) {
				p.sendMessage(ChatColor.RED + "No arguments specified.");
				return true;
			}
			if(args[0].equalsIgnoreCase("caravan")) {
				//NPCHandler.instance().createDummyVillager(p.getLocation(), Constant.CARAVAN_DRIVER);
				Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
				v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, Integer.MAX_VALUE));
				v.setCustomName(Constant.CARAVAN_DRIVER);
				v.setCustomNameVisible(true);
				p.sendMessage("§6Spawned caravan driver at your location.");
				return true;
			}
		}
		return true;
	}
}
