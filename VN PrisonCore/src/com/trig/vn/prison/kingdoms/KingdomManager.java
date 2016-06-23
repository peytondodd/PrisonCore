package com.trig.vn.prison.kingdoms;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import com.trig.vn.prison.Prison;

public class KingdomManager {

	private Prison main;
	private Timer timer = new Timer();
	
	public KingdomManager(Prison main) {
		this.main = main;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date start = c.getTime();
		//timer.schedule(new WorldEventTask(), start, TimeUnit.HOURS.toMillis(1)); //Will run at every hour of the day.
	}
}
