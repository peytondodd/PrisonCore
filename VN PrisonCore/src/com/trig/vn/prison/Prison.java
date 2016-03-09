package com.trig.vn.prison;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.trig.vn.db.DatabaseConfig;
import com.trig.vn.prison.commands.CommandRankup;
import com.trig.vn.prison.economy.MineShop;
import com.trig.vn.prison.listeners.BlockToInventoryEvent;
import com.trig.vn.prison.ranks.PrisonRank;
import com.trig.vn.prison.ranks.listeners.PlayerJoin;
import com.vn.core.Core;
import com.vn.core.sql.MySQL;

public class Prison extends JavaPlugin {

	private List<String> blockToInventoryWorlds = new ArrayList<String>();
	private List<MineShop> mineShops = new ArrayList<MineShop>();
	private static List<PrisonRank> prisonRanks = new ArrayList<PrisonRank>();
	private static Economy eco;
	private static Prison instance;
	private PrisonManager manager;
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
		dbm = new DatabaseManager(this);
	}
	
	private void setup() {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		eco = rsp.getProvider();
		core = (Core) Bukkit.getServer().getPluginManager().getPlugin("VNCore");
		Bukkit.getServer().getPluginManager().registerEvents(new BlockToInventoryEvent(this), this);
		
		if(!getDataFolder().exists()) { //Copy default config
			System.out.println("Creating config file...");
			saveConfig(); //Create all the stuff we need
			System.out.println("Starting to copy...");
			BufferedReader reader = new BufferedReader(new InputStreamReader(getResource("config.yml")));
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getDataFolder() + "/config.yml")));
				String line;
				while((line = reader.readLine()) != null) {
					writer.write(line);
					writer.newLine();
				}
				reader.close();
				writer.flush();
				writer.close();
				System.out.println("Configuration copy complete");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		setupSQL();
		core.registerSQLConnection(c);
		loadBlockToInventoryWorlds();
		loadMineShops();
		loadRanks();
		registerEvents();
		registerCommands();
		
	}
	
	private void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
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
				ItemStack item = getConfig().getItemStack("shops." + s + "." + subItem + ".item");
				double price = getConfig().getDouble("shops." + s + "." + subItem + ".price");
				shop.getValues().put(item, price);
			}
			mineShops.add(shop);
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
