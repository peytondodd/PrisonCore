package com.trig.vn.prison.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.economy.MineShop;
import com.trig.vn.prison.ranks.PrisonRank;

public class CommandSell implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("sell")) {
			Player p = (Player) sender;
			PrisonPlayer player = Prison.getPrisonManager().getPrisonPlayer(p);
			PrisonRank rank = player.getRank();
			//We'll attempt to sell at every shop they have access to.
			while(rank != null) {
				MineShop shop = Prison.instance().getMineShop(rank.getName());
				shop.sell(player);
				rank = PrisonRank.getPreviousRank(rank);
			}
		}
		return true;
	}

}
