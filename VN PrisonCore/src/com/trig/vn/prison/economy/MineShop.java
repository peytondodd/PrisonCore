package com.trig.vn.prison.economy;

import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;

public class MineShop {

	private String mineID;
	private HashMap<ItemStack, Double> values = new HashMap<ItemStack, Double>();
	
	public MineShop(String mineID) {
		this.mineID = mineID;
	}
	
	public String getMineID() {
		return mineID;
	}
	
	public HashMap<ItemStack, Double> getValues() {
		return values;
	}
	
	public double getValue(ItemStack item) {
		return values.get(item);
	}
	
	public void sell(PrisonPlayer p) {
		for(ItemStack item : values.keySet()) {
			for(int i = 1; i <= 2; i++) { //Loop through two inventories
				int a = 0;
				ItemStack it = item.clone();
				MaterialData data = item.getData();
				it.setData(data);
				switch(i) {
				case 1:
					a = getAmountOfItem(item, p.getInventory());
					if(a <= 0) { continue; }
					it.setAmount(a);
					p.getInventory().removeItem(it);
					break;
				case 2:
					a = getAmountOfItem(item, p.getAlternativeInventory());
					if(a <= 0) { continue; }
					it.setAmount(a);
					p.getAlternativeInventory().removeItem(it);
					break;
				}
				double value = getValue(item);
				double total = value * a;
				total *= Multiplier.getMultiplier(p);
				
				Prison.getEco().depositPlayer(p, total);
				p.sendMessage("§a$" + total + " §7was added to your account for selling " + item.getType());
			}
		}
	}
	
	private int getAmountOfItem(ItemStack item, Inventory inv) {
		int a = 0;
		for(ItemStack itemstack : inv.getContents()) {
			if(itemstack == null) { continue; }
			if(itemstack.getType() == item.getType() && itemstack.getData().getData() == item.getData().getData()) {
				a += itemstack.getAmount();
			}
		}
		return a;
	}
}
