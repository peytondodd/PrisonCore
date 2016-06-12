package com.trig.vn.prison.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryCopy {

	public static void copy(Inventory i1, Inventory i2) {
		for(ItemStack item : i1.getContents()) {
			i2.addItem(item);
		}
	}
}
