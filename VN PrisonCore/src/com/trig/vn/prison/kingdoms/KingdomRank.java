package com.trig.vn.prison.kingdoms;

import java.util.ArrayList;
import java.util.List;

public class KingdomRank {

	private static List<KingdomRank> kingdomRanks = new ArrayList<KingdomRank>();
	
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
}
