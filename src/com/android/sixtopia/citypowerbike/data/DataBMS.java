package com.android.sixtopia.citypowerbike.data;

public class DataBMS {
	private boolean live;
	private boolean charging;
	private boolean relay1Set;
	private boolean relay2Set;
	private boolean relay3Set;
	private int [] cellVoltage = new int [14];
	private int  entireCurrent;
	private int chargingStatus;
	private int temperature;
	
	public DataBMS(boolean live){
		this.live = live;
	}

	public DataBMS(){}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isCharging() {
		return charging;
	}

	public void setCharging(boolean charging) {
		this.charging = charging;
	}

	public boolean isRelay1Set() {
		return relay1Set;
	}

	public void setRelay1Set(boolean relay1Set) {
		this.relay1Set = relay1Set;
	}

	public boolean isRelay2Set() {
		return relay2Set;
	}

	public void setRelay2Set(boolean relay2Set) {
		this.relay2Set = relay2Set;
	}

	public boolean isRelay3Set() {
		return relay3Set;
	}

	public void setRelay3Set(boolean relay3Set) {
		this.relay3Set = relay3Set;
	}

	public int[] getCellVoltage() {
		return cellVoltage;
	}

	public void setCellVoltage(int[] cellVoltage) {
		this.cellVoltage = cellVoltage;
	}

	public int getEntireCurrent() {
		return entireCurrent;
	}

	public void setEntireCurrent(int entireCurrent) {
		this.entireCurrent = entireCurrent;
	}

	public int getChargingStatus() {
		return chargingStatus;
	}

	public void setChargingStatus(int chargingStatus) {
		this.chargingStatus = chargingStatus;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public String toString(){
		String msg = "\nDataBMS:\n";
		
		if(live) msg += "Status: OK\n";
		else msg += "Status: DEAD\n";
		
		if(this.relay1Set) msg += "Relay 1: ON\n";
		else msg += "Relay 1: OFF\n";
		
		if(this.relay2Set) msg += "Relay 2: ON\n";
		else msg += "Relay 2: OFF\n";
		
		if(this.relay3Set) msg += "Relay 3: ON\n";
		else msg += "Relay 3: OFF\n";
		
		for(int i = 0; i < cellVoltage.length; i++){
			msg += "Cell " + (i+1) + " Voltage: "+cellVoltage[i]+" mV\n";
		}
		msg += "Entire Current: "+this.entireCurrent+" mA\n";
		msg += "Charging Status:"+this.chargingStatus+" %\n";
		msg += "Temperature: "+this.temperature+" �C\n";
		
		return msg;
	}
}
