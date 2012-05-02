package com.android.sixtopia.citypowerbike.data;

public class DataSpeedMeter {
	
	private boolean live;
	private int currentSpeed;
	private int maximumSpeed;
	private int averageSpeed;
	private float maximumVelocity;
	private float currentVelocity;
	private float track;
	private int entireDistance;
	
	public DataSpeedMeter(boolean live){
		this.live = live;
	}

	public DataSpeedMeter(){}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public int getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public int getMaximumSpeed() {
		return maximumSpeed;
	}

	public void setMaximumSpeed(int maximumSpeed) {
		this.maximumSpeed = maximumSpeed;
	}

	public int getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(int averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public float getMaximumVelocity() {
		return maximumVelocity;
	}

	public void setMaximumVelocity(float maximumVelocity) {
		this.maximumVelocity = maximumVelocity;
	}

	public float getCurrentVelocity() {
		return currentVelocity;
	}

	public void setCurrentVelocity(float currentVelocity) {
		this.currentVelocity = currentVelocity;
	}

	public float getTrack() {
		return track;
	}

	public void setTrack(float track) {
		this.track = track;
	}

	public int getEntireDistance() {
		return entireDistance;
	}

	public void setEntireDistance(int entireDistance) {
		this.entireDistance = entireDistance;
	}
	public String toString(){
		String msg = "\nDataSpeedMeter\n";
		
		if(live) msg += "Status: OK\n";
		else msg += "Status: DEAD\n";
		msg += "Current Speed: "+this.currentSpeed+" km/h\n";
		msg += "Maximum Speed: "+this.maximumSpeed+" km/h\n";
		msg += "Average Speed: "+this.averageSpeed+" km/h\n";
		msg += "Maximum Velocity: "+this.maximumVelocity+" m/s^2\n";
		msg += "Current Velocity: "+this.currentVelocity+" m/s^2\n";
		msg += "Track: "+this.track+" km\n";
		msg += "Entire Distance: "+this.entireDistance+" km\n";
		
		return msg;
		
	}
}
