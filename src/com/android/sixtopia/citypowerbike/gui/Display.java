package com.android.sixtopia.citypowerbike.gui;


public class Display {

	/**
	 * This Display Class works as an interface between GUI and Protocol Level
	 * Please make sure that the same object of Display is used when setting or getting information of it
	 */
	
	//Light
	private boolean bLightOn;
	
	//Speed in km/h
	private float fCurrentSpeed;
	private float fAverageSpeed;
	private float fMaximumSpeed;
	
	//Acceleration in m/s^2
	private float fCurrentAcceleration;
	private float fMaximumAcceleration;
	
	//Kilometers and Gradient in �
	private float fGradient;
	private float fDrivenKilometers;
	private float fTrip;
	
	//Battery Infos
	private float fBatteryStatus;
	private float fElectricityValue;
	private int temperature;
	//0 - Schuetze 1; 1 - Schuetze 2; 2 - Schuetze 3
	private boolean bPositionOfSchuetze[] = new boolean[3];
	//0 - Cell 1; 1 - Cell 2; ....
	//.getLength to get number of cells
	private int fCellVoltages[];
	private boolean chargingStatus;
	
	//Biosognal
	private int puls;
	
		

	/**
	 * get lightStatus
	 * @return lightOn : true - on; false - off
	 */
	public boolean isLightOn() {
		return bLightOn;
	}
	/**
	 * set lightStatus
	 * @param lightOn
	 */
	public void setLightOn(boolean lightOn) {
		this.bLightOn = lightOn;
	}

	
	/**
	 * set the Speed for displaying in textfield
	 * @param speed in km/h
	 */
	public void setCurrentSpeed(float speed) {
		this.fCurrentSpeed = speed;
	}
	
	/**
	 * get the current speed
	 * @return speed 
	 */
	public float getCurrentSpeed(){
		return fCurrentSpeed;
	}
	
	/**
	 * 
	 * @param fGradient in �
	 */
	public void setGradient(float fGradient) {
		this.fGradient = fGradient;
	}
	
	/**
	 * 
	 * @return Gradient in �
	 */
	public float getGradient() {
		return fGradient;
	}
	
	
	/**
	 * @param fMaximumAcceleration the fMaximumAcceleration to set
	 */
	public void setMaximumAcceleration(float fMaximumAcceleration) {
		this.fMaximumAcceleration = fMaximumAcceleration;
	}
	/**
	 * @return the fMaximumAcceleration
	 */
	public float getMaximumAcceleration() {
		return fMaximumAcceleration;
	}
	
	/**
	 * @param fDrivenKilometers the fDrivenKilometers to set
	 */
	public void setDrivenKilometers(float fDrivenKilometers) {
		this.fDrivenKilometers = fDrivenKilometers;
	}
	/**
	 * @return the fDrivenKilometers
	 */
	public float getDrivenKilometers() {
		return fDrivenKilometers;
	}
	
	/**
	 * @param fTrip the fTrip to set
	 */
	public void setTrip(float fTrip) {
		this.fTrip = fTrip;
	}
	/**
	 * @return the fTrip
	 */
	public float getTrip() {
		return fTrip;
	}
	/**
	 * @param fAverageSpeed the fAverageSpeed to set
	 */
	public void setAverageSpeed(float fAverageSpeed) {
		this.fAverageSpeed = fAverageSpeed;
	}
	/**
	 * @return the fAverageSpeed
	 */
	public float getAverageSpeed() {
		return fAverageSpeed;
	}
	/**
	 * @param fMaximumSpeed the fMaximumSpeed to set
	 */
	public void setMaximumSpeed(float fMaximumSpeed) {
		this.fMaximumSpeed = fMaximumSpeed;
	}
	/**
	 * @return the fMaximumSpeed
	 */
	public float getMaximumSpeed() {
		return fMaximumSpeed;
	}
	/**
	 * @param fCurrentAcceleration the fCurrentAcceleration to set
	 */
	public void setCurrentAcceleration(float fCurrentAcceleration) {
		this.fCurrentAcceleration = fCurrentAcceleration;
	}
	/**
	 * @return the fCurrentAcceleration
	 */
	public float getCurrentAcceleration() {
		return fCurrentAcceleration;
	}
	/**
	 * @param bPositionOfSchuetze the bPositionOfSchuetze to set
	 */
	public void setPositionOfSchuetze(boolean bPositionOfSchuetze[]) {
		this.bPositionOfSchuetze = bPositionOfSchuetze;
	}
	/**
	 * @return the bPositionOfSchuetze
	 */
	public boolean[] getPositionOfSchuetze() {
		return bPositionOfSchuetze;
	}
	/**
	 * @param fBatteryStatus the fBatteryStatus to set
	 */
	public void setBatteryStatus(float fBatteryStatus) {
		this.fBatteryStatus = fBatteryStatus;
	}
	/**
	 * @return the fBatteryStatus
	 */
	public float getBatteryStatus() {
		return fBatteryStatus;
	}
	/**
	 * @param fElectricityValue the fElectricityValue to set
	 */
	public void setElectricityValue(float fElectricityValue) {
		this.fElectricityValue = fElectricityValue;
	}
	/**
	 * @return the fElectricityValue
	 */
	public float getElectricityValue() {
		return fElectricityValue;
	}
	/**
	 * @param fCellVoltages the fCellVoltages to set
	 */
	public void setCellVoltages(int fCellVoltages[]) {
		this.fCellVoltages = fCellVoltages;
	}
	/**
	 * @return the fCellVoltages
	 */
	public int[] getCellVoltages() {
		return fCellVoltages;
	}
	public void setPuls(int puls) {
		this.puls = puls;
	}
	public int getPuls() {
		return puls;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setChargingStatus(boolean chargingStatus) {
		this.chargingStatus = chargingStatus;
	}
	public boolean isChargingStatus() {
		return chargingStatus;
	}
	
}
