package com.trig.vn.prison.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.trig.vn.prison.Prison;

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
		if(main.getBlockToInventoryWorlds().contains(e.getBlock().getWorld().getName())) {
			Player p = e.getPlayer();
			e.setCancelled(true);
			Material type = e.getBlock().getType();
			e.getBlock().setType(Material.AIR);
			int amount = 1;
			if(p.getItemInHand() != null) {
				if(p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					amount = p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
				}
			}
			
			if(blockConversions.containsKey(type)) {
				ItemStack item = blockConversions.get(type).clone();
				item.setAmount(amount);
				p.getInventory().addItem(item);
				return;
			} 
			p.getInventory().addItem(new ItemStack(type, amount));
			return;
		}
	}
}
