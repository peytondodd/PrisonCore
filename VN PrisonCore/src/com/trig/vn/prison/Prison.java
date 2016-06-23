package com.trig.vn.prison;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.trig.essentials.VNEssentials;
import com.trig.playerutils.PlayerUtils;
import com.trig.vn.db.DatabaseConfig;
import com.trig.vn.prison.achievements.PrisonAchievements;
import com.trig.vn.prison.commands.CommandAchievement;
import com.trig.vn.prison.commands.CommandBackpack;
import com.trig.vn.prison.commands.CommandBloat;
import com.trig.vn.prison.commands.CommandPrison;
import com.trig.vn.prison.commands.CommandRankup;
import com.trig.vn.prison.commands.CommandSell;
import com.trig.vn.prison.config.Config;
import com.trig.vn.prison.economy.MineShop;
import com.trig.vn.prison.eggs.ClickEggEvent;
import com.trig.vn.prison.eggs.EasterEgg;
import com.trig.vn.prison.kingdoms.KingdomManager;
import com.trig.vn.prison.listeners.BackpackUpgrade;
import com.trig.vn.prison.listeners.BloatChatEvent;
import com.trig.vn.prison.listeners.BlockAchievementGUI;
import com.trig.vn.prison.listeners.BlockToInventoryEvent;
import com.trig.vn.prison.listeners.CaravanDriver;
import com.trig.vn.prison.listeners.ChangeWorld;
import com.trig.vn.prison.listeners.ClickSellSignEvent;
import com.trig.vn.prison.listeners.DoubleJump;
import com.trig.vn.prison.listeners.FormatChatEvent;
import com.trig.vn.prison.listeners.PlayerJoin;
import com.trig.vn.prison.listeners.PlayerLinkEvent;
import com.trig.vn.prison.managers.LocationManager;
import com.trig.vn.prison.managers.PrisonLinks;
import com.trig.vn.prison.managers.PrisonManager;
import com.trig.vn.prison.managers.RegionManager;
import com.trig.vn.prison.objects.PrisonWarp;
import com.trig.vn.prison.ranks.PrisonRank;
import com.trig.vn.prison.utils.Constant;
import com.trig.vn.prison.utils.TextCycle;
import com.vn.core.Core;
import com.vn.core.sql.MySQL;
import com.vn.core.utils.Region;

public class Prison extends JavaPlugin {

	private List<MineShop> mineShops = new ArrayList<MineShop>();
	
	private static Economy eco;
	private static Prison instance;
	private static PrisonManager manager;
	private KingdomManager kingdom;
	private DatabaseManager dbm;
	
	private static Essentials essentials;
	private static VNEssentials vnessentials;
	private WorldEditPlugin worldEdit;
	
	private Core core;
	private static PlayerUtils playerUtils;
	private static TextCycle staffOnline = new TextCycle();
	
	private MySQL sql;
	private Connection c;
	private final String DB_HOST = DatabaseConfig.getValueFromConfig("DB_HOST");
	private final String DB_PORT = DatabaseConfig.getValueFromConfig("DB_PORT");
	private final String DB_NAME = "db_prison";
	private final String DB_USER = DatabaseConfig.getValueFromConfig("DB_USER");
	private final String DB_PASSWORD = DatabaseConfig.getValueFromConfig("DB_PASSWORD");
	
	public void onEnable() { //Test
		instance = this;
		setup();
		manager = new PrisonManager(this);
		kingdom = new KingdomManager(this);
		dbm = new DatabaseManager(this);
		
		manager.scoreboardLoop();
	}
	
	public void onDisable(){
		manager.cleanup();
	}
	
	private void setup() {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		eco = rsp.getProvider();
		core = (Core) Bukkit.getServer().getPluginManager().getPlugin("VNCore");
		playerUtils = (PlayerUtils) Bukkit.getServer().getPluginManager().getPlugin("PlayerUtils");
		vnessentials = (VNEssentials) Bukkit.getServer().getPluginManager().getPlugin("VNEssentials");
		essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		setupSQL();
		core.registerSQLConnection(c);
		Constant.init();
		//In case of a reload, we don't want to have double values
		LocationManager.cleanup();
		RegionManager.cleanup();
		PrisonLinks.cleanup();
		BloatChatEvent.clear();
		getPrisonManager().cleanup();
		loadConfig();
		loadMineShops();
		loadKingdoms();
		loadRanks();
		loadRegions();
		loadLocations();
		loadLinks(); //Must be after regions and locations
		loadEasterEggs();
		registerEvents();
		registerCommands();
		setupPlayers(); //In case of /reload, we need to pretend players just logged in.
	}
	
	private void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BlockToInventoryEvent(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ClickSellSignEvent(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ClickEggEvent(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new CaravanDriver(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerLinkEvent(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ChangeWorld(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BloatChatEvent(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BlockAchievementGUI(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new DoubleJump(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new FormatChatEvent(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BackpackUpgrade(this), this);
	}
	
	private void registerCommands() {
		this.getCommand("rankup").setExecutor(new CommandRankup(this));
		this.getCommand("achievement").setExecutor(new CommandAchievement(this));
		this.getCommand("backpack").setExecutor(new CommandBackpack(this));
		this.getCommand("prison").setExecutor(new CommandPrison(this));
		this.getCommand("bloat").setExecutor(new CommandBloat());
		this.getCommand("sell").setExecutor(new CommandSell());
	}
	
	private void setupPlayers() {
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			getPrisonManager().setupPlayer(p);
		}
	}
	
	private void loadConfig() {
		Config.MINING_KEY_RATE = getConfig().getInt("miningKeyRate");
		Config.MESSAGE_PREFIX = getConfig().getString("message-prefix").replaceAll("&", "§");
		Config.MINE_WORLDS = getConfig().getStringList("blocksToInventoryWorlds");
	}
	
	private void loadRegions() {
		try {
			for(String s : getConfig().getConfigurationSection("regions").getKeys(false)) {
				String world = getConfig().getString("regions." + s + ".world");
				int minX = getConfig().getInt("regions." + s + ".min.x");
				int minY = getConfig().getInt("regions." + s + ".min.y");
				int minZ = getConfig().getInt("regions." + s + ".min.z");
				
				int maxX = getConfig().getInt("regions." + s + ".max.x");
				int maxY = getConfig().getInt("regions." + s + ".max.y");
				int maxZ = getConfig().getInt("regions." + s + ".max.z");
				
				World w = Bukkit.getServer().getWorld(world);
				Location min = new Location(w, minX, minY, minZ);
				Location max = new Location(w, maxX, maxY, maxZ);
				Region r = new Region(min, max);
				if(getConfig().contains("regions." + s + ".flags")) {
					List<String> flags = getConfig().getStringList("regions." + s + ".flags");
					r.getFlags().addAll(flags);
				}
				RegionManager.addRegion(s, r);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadLocations() {
		try {
			for(String s : getConfig().getConfigurationSection("locations").getKeys(false)) {
				String world = getConfig().getString("locations." + s + ".world");
				double x = getConfig().getDouble("locations." + s + ".x");
				double y = getConfig().getDouble("locations." + s + ".y");
				double z = getConfig().getDouble("locations." + s + ".z");
				float yaw = (float) getConfig().getDouble("locations." + s + ".yaw");
				float pitch = (float) getConfig().getDouble("locations." + s + ".pitch");
				
				World w = Bukkit.getServer().getWorld(world);
				Location loc = new Location(w, x, y, z, yaw, pitch);
				LocationManager.addLocation(s, loc);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadLinks() {
		try {
			for(String s : getConfig().getConfigurationSection("links").getKeys(false)) {
				String region = getConfig().getString("links." + s + ".region");
				String location = getConfig().getString("links." + s + ".location");
				PrisonWarp warp = new PrisonWarp(region, location);
				PrisonLinks.addWarp(warp);
				if(getConfig().contains("links." + s + ".permission")) {
					warp.setPermission(getConfig().getString("links." + s + ".permission"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadRanks() {
		if(!getDataFolder().exists()) { return; }
		for(String rank : getConfig().getConfigurationSection("ranks").getKeys(false)) {
			double value = getConfig().getDouble("ranks." + rank);
			PrisonRank pr = new PrisonRank(rank, value);
			PrisonRank.addPrisonRank(pr);
			System.out.println("Loaded rank [" + rank + "] $" + value);
		}
	}
	
	private void loadKingdoms() {
		if(!getDataFolder().exists()) { return; }
		
	}
	
	private void loadEasterEggs() {
		
		if(getDataFolder().exists()) {
			try {				
				for(String s : getConfig().getConfigurationSection("eggs").getKeys(false)) {
					String name = getConfig().getString("eggs." + s + ".name");
					name = name.replaceAll("&", "§");
					String worldName = getConfig().getString("eggs." + s + ".location.world");
					int x = getConfig().getInt("eggs." + s + ".location.x");
					int y = getConfig().getInt("eggs." + s + ".location.y");
					int z = getConfig().getInt("eggs." + s + ".location.z");
					Location loc = new Location(Bukkit.getServer().getWorld(worldName), x, y, z);
					List<String> lore = getConfig().getStringList("eggs." + s + ".lore");
					for(String l : lore) {
						l = l.replaceAll("&", "§");
					}
					int id = Integer.parseInt(s);
					EasterEgg.addEasterEgg(new EasterEgg(name, lore, loc, id));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void resetEasterEggs() {
		EasterEgg.clear();
	}
	
	private void resetAchievements() {
		PrisonAchievements.clear();
	}
	
	public void refreshConfig() {
		reloadConfig();
		loadConfig();
		resetEasterEggs();
		resetAchievements();
		loadMineShops();
		loadEasterEggs();
		loadLinks();
		loadLocations();
		loadRanks();
		loadRegions();
	}
	
	private void setupSQL() {
		try {
			sql = new MySQL(DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD);
			c = sql.open();
			c.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadMineShops() {
		mineShops.clear();
		for(String s : getConfig().getConfigurationSection("shops").getKeys(false)) {
			MineShop shop = new MineShop(s);
			for(String subItem : getConfig().getConfigurationSection("shops." + s).getKeys(false)) {
				//ItemStack item = ItemLoader.loadItem(getConfig().getString("shops." + s + "." + subItem + ".item"));
				ItemStack item = null;
				try {
					item = essentials.getItemDb().get(getConfig().getString("shops." + s + "." + subItem + ".item"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				double price = getConfig().getDouble("shops." + s + "." + subItem + ".price");
				shop.getValues().put(item, price);
			}
			mineShops.add(shop);
			System.out.println("Loaded Mineshop [" + shop.getMineID() + "]");
			for(ItemStack entry : shop.getValues().keySet()) {
				System.out.println("   " + entry.getType() + ":" + entry.getData().getData() + " $" + shop.getValues().get(entry));
			}
		}
		
	}
	
	public void saveRegion(String name, Region region) {
		getConfig().set("regions." + name + ".world", region.getMin().getWorld().getName());
		getConfig().set("regions." + name + ".min.x", region.getMin().getBlockX());
		getConfig().set("regions." + name + ".min.y", region.getMin().getBlockY());
		getConfig().set("regions." + name + ".min.z", region.getMin().getBlockZ());
		
		getConfig().set("regions." + name + ".max.x", region.getMax().getBlockX());
		getConfig().set("regions." + name + ".max.y", region.getMax().getBlockY());
		getConfig().set("regions." + name + ".max.z", region.getMax().getBlockZ());
		
		getConfig().set("regions." + name + ".flags", region.getFlags());
		saveConfig();
		reloadConfig();
		
		RegionManager.addRegion(name, region);
	}
	
	public void saveLocation(String name, Location loc) {
		getConfig().set("locations." + name + ".world", loc.getWorld().getName());
		getConfig().set("locations." + name + ".x", loc.getX());
		getConfig().set("locations." + name + ".y", loc.getY());
		getConfig().set("locations." + name + ".z", loc.getZ());
		getConfig().set("locations." + name + ".yaw", loc.getYaw());
		getConfig().set("locations." + name + ".pitch", loc.getPitch());
		saveConfig();
		reloadConfig();
		
		LocationManager.addLocation(name, loc);
	}
	
	public void saveLink(String name, PrisonWarp warp) {
		getConfig().set("links." + name + ".region", warp.getRegion());
		getConfig().set("links." + name + ".location", warp.getLocation());
		saveConfig();
		reloadConfig();
		
		PrisonLinks.addWarp(warp);
	}
	
	public void saveEasterEgg(EasterEgg egg) {
		getConfig().set("eggs." + egg.getId() + ".name", egg.getName());
		getConfig().set("eggs." + egg.getId() + ".location.world", egg.getLocation().getWorld().getName());
		getConfig().set("eggs." + egg.getId() + ".location.x", egg.getLocation().getBlockX());
		getConfig().set("eggs." + egg.getId() + ".location.y", egg.getLocation().getBlockY());
		getConfig().set("eggs." + egg.getId() + ".location.z", egg.getLocation().getBlockY());
		getConfig().set("eggs." + egg.getId() + ".lore", egg.getLore());
		saveConfig();
		reloadConfig();
	}
	
	public static Economy getEco() {
		return eco;
	}
	
	public List<MineShop> getMineShops() {
		return mineShops;
	}
	
	public static PrisonManager getPrisonManager() {
		return manager;
	}
	
	public KingdomManager getKingdomManager() {
		return kingdom;
	}
	
	public DatabaseManager getDatabaseManager() {
		return dbm;
	}
	
	public static TextCycle getStaffOnline() {
		return staffOnline;
	}
	
	public static Essentials getEssentials() {
		return essentials;
	}
	
	public static VNEssentials getVNEssentials() {
		return vnessentials;
	}
	
	public WorldEditPlugin getWorldEdit() {
		return worldEdit;
	}
	
	public MineShop getMineShop(String id) {
		for(MineShop shop : mineShops) {
			if(shop.getMineID().equalsIgnoreCase(id)) {
				return shop;
			}
		}
		return null;
	}
	
	public Connection getSQL() {
		return c;
	}
	
	public static Prison instance() {
		return instance;
	}
	
	public static PlayerUtils getPlayerUtils() {
		return playerUtils;
	}
}
