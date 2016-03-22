package com.trig.vn.prison;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.trig.vn.db.DatabaseConfig;
import com.trig.vn.prison.commands.CommandRankup;
import com.trig.vn.prison.economy.MineShop;
import com.trig.vn.prison.eggs.EasterEgg;
import com.trig.vn.prison.kingdoms.KingdomManager;
import com.trig.vn.prison.listeners.BlockToInventoryEvent;
import com.trig.vn.prison.listeners.ClickSellSignEvent;
import com.trig.vn.prison.ranks.PrisonRank;
import com.trig.vn.prison.ranks.listeners.PlayerJoin;
import com.trig.vn.prison.utils.ItemLoader;
import com.vn.core.Core;
import com.vn.core.sql.MySQL;

public class Prison extends JavaPlugin {

	private List<String> blockToInventoryWorlds = new ArrayList<String>();
	private List<MineShop> mineShops = new ArrayList<MineShop>();
	private static List<PrisonRank> prisonRanks = new ArrayList<PrisonRank>();
	private static Economy eco;
	private static Prison instance;
	private PrisonManager manager;
	private KingdomManager kingdom;
	private DatabaseManager dbm;
	
	private Core core;
	
	private MySQL sql;
	private Connection c;
	private final String DB_HOST = DatabaseConfig.getValueFromConfig("DB_HOST");
	private final String DB_PORT = DatabaseConfig.getValueFromConfig("DB_PORT");
	private final String DB_NAME = "db_prison";
	private final String DB_USER = DatabaseConfig.getValueFromConfig("DB_USER");
	private final String DB_PASSWORD = DatabaseConfig.getValueFromConfig("DB_PASSWORD");
	
	public void onEnable() {
		instance = this;
		setup();
		manager = new PrisonManager(this);
		kingdom = new KingdomManager(this);
		dbm = new DatabaseManager(this);
	}
	
	private void setup() {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		eco = rsp.getProvider();
		core = (Core) Bukkit.getServer().getPluginManager().getPlugin("VNCore");
		
		setupSQL();
		core.registerSQLConnection(c);
		loadBlockToInventoryWorlds();
		loadMineShops();
		loadRanks();
		loadEasterEggs();
		registerEvents();
		registerCommands();
		
	}
	
	private void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BlockToInventoryEvent(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ClickSellSignEvent(this), this);
	}
	
	private void registerCommands() {
		this.getCommand("rankup").setExecutor(new CommandRankup(this));
	}
	
	private void loadRanks() {
		if(!getDataFolder().exists()) { return; }
		for(String rank : getConfig().getConfigurationSection("ranks").getKeys(false)) {
			double value = getConfig().getDouble("ranks." + rank);
			PrisonRank pr = new PrisonRank(rank, value);
			prisonRanks.add(pr);
			System.out.println("Loaded rank [" + rank + "] $" + value);
		}
	}
	
	private void loadEasterEggs() {
		if(getDataFolder().exists()) {
			for(String s : getConfig().getConfigurationSection("eggs").getKeys(false)) {
				String name = getConfig().getString("eggs." + s + ".name");
				String worldName = getConfig().getString("eggs." + s + ".location.world");
				int x = getConfig().getInt("eggs." + s + ".location.x");
				int y = getConfig().getInt("eggs." + s + ".location.y");
				int z = getConfig().getInt("eggs." + s + ".location.z");
				Location loc = new Location(Bukkit.getServer().getWorld(worldName), x, y, z);
				List<String> lore = getConfig().getStringList("eggs." + s + ".lore");
				EasterEgg.addEasterEgg(new EasterEgg(name, lore, loc));
			}
		}
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
	
	private void loadBlockToInventoryWorlds() {
		blockToInventoryWorlds = getConfig().getStringList("blocksToInventoryWorlds");
	}
	
	private void loadMineShops() {
		for(String s : getConfig().getConfigurationSection("shops").getKeys(false)) {
			MineShop shop = new MineShop(s);
			for(String subItem : getConfig().getConfigurationSection("shops." + s).getKeys(false)) {
				ItemStack item = ItemLoader.loadItem(getConfig().getString("shops." + s + "." + subItem + ".item"));
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
	
	public List<String> getBlockToInventoryWorlds() {
		return blockToInventoryWorlds;
	}
	
	public static Economy getEco() {
		return eco;
	}
	
	public List<MineShop> getMineShops() {
		return mineShops;
	}
	
	public PrisonManager getPrisonManager() {
		return manager;
	}
	
	public KingdomManager getKingdomManager() {
		return kingdom;
	}
	
	public DatabaseManager getDatabaseManager() {
		return dbm;
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
	
	public static PrisonRank getPrisonRank(String name) {
		for(PrisonRank rank : prisonRanks) {
			if(rank.getName().equalsIgnoreCase(name)) {
				return rank;
			}
		}
		return null;
	}
	
	public static List<PrisonRank> getPrisonRanks() {
		return prisonRanks;
	}
}
