package com.trig.vn.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.managers.LocationManager;
import com.trig.vn.prison.managers.RegionManager;
import com.trig.vn.prison.objects.PrisonWarp;
import com.trig.vn.prison.utils.Constant;
import com.trig.vn.prison.utils.RegionConverter;
import com.trig.vn.prison.utils.SpawnTools;
import com.vn.core.utils.Region;

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
			
			if(args[0].equalsIgnoreCase("region")) {
				if(args.length == 2) {
					String rg = args[1];
					Region r = RegionConverter.getRegionFromSelection(main.getWorldEdit().getSelection(p));
					main.saveRegion(rg, r);
					p.sendMessage("§aCreated region §6" + rg);
					p.sendMessage("§6§lMIN: §7X: " + r.getMin().getX() + "  Y: " + r.getMin().getY() + "  Z: " + r.getMin().getZ());
					p.sendMessage("§6§lMAX: §7X: " + r.getMax().getX() + "  Y: " + r.getMax().getY() + "  Z: " + r.getMax().getZ());
					return true;
				} else {
					p.sendMessage("§4Invalid usage! Use §6/prison region <name>");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("location")) {
				if(args.length == 2) {
					String rg = args[1];
					Location loc = p.getLocation();
					main.saveLocation(rg, loc);
					p.sendMessage("§aCreated location at your current position.");
					return true;
				} else {
					p.sendMessage("§4Invalid usage! Use §6/prison location <name>");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("link")) {
				if(args.length == 4) {
					String name = args[1];
					String region = args[2];
					String location = args[3];
					if(RegionManager.getRegion(region) != null) {
						if(LocationManager.getLocation(location) != null) {
							PrisonWarp warp = new PrisonWarp(region, location);
							main.saveLink(name, warp);
							p.sendMessage("§aSaved link §6" + name + " §7[" + region + "->" + location + "]");
							return true;
						} else {
							p.sendMessage("§4The location §6" + location + " §4could not be found.");
							return true;
						}
					} else {
						p.sendMessage("§4The region §6" + region + " §4could not be found.");
						return true;
					}
				} else {
					p.sendMessage("§4Invalid usage! Use §6/prison link <name> <region-name> <location-name>");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("test")) {
				SpawnTools.lightningCircle();
				p.sendMessage("Created lightning");
			}
		}
		return true;
	}
}
