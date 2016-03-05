package com.trig.vn.prison.listeners;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.ranks.PrisonPlayer;

public class BlockToInventoryEvent implements Listener {

	private Prison main;
	private static HashMap<Material, ItemStack> blockConversions = new HashMap<Material, ItemStack>();
	
	public BlockToInventoryEvent(Prison main) {
		this.main = main;
		blockConversions.put(Material.REDSTONE_ORE, new ItemStack(Material.REDSTONE));
		blockConversions.put(Material.LAPIS_ORE, new ItemStack(Material.INK_SACK, 1, (byte) 4));
		blockConversions.put(Material.DIAMOND_ORE, new ItemStack(Material.DIAMOND));
		blockConversions.put(Material.EMERALD_ORE, new ItemStack(Material.EMERALD));
		blockConversions.put(Material.COAL_ORE, new ItemStack(Material.COAL));
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		if(main.getBlockToInventoryWorlds().contains(e.getBlock().getWorld().getName())) {
			Player p = e.getPlayer();
			PrisonPlayer pp = main.getPrisonManager().getPrisonPlayer(p);
			Inventory inv = p.getInventory();;
			e.setCancelled(true);
			if(p.getInventory().firstEmpty() == -1) {
				if(pp.getAlternativeInventory().firstEmpty() == -1) {
					//both inventories are full
					return;
				} else {
					inv = pp.getAlternativeInventory();
				}
			}
			Material type = e.getBlock().getType();
			e.getBlock().setType(Material.AIR);
			int amount = 1;
			if(p.getInventory().getItemInMainHand() != null) {
				if(p.getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					amount = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
				}
			}
			
			if(blockConversions.containsKey(type)) {
				ItemStack item = blockConversions.get(type).clone();
				item.setAmount(amount);
				inv.addItem(item);
				return;
			} 
			inv.addItem(new ItemStack(type, amount));
			return;
		}
	}
}
