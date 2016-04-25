package com.trig.vn.prison;

import net.minecraft.server.v1_9_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.trig.vn.prison.achievements.PrisonAchievements;
import com.trig.vn.prison.kingdoms.KingdomRank;
import com.trig.vn.prison.ranks.PrisonRank;

public class PrisonPlayer extends CraftPlayer {

	private PrisonRank rank;
	private final Inventory warpGui;
	private KingdomRank kingdomRank;
	private Inventory altInv;
	private int backpackSize = 9;
	private PrisonAchievements achievements = new PrisonAchievements();
	
	private long lastTitle = 0L;
	
	public PrisonPlayer(CraftServer server, EntityPlayer entity) {
		super(server, entity);
		Prison.instance().getDatabaseManager().loadAchievements(this);
		achievements.init();
		backpackSize = Prison.instance().getDatabaseManager().getBackpackSize(this);
		altInv = Bukkit.getServer().createInventory(null, backpackSize, "" + getName());
		warpGui = Bukkit.getServer().createInventory(null, 54, "§6Locations");
		
		lastTitle = System.currentTimeMillis();
	}
	
	public void openAchievements() {
		openInventory(achievements.getGUI());
	}

	public PrisonRank getRank() {
		return rank;
	}

	public void setRank(PrisonRank rank) {
		this.rank = rank;
	}
	
	public void setKingdomRank(KingdomRank kingdomRank) {
		this.kingdomRank = kingdomRank;
	}
	
	public KingdomRank getKingdomRank() {
		return kingdomRank;
	}
	
	public Inventory getAlternativeInventory() {
		return altInv;
	}
	
	public PrisonAchievements getAchievements() {
		return achievements;
	}
	
	public long getLastTitle() {
		return lastTitle;
	}
	
	public void rankup() {
		PrisonRank current = this.getRank();
		PrisonRank next = PrisonRank.getNextRank(current);
		if(Prison.getEco().getBalance(this.getPlayer()) >= next.getValue()) { //The player has enough money
			Prison.getEco().withdrawPlayer(this.getPlayer(), next.getValue());
			this.setRank(PrisonRank.getNextRank(this.getRank()));
			Bukkit.getServer().broadcastMessage("§e" + this.getName() + " §7has ranked up to §e§l" + this.getRank().getName() + "§7!");
			Prison.instance().getDatabaseManager().updateRank(this);
		} else {
			this.sendMessage(ChatColor.RED + "You do not have enough money to rankup!");
			return;
		}
	}
	
	private void updateWarps() {
		warpGui.clear();
		for(PrisonRank rank : PrisonRank.getPrisonRanks()) {
			if(!this.getRank().isAheadOf(rank)) { 
				continue;
			}
			ItemStack warp = new ItemStack(Material.EMERALD_BLOCK);
			ItemMeta meta = warp.getItemMeta();
			meta.setDisplayName("§a§l" + rank.getName());
			warp.setItemMeta(meta);
			warpGui.addItem(warp);
		}
		
	}
	
	public void setLastTitle(long lastTitle) {
		this.lastTitle = lastTitle;
	}
	
	public void openWarpGUI() {
		updateWarps();
		this.openInventory(warpGui);
	}
	
}
