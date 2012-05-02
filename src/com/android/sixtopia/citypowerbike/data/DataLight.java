package com.android.sixtopia.citypowerbike.data;

public class DataLight {
	
	private boolean lightOn;
	private boolean live;
	
	public DataLight(boolean lightOn, boolean live){
		this.setLightOn(lightOn);
		this.setLive(live);
	}

	public DataLight(){}
	
	public void setLightOn(boolean lightOn) {
		this.lightOn = lightOn;
	}

	public boolean isLightOn() {
		return lightOn;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}
	public String toString(){
		String msg = "\nDataLight\n";
		
		if(live) msg += "Status: OK\n";
		else msg += "Status: DEAD\n";
		
		if(lightOn) msg += "Light: ON\n";
		else msg += "Light: OFF\n";
		
		return msg;
	}
}
