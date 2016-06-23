package com.trig.vn.prison.listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.trig.vn.crates.Crates;
import com.trig.vn.crates.api.KeyData;
import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.config.Config;

public class BlockToInventoryEvent implements Listener {

	private Prison main;
	private static HashMap<Material, ItemStack> blockConversions = new HashMap<Material, ItemStack>();
	
	private static final long TITLE_DELAY = 3000;
	
	public BlockToInventoryEvent(Prison main) {
		this.main = main;
		blockConversions.put(Material.REDSTONE_ORE, new ItemStack(Material.REDSTONE));
		blockConversions.put(Material.LAPIS_ORE, new ItemStack(Material.INK_SACK, 1, (byte) 4));
		blockConversions.put(Material.DIAMOND_ORE, new ItemStack(Material.DIAMOND));
		blockConversions.put(Material.EMERALD_ORE, new ItemStack(Material.EMERALD));
		blockConversions.put(Material.COAL_ORE, new ItemStack(Material.COAL));
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		if(e.isCancelled()) { return; }
		if(!WorldGuardPlugin.inst().canBuild(e.getPlayer(), e.getBlock())) {
			return;
		}
		if(Config.MINE_WORLDS.contains(e.getBlock().getWorld().getName())) {
			Player p = e.getPlayer();
			PrisonPlayer pp = Prison.getPrisonManager().getPrisonPlayer(p);
			Inventory inv = p.getInventory();
			e.setCancelled(true);
			//We need to find out which inventory to place the block in.
			if(p.getInventory().firstEmpty() == -1) {
				if(pp.getAlternativeInventory().firstEmpty() == -1) {
					//both inventories are full
					if(pp.canSeeTitles()) {						
						if(System.currentTimeMillis() - pp.getLastTitle() >= TITLE_DELAY) {						
							pp.sendTitle("§c§lWarning", "§4Full Inventory");
							pp.setLastTitle(System.currentTimeMillis());
						}
					}
				} else {
					inv = pp.getAlternativeInventory();
				}
			}
			rollForKey(p);
			Material type = e.getBlock().getType();
			byte magic = e.getBlock().getData();
			e.getBlock().setType(Material.AIR);
			BlockState state = e.getBlock().getState();
			state.update(true);
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
			inv.addItem(new ItemStack(type, amount, magic));
			return;
		}
	}
	
	private void rollForKey(Player p) {
		int r = new Random().nextInt(Config.MINING_KEY_RATE);
		if(r == 0) {
			KeyData data = Crates.getKeyData(p);
			data.setMining(data.getMining() + 1);
			Crates.instance().updateKeys(p, data);
			p.sendMessage(Config.MESSAGE_PREFIX + "§f§lYou have found a §6§lkey §f§lfrom mining!");
		}
	}
}
