package com.trig.vn.prison.utils;

import java.util.ArrayList;
import java.util.List;

import com.trig.vn.prison.Prison;

public class TextCycle {

	private List<String> cycles = new ArrayList<String>();
	private int index = 0;
	
	public void update() {
		cycles = new ArrayList<String>();
		cycles.addAll(Prison.getPlayerUtils().getTrialMods().getUsernames());
		cycles.addAll(Prison.getPlayerUtils().getMods().getUsernames());
		cycles.addAll(Prison.getPlayerUtils().getAdmins().getUsernames());
		cycles.addAll(Prison.getPlayerUtils().getOwners().getUsernames());
	}
	
	public String next() {
		if(cycles.size() == 0) {
			return "None";
		}
		
		index++;
		if(index > cycles.size() - 1) {
			index = 0;
		}
		
		return cycles.get(index);
	}
	
	public String getCurrent() {
		if(cycles.size() == 0) {
			return "None";
		}
		return cycles.get(index);
	}
	
}
