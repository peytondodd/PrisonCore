package com.trig.vn.prison;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.bukkit.Bukkit;

import com.trig.vn.prison.achievements.PrisonAchievements;
import com.trig.vn.prison.ranks.PrisonRank;

public class DatabaseManager {

	private Prison main;
	private Connection c;
	
	public DatabaseManager(Prison main) {
		this.main = main;
		this.c = main.getSQL();
	}
	
	public String getPrisonRankName(String uuid) {
		try {
			PreparedStatement statement = c.prepareStatement("SELECT * FROM t_players WHERE uuid=?");
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
		return PrisonRank.getPrisonRanks().get(0).getName(); //If they don't have a rank, they do now.
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
	
	public int getBackpackSize(PrisonPlayer p) {
		try {
			PreparedStatement statement = c.prepareStatement("SELECT * FROM t_players WHERE uuid=?");
			statement.setString(1, p.getUniqueId().toString());
			ResultSet results = statement.executeQuery();
			results.next();
			int slots = results.getInt("backpack");
			results.close();
			statement.close();
			return slots;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 9;
	}
	
	public void registerPlayer(PrisonPlayer p) {
		System.out.println("Registering player: " + p.getName() + " (" + p.getUniqueId().toString() + ")");
		try {
			PreparedStatement statement = c.prepareStatement("INSERT IGNORE INTO t_players (uuid, rank, backpack) VALUES (?, ?, ?)");
			statement.setString(1, p.getUniqueId().toString());
			statement.setString(2, PrisonRank.getDefaultRank().getName());
			statement.setInt(3, 9);
			statement.executeUpdate();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
