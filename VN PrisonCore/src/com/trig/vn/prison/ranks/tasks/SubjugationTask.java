package com.trig.vn.prison.ranks.tasks;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.trig.vn.prison.ranks.PrisonRank;

public class SubjugationTask extends TimerTask {

	private Timer timer;
	private PrisonRank main;
	
	public SubjugationTask(PrisonRank main, Timer timer) {
		this.main = main;
		this.timer = timer;
	}
	
	public void run() {
		timer.schedule(new DespawnSubjugationParty(main), System.currentTimeMillis(), TimeUnit.MINUTES.toMillis(10));
		//Spawn enemies
	}

}
