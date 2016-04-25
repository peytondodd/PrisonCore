package com.trig.vn.prison.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.trig.vn.prison.Prison;

public class SpawnTools {

	private static int temp = 100;
	private static int iterations = 0;
	
	public static void lightningLine() {
		temp = 100;
		iterations = 0;
		final Location center = new Location(Bukkit.getServer().getWorld("prison"), -60, 141, -60);
		for(int i = 0; i < 50; i++) {
			iterations++;
			Bukkit.getServer().getScheduler().runTaskLater(Prison.instance(), new Runnable() {
				public void run() {
					center.getWorld().strikeLightningEffect(new Location(center.getWorld(), center.getX() - temp, center.getY(), center.getZ()));
					center.getWorld().strikeLightningEffect(new Location(center.getWorld(), center.getX() + temp, center.getY(), center.getZ()));
					temp -= 2;
				}
			}, 5 * iterations);
		}
	}
	
	public static void test() {
		BossBar bar = Bukkit.getServer().createBossBar("§6King's Soul", BarColor.GREEN, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG, BarFlag.DARKEN_SKY);
		for(Player p : Bukkit.getServer().getWorld("prison").getPlayers()) {
			bar.addPlayer(p);
		}
		bar.show();
		bar.setProgress(1);
	}
	
	public static void lightningCircle(final Location center, final int radius) {
		final int y = center.getBlockY();
		int iterations = 0;
		for(double i = 0.0; i < 360.0; i += 15) {
			final double degrees = i;
			Bukkit.getServer().getScheduler().runTaskLater(Prison.instance(), new Runnable() {
				public void run() {
					double angle = Math.toRadians(degrees); //degrees * Math.PI / 180;
					int x = (int)(center.getX() + radius * Math.cos(angle));
					int z = (int)(center.getZ() + radius * Math.sin(angle));					
					center.getWorld().strikeLightningEffect(new Location(center.getWorld(), x, y, z));
					
				}
			}, 7 * (iterations++));
		}
	}
}
