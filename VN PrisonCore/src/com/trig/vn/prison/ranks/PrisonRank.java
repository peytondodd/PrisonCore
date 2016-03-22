package com.trig.vn.prison.ranks;

import org.bukkit.ChatColor;

import com.trig.vn.prison.Prison;



public class PrisonRank {
	
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
	
	public static boolean canRankup(PrisonPlayer player) {
		PrisonRank next = PrisonRank.getNextRank(player.getRank());
		if(next == null) {
			player.sendMessage(ChatColor.RED + "You are currently the highest rank!");
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
		for(int i = 0; i < Prison.getPrisonRanks().size(); i++) {
			if(Prison.getPrisonRanks().get(i).getName().equalsIgnoreCase(currentRank.getName())) {
				if(i + 1 > Prison.getPrisonRanks().size()) {
					return null;
				}
				return Prison.getPrisonRanks().get(i + 1);
			}
		}
		return null;
	}
	
	public static PrisonRank getPreviousRank(PrisonRank currentRank) {
		for(int i = 0; i < Prison.getPrisonRanks().size(); i++) {
			if(Prison.getPrisonRanks().get(i).getName().equalsIgnoreCase(currentRank.getName())) {
				if(i == 0) {
					return null;
				}
				return Prison.getPrisonRanks().get(i - 1);
			}
		}
		return null;
	}
}
