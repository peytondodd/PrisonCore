package com.trig.vn.prison.kingdoms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;
import com.trig.vn.prison.ranks.PrisonRank;

public class KingdomRank {

	private static List<KingdomRank> kingdomRanks = new ArrayList<KingdomRank>();
	
	private String name;
	private double value;
	
	public KingdomRank(String name, double value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public double getValue() {
		return value;
	}
	
	public static boolean canRankup(PrisonPlayer player) {
		KingdomRank next = KingdomRank.getNextRank(player.getKingdomRank());
		if(next == null) {
			player.sendMessage(ChatColor.GOLD + "You are already the highest rank!");
			return false;
		}
		double amount = next.getValue();
		if(player.getKingdomReputation() >= amount) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isAheadOf(KingdomRank rank) {
		int slot = kingdomRanks.indexOf(rank);
		int current = kingdomRanks.indexOf(this);
		return (current >= slot);
	}
	
	public boolean equals(KingdomRank other) {
		return name.equalsIgnoreCase(other.getName());
	}
	
	public static void addKingdomRank(KingdomRank rank) {
		kingdomRanks.add(rank);
	}
	
	public static List<KingdomRank> getKingdomRanks() {
		return kingdomRanks;
	}
	
	public static KingdomRank getNextRank(KingdomRank rank) {
		for(int i = 0; i < kingdomRanks.size(); i++) {
			if(kingdomRanks.get(i).equals(rank)) {
				if(i < kingdomRanks.size()) {
					return kingdomRanks.get(i + 1);
				} else { //They are at the last rank
					return null;
				}
			}
		}
		return null;
	}
	
	public static KingdomRank getPreviousRank(KingdomRank rank) {
		for(int i = 0; i < kingdomRanks.size(); i++) {
			if(kingdomRanks.get(i).equals(rank)) {
				if(i > 0) {
					return kingdomRanks.get(i - 1);
				} else { //They are at the last rank
					return null;
				}
			}
		}
		return null;
	}
	
	public static KingdomRank getDefaultRank() {
		return getKingdomRanks().get(0);
	}
	
	public static KingdomRank getKingdomRank(String name) {
		for(KingdomRank r : getKingdomRanks()) {
			if(r.getName().equalsIgnoreCase(name)) {
				return r;
			}
		}
		return null;
	}
}
