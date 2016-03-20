package com.trig.vn.prison.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ItemLoader {

	public static ItemStack loadItem(String item) {
		System.out.println("Loading item: " + item);
		ItemStack itemstack = null;
		if(item.contains(":")) {
			String[] parts = item.split("\\:");
			int itemId = Integer.parseInt(parts[0]);
			int magic = Integer.parseInt(parts[1]);
			System.out.println("Parsed item: ");
			System.out.println("Item ID: " + itemId);
			System.out.println("Data Value: " + magic);
			itemstack = new ItemStack(itemId);
			MaterialData data = itemstack.getData();
			data.setData((byte) magic);
			itemstack.setData(data);
			return itemstack;
		} else {
			itemstack = new ItemStack(Integer.parseInt(item));
			return itemstack;
		}
		
	}
}
