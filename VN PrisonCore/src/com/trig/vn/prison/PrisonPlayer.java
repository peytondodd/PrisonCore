package com.trig.vn.prison;

import java.text.DecimalFormat;

import net.minecraft.server.v1_9_R2.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.trig.vn.prison.achievements.PrisonAchievements;
import com.trig.vn.prison.kingdoms.KingdomRank;
import com.trig.vn.prison.ranks.PrisonRank;

public class PrisonPlayer extends CraftPlayer {

	private static DecimalFormat moneyFormat = new DecimalFormat("#,###");
	private PrisonRank rank;
	private final Inventory warpGui;
	private KingdomRank kingdomRank;
	private Inventory altInv;
	private int backpackSize = 9;
	private PrisonAchievements achievements = new PrisonAchievements();
	
	private Scoreboard scoreboard;
	private Objective obj;
	private Score currentRank;
	private Score money;
	
	private long lastTitle = 0L;
	private boolean titles = true;
	private boolean useScoreboard = true;
	
	public PrisonPlayer(CraftServer server, EntityPlayer entity) {
		super(server, entity);
		Prison.instance().getDatabaseManager().loadAchievements(this);
		achievements.init();
		backpackSize = Prison.instance().getDatabaseManager().getBackpackSize(this);
		altInv = Bukkit.getServer().createInventory(null, backpackSize, "" + getName());
		warpGui = Bukkit.getServer().createInventory(null, 54, "§6Locations");
		
		lastTitle = System.currentTimeMillis();
		scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

	}
	
	public void updateScoreboard() { //Must always be called after all variables are initialized
		if(obj != null) {			
			obj.unregister();
		}
		obj = scoreboard.registerNewObjective("gui", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("§cVitality §fNetwork");
		currentRank = obj.getScore("§6Rank: §b" + rank.getName());
		currentRank.setScore(40);
		money = obj.getScore("§6Money: §b$" + moneyFormat.format(Prison.getEco().getBalance(this)));
		money.setScore(39);
	}
	
	public void showScoreboard() {
		this.setScoreboard(scoreboard);
		this.useScoreboard = true;
	}
	
	public void hideScoreboard() {
		this.setScoreboard(Bukkit.getServer().getScoreboardManager().getMainScoreboard());
		this.useScoreboard = false;
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
	
	public boolean isUseScoreboard() {
		return useScoreboard;
	}
	
	public void toggleScoreboard() {
		useScoreboard = !useScoreboard;
		if(useScoreboard) {
			showScoreboard();
		} else {
			hideScoreboard();
		}
	}
	
	public void toggleScoreboard(boolean useScoreboard) {
		this.useScoreboard = useScoreboard;
		if(useScoreboard) {
			showScoreboard();
		} else {
			hideScoreboard();
		}
	}
	
	public void rankup() {
		PrisonRank current = PrisonRank.getPrisonRank(this.getRank().getName());
		PrisonRank next = PrisonRank.getNextRank(current);
		if(Prison.getEco().getBalance(this.getPlayer()) >= next.getValue()) { //The player has enough money
			Prison.getEco().withdrawPlayer(this.getPlayer(), next.getValue());
			this.setRank(PrisonRank.getNextRank(this.getRank()));
			Bukkit.getServer().broadcastMessage("§e" + this.getName() + " §7has ranked up to §e§l" + this.getRank().getName() + "§7!");
			Prison.instance().getDatabaseManager().updateRank(this);
			updateRank();
//			PermissionsEx.getUser(this).removeGroup(current.getName());
//			PermissionsEx.getUser(this).addGroup(getRank().getName());
		} else {
			this.sendMessage(ChatColor.RED + "You do not have enough money to rankup!");
			return;
		}
	}
	
	public void updateRank() {
		try {
			//TODO Store an instance of the player's ACTUAL last rank. /setrank command will mess up perms
			PrisonRank previous = PrisonRank.getPreviousRank(rank);
			PermissionsEx.getUser(this).removeGroup(previous.getName());
			PermissionsEx.getUser(this).addGroup(rank.getName());			
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public boolean canSeeTitles() {
		return titles;
	}
	
	public void toggleTitles() {
		titles = !titles;
	}
	
	public void setLastTitle(long lastTitle) {
		this.lastTitle = lastTitle;
	}
	
	public void openWarpGUI() {
		updateWarps();
		this.openInventory(warpGui);
	}
	
}
