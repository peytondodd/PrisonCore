package com.trig.vn.prison;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.trig.vn.prison.listeners.BlockToInventoryEvent;

public class Prison extends JavaPlugin {

	private List<String> blockToInventoryWorlds = new ArrayList<String>();
	private List<MineShop> mineShops = new ArrayList<MineShop>();
	private static Economy eco;
	
	public void onEnable() {
		setup();
	}
	
	private void setup() {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		eco = rsp.getProvider();
		Bukkit.getServer().getPluginManager().registerEvents(new BlockToInventoryEvent(this), this);
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
	
	public MineShop getMineShop(String id) {
		for(MineShop shop : mineShops) {
			if(shop.getMineID().equalsIgnoreCase(id)) {
				return shop;
			}
		}
		return null;
	}
}
