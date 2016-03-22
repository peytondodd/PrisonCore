package com.trig.vn.prison;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.bukkit.Bukkit;

import com.trig.vn.prison.achievements.PrisonAchievements;

public class DatabaseManager {

	private Prison main;
	private Connection c;
	
	public DatabaseManager(Prison main) {
		this.main = main;
		this.c = main.getSQL();
	}
	
	public String getPrisonRankName(String uuid) {
		try {
			PreparedStatement statement = c.prepareStatement("SELECT * FROM t_ranks WHERE uuid=?");
			statement.setString(1, uuid);
			ResultSet results = statement.executeQuery();
			results.next();
			String rank = results.getString("rank");
			results.close();
			statement.close();
			return rank;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Prison.getPrisonRanks().get(0).getName(); //If they don't have a rank, they do now.
	}
	
	public void loadAchievements(PrisonPlayer p) {
		try {
			PreparedStatement statement = c.prepareStatement("SELECT * FROM t_achievements WHERE uuid=?");
			statement.setString(1, p.getUniqueId().toString());
			ResultSet results = statement.executeQuery();
			if(results == null) {
				statement.close();
				return;
			}
			while(results.next()) {
				int id = results.getInt("id");
				p.getAchievements().addAchievement(PrisonAchievements.getAchievement(id));
			}
			results.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
