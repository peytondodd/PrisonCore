package com.trig.vn.prison.ranks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.trig.vn.prison.Prison;
import com.trig.vn.prison.PrisonPlayer;

/*
 * TODO
 * Redo config so that ranks 1-20 will be under a section named "Kingdom 1" or something
 * Create more than one PrisonRank list, for each kingdom
 * Modify rankup to check next rank in current kingdom
 */

public class PrisonRank {
	private static List<PrisonRank> prisonRanks = new ArrayList<PrisonRank>();
	
	private String rankName; //The name of the rank
	private double value; //How much the rank costs
	
	public PrisonRank(String rankName, double value) {
		this.rankName = rankName;
		this.value = value;
	}
	
	public String getName() {
		return rankName;
	}
	
	public double getValue() {
		return value;
	}
	
	public boolean isAheadOf(PrisonRank rank) {
		int slot = prisonRanks.indexOf(rank);
		int current = prisonRanks.indexOf(this);
		return (current >= slot);
	}
	
	public boolean equals(PrisonRank other) {
		return rankName.equalsIgnoreCase(other.getName());
	}
	
	public static boolean canRankup(PrisonPlayer player) {
		PrisonRank next = PrisonRank.getNextRank(player.getRank());
		if(next == null) {
			player.sendMessage(ChatColor.GOLD + "You are already the highest rank!");
			return false;
		}
		double amount = next.getValue();
		if(Prison.getEco().getBalance(player.getPlayer()) >= amount) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static PrisonRank getNextRank(PrisonRank currentRank) {
		for(int i = 0; i < getPrisonRanks().size(); i++) {
			if(getPrisonRanks().get(i).getName().equalsIgnoreCase(currentRank.getName())) {
				if(i + 1 >= getPrisonRanks().size()) {
					return null;
				}
				return getPrisonRanks().get(i + 1);
			}
		}
		return null;
	}
	
	public static boolean isFinalRank(PrisonRank rank) {
		return (prisonRanks.get(prisonRanks.size() - 1).equals(rank));
	}
	
	public static PrisonRank getPreviousRank(PrisonRank currentRank) {
		for(int i = 0; i < getPrisonRanks().size(); i++) {
			if(getPrisonRanks().get(i).getName().equalsIgnoreCase(currentRank.getName())) {
				if(i == 0) {
					return null;
				}
				return getPrisonRanks().get(i - 1);
			}
		}
		return null;
	}
	
	public static PrisonRank getDefaultRank() {
		return getPrisonRanks().get(0);
	}
	
	public static void addPrisonRank(PrisonRank rank) {
		prisonRanks.add(rank);
	}
	
	public static PrisonRank getPrisonRank(String rank) {
		for(PrisonRank pr : prisonRanks) {
			if(pr.getName().equalsIgnoreCase(rank)) {
				return pr;
			}
		}
		return null;
	}
	
	public static List<PrisonRank> getPrisonRanks() {
		return prisonRanks;
	}
	
	@Override
	public String toString() {
		return rankName;
	}
}
