package com.trig.vn.prison.achievements;

import java.util.List;

public class Achievement {

	private String name;
	private List<String> lore;
	private int id;
	private AchievementCategory category;
	
	
	public Achievement(String name, List<String> lore, AchievementCategory category, int id) {
		this.name = name;
		this.lore = lore;
		this.category = category;
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public List<String> getLore() {
		return lore;
	}


	public int getId() {
		return id;
	}
	
	public AchievementCategory getCategory() {
		return category;
	}
}
