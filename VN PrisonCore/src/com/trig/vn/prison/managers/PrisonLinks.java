package com.trig.vn.prison.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.trig.vn.prison.objects.PrisonWarp;

public class PrisonLinks {

	private static List<PrisonWarp> links = new ArrayList<PrisonWarp>();
	
	public static void addWarp(PrisonWarp w) {
		links.add(w);
	}
	
	public static void cleanup() {
		links.clear();
	}
	
	public static List<PrisonWarp> getLinks() {
		return links;
	}
	
	public static boolean linkValid(String region) {
		for(PrisonWarp warp : links) {
			if(warp.getRegion().equalsIgnoreCase(region)) {
				return true;
			}
		}
		return false;
	}
	
	public static void link(String region, Player p) {
		for(PrisonWarp warp : links) {
			if(warp.getRegion().equalsIgnoreCase(region)) {
				warp.warp(p);
				return;
			}
		}
	}
	
	public static PrisonWarp getLinkForRegion(String region) {
		if(linkValid(region)) {
			for(PrisonWarp warp : links) {
				if(warp.getRegion().equalsIgnoreCase(region)) {
					return warp;
				}
			}
		}
		return null;
	}

}
