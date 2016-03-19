package com.trig.vn.prison.economy;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.trig.vn.prison.Prison;

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
	
	public void sell(Player p) {
		for(ItemStack item : values.keySet()) {
			if(p.getInventory().containsAtLeast(item, 1)) {
				double value = getValue(item);
				int a = getAmountOfItem(item, p);
				double total = value * a;
				total *= Multiplier.getMultiplier(p);
				ItemStack i = item.clone();
				i.setAmount(a);
				p.getInventory().removeItem(i);
				Prison.getEco().depositPlayer(p, total);
				p.sendMessage("§a$" + total + " §7 was added to your account for selling " + item.getType());
			}
		}
	}
	
	private int getAmountOfItem(ItemStack item, Player p) {
		int a = 0;
		for(ItemStack itemstack : p.getInventory().getContents()) {
			if(itemstack == null) { continue; }
			if(itemstack.getType() == item.getType()) {
				a += itemstack.getAmount();
			}
		}
		return a;
	}
}
