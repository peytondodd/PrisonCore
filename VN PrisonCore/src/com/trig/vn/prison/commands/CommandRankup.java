package com.trig.vn.prison.commands;

import java.text.DecimalFormat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.ranks.PrisonRank;

public class CommandRankup implements CommandExecutor {

	private Prison main;
	private DecimalFormat format = new DecimalFormat("#,###.##");
	
	public CommandRankup(Prison main) {
		this.main = main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("rankup")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				PrisonPlayer player = main.getPrisonManager().getPrisonPlayer(p);	
				if(PrisonRank.canRankup(player)) {
					player.rankup();
				} else {
					if(PrisonRank.isFinalRank(player.getRank())) {
						return true;
					}
					PrisonRank next = PrisonRank.getNextRank(player.getRank());
					player.sendMessage("§7Your next rank §6[§8" + next.getName() + "§6] §7costs §a$" + format.format(next.getValue()));
					return true;
				}
			}
		}
		return true;
	}
}
