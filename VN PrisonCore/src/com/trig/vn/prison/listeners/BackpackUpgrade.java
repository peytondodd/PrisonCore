package com.trig.vn.prison.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.config.Config;
import com.trig.vn.prison.utils.InventoryCopy;

public class BackpackUpgrade implements Listener {

	private Prison main;
	
	public BackpackUpgrade(Prison main) {
		this.main = main;
	}
	
	@EventHandler
	public void onClickSign(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PrisonPlayer player = Prison.getPrisonManager().getPrisonPlayer(p);
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				if(sign.getLine(0).equalsIgnoreCase("§1[Upgrade Bag]")) {
					String sizeString = sign.getLine(2);
					sizeString = ChatColor.stripColor(sizeString);
					String[] ss = sizeString.split(" ");
					int slots = Integer.parseInt(ss[0]);
					
					String tokenCost = sign.getLine(3);
					tokenCost = ChatColor.stripColor(tokenCost);
					String[] tc = tokenCost.split(" ");
					int tokens = Integer.parseInt(tc[0]);
					
					if(player.getAlternativeInventory().getSize() >= slots) {
						p.sendMessage(Config.MESSAGE_PREFIX + "§4This backpack is smaller than your current one!");
						return;
					}
					if(Prison.getPlayerUtils().getTokens().getTokens(p) < tokens) {
						p.sendMessage(Config.MESSAGE_PREFIX + "§4You do not have enough tokens to make this purchase!");
						p.sendMessage("§7You can earn more tokens by voting!");
						return;
					}
					Inventory newInv = Bukkit.getServer().createInventory(null, slots, "" + player.getName());
					InventoryCopy.copy(player.getAlternativeInventory(), newInv);
					player.setAlternateInventory(newInv);
					main.getDatabaseManager().updateBackpack(player);
					player.sendMessage(Config.MESSAGE_PREFIX + "§6Your inventory has been upgraded!");
					return;
				}
			}
		}
	}
}
