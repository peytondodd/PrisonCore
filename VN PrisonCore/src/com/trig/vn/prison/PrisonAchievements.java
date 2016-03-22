package com.trig.vn.prison;

import java.util.ArrayList;
import java.util.List;

public class PrisonAchievements {

	private List<String> achievements = new ArrayList<String>();
	
	public boolean hasAchievement(String a) {
		return achievements.contains(a);
	}
	
	public void unlockAchievement(String a) {
		achievements.add(a);
	}
}
