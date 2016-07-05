package com.trig.vn.prison.commands;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.trig.ct.ClockTimer;
import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.achievements.PrisonAchievements;
import com.trig.vn.prison.config.Config;
import com.trig.vn.prison.economy.Multiplier;
import com.trig.vn.prison.eggs.EasterEgg;
import com.trig.vn.prison.managers.LocationManager;
import com.trig.vn.prison.managers.RegionManager;
import com.trig.vn.prison.mobs.WorldEvent;
import com.trig.vn.prison.objects.PrisonWarp;
import com.trig.vn.prison.ranks.PrisonRank;
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
				sender.sendMessage(Config.MESSAGE_PREFIX + "Command not available from console.");
				return true;
			}
			if(args.length == 0) {
				showCommandHelp(sender);
				return true;
			}
			if(args[0].equalsIgnoreCase("titles")) {
				Player p = (Player) sender;
				PrisonPlayer pp = Prison.getPrisonManager().getPrisonPlayer(p);
				pp.toggleTitles();
				if(pp.canSeeTitles()) {					
					pp.sendMessage(Config.MESSAGE_PREFIX + "§7Titles are now §6on!");
				} else {
					pp.sendMessage(Config.MESSAGE_PREFIX + "§7Titles are now §6off!");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("scoreboard")) {
				Player p = (Player) sender;
				PrisonPlayer pp = Prison.getPrisonManager().getPrisonPlayer(p);
				pp.toggleScoreboard();
				if(pp.isUseScoreboard()) {					
					pp.sendMessage(Config.MESSAGE_PREFIX + "§7Scoreboards are now §6on!");
				} else {
					pp.sendMessage(Config.MESSAGE_PREFIX + "§7Scoreboards are now §6off!");
				}
			}
			
			if(!sender.hasPermission("vn.admin")) {
				sender.sendMessage(Config.MESSAGE_PREFIX + ChatColor.RED + "This command is for admins only.");
				return true;
			}
			
			//Console Commands
			if(args[0].equalsIgnoreCase("startmultiplier")) {
				double value = Double.parseDouble(args[1]);
				long duration = Long.parseLong(args[2]);
				Multiplier.setMultiplier(value, duration, (Player) sender);
				//Bukkit.getServer().broadcastMessage("§6§lA sell multiplier of §a§l" + value + "x §6§lhas been activated for §a§l" + ClockTimer.formatTimeMillis(duration, false));
				return true;
			}
			
			//Player commands
			Player p = (Player) sender;
			
			if(args.length == 0) {
				p.sendMessage(Config.MESSAGE_PREFIX + ChatColor.RED + "No arguments specified.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")) {
				main.refreshConfig();
				p.sendMessage(Config.MESSAGE_PREFIX + "§6Config reloaded.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("caravan")) {
				Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
				v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 133));
				v.setCustomName(Constant.CARAVAN_DRIVER);
				v.setCustomNameVisible(true);
				p.sendMessage(Config.MESSAGE_PREFIX + "§6Spawned caravan driver at your location.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("setrank")) {
				if(args.length == 3) {
					String playerName = args[1];
					String rank = args[2];
					Player other = Bukkit.getServer().getPlayer(playerName);
					if(other != null) {
						PrisonRank r = PrisonRank.getPrisonRank(rank);
						if(r != null) {
							PrisonPlayer po = main.getPrisonManager().getPrisonPlayer(other);
							if(po != null) {
								po.setRank(r);
								main.getDatabaseManager().updateRank(po);
								po.updateRank();
								p.sendMessage(Config.MESSAGE_PREFIX + "§7Changed §6" + other.getName() + "§7's rank to §6" + r.getName());
								po.sendMessage(Config.MESSAGE_PREFIX + "§7Your rank was set to §6" + r.getName() + " §7by §6" + p.getName());
								return true;
							} else {
								p.sendMessage(Config.MESSAGE_PREFIX + "§cCould not find PrisonPlayer for " + other.getName());
								return true;
							}
						} else {
							p.sendMessage(Config.MESSAGE_PREFIX + "§cThere is no rank with the name §6" + rank);
							return true;
						}
					} else {
						p.sendMessage(Config.MESSAGE_PREFIX + "§cCould not find " + playerName);
						return true;
					}
				}
			}
			
			if(args[0].equalsIgnoreCase("region")) {
				if(args.length == 2) {
					String rg = args[1];
					Region r = RegionConverter.getRegionFromSelection(main.getWorldEdit().getSelection(p));
					main.saveRegion(rg, r);
					p.sendMessage(Config.MESSAGE_PREFIX + "§aCreated region §6" + rg);
					p.sendMessage("§6§lMIN: §7X: " + r.getMin().getX() + "  Y: " + r.getMin().getY() + "  Z: " + r.getMin().getZ());
					p.sendMessage("§6§lMAX: §7X: " + r.getMax().getX() + "  Y: " + r.getMax().getY() + "  Z: " + r.getMax().getZ());
					return true;
				} else if(args.length == 3) {
					if(args[1].equalsIgnoreCase("addflag")) {
						String flag = args[2];
						String rg = args[3];
						Region r = RegionManager.getRegion(rg);
						if(r != null) {
							r.addFlag(flag);
							main.saveRegion(rg, r);
							p.sendMessage(Config.MESSAGE_PREFIX + "§7Sucessfully added flag §4" + flag + "§7to the region §4" + rg);
						} else {
							p.sendMessage(Config.MESSAGE_PREFIX + "§4Could not find region with the name §6" + rg);
							return true;
						}
					}
				} else {
					p.sendMessage(Config.MESSAGE_PREFIX + "§4Invalid usage! Use §6/prison region <name> §4or §6/prison region addflag <flag> <region>");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("location")) {
				if(args.length == 2) {
					String rg = args[1];
					Location loc = p.getLocation();
					main.saveLocation(rg, loc);
					p.sendMessage(Config.MESSAGE_PREFIX + "§aCreated location at your current position.");
					return true;
				} else {
					p.sendMessage(Config.MESSAGE_PREFIX + "§4Invalid usage! Use §6/prison location <name>");
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
							p.sendMessage(Config.MESSAGE_PREFIX + "§aSaved link §6" + name + " §7[" + region + "->" + location + "]");
							return true;
						} else {
							p.sendMessage(Config.MESSAGE_PREFIX + "§4The location §6" + location + " §4could not be found.");
							return true;
						}
					} else {
						p.sendMessage(Config.MESSAGE_PREFIX + "§4The region §6" + region + " §4could not be found.");
						return true;
					}
				} else {
					p.sendMessage(Config.MESSAGE_PREFIX + "§4Invalid usage! Use §6/prison link <name> <region-name> <location-name>");
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("egg")) {
				if(args.length == 2) {
					String name = args[1];
					Block b = p.getTargetBlock((Set<Material>)null, 3);
					if(b == null || b.getType() == Material.AIR) {
						p.sendMessage(Config.MESSAGE_PREFIX + "§cCould not find a block within 3m.");
						return true;
					}
					
					if(b.getType() == Material.DRAGON_EGG) {
						Location loc = b.getLocation();
						EasterEgg egg = new EasterEgg(name, Arrays.asList(""), loc, PrisonAchievements.nextAvailableAchievementId());
						main.saveEasterEgg(egg);
						EasterEgg.addEasterEgg(egg);
						p.sendMessage(Config.MESSAGE_PREFIX + "§7Created easter egg with name §6" + name);
						p.sendMessage("§7Lore must be set in config.");
						return true;
					} else {
						p.sendMessage(Config.MESSAGE_PREFIX + "§cSelected block is not a dragon egg!");
						return true;
					}
				} else {
					p.sendMessage(Config.MESSAGE_PREFIX + "§4Invalid usage! §6Use /prison egg <name>");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("test")) {
				
				p.sendMessage("Executed test");
			}
			if(args[0].equalsIgnoreCase("startevent")) {
				p.sendMessage(Config.MESSAGE_PREFIX + "§5Starting World Event...");
				WorldEvent.init();
				return true;
			}
			if(args[0].equalsIgnoreCase("stopevent")) {
				p.sendMessage(Config.MESSAGE_PREFIX + "§5Stopping World Event...");
				WorldEvent.stop();
				return true;
			}
		}
		return true;
	}

	private void showCommandHelp(CommandSender sender) {
		sender.sendMessage("§6/prison §ctitles    §8Disable or enable title notifications.");
		sender.sendMessage("§6/prison §cscoreboard    §8Disable or enable the scoreboard.");
	}
}
