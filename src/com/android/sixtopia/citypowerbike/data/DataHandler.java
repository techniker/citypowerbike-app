package com.android.sixtopia.citypowerbike.data;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.android.sixtopia.citypowerbike.data.abcp.ABCPProtocolHandler;
import com.android.sixtopia.citypowerbike.gui.Display;
import com.android.sixtopia.citypowerbike.util.Tags;

import android.util.Log;

public class DataHandler implements Observer {
	
	private ABCPProtocolHandler abcp;
	private DataListener dataListener;
	private Display display = new Display();
	
	public DataHandler(){
		abcp = new ABCPProtocolHandler(this);
		abcp.connect();
	}
	public void addDataListener(DataListener dataListener){
		this.dataListener = dataListener;
	}
	
	public void setLight(boolean on) throws IOException{
		abcp.sendLightFrame(on);
	}
	@Override
	public void update(Observable arg0, Object data) {
		Log.e(Tags.LOG_TAG, "Object received in DataHandler");
		if(data instanceof DataBiosignal){
			Log.d(Tags.LOG_TAG, "DataHandler received DataBiosignal object");
			Log.d(Tags.LOG_TAG_TO_STRING, data.toString());
			//TODO: Fill in Display Attributes
			display.setPuls(((DataBiosignal) data).getPuls());
			dataListener.onNewDataReceived(display);
		}
		if(data instanceof DataBMS){
			Log.d(Tags.LOG_TAG, "DataHandler received DataBMS object");
			Log.d(Tags.LOG_TAG_TO_STRING, data.toString());
			//TODO: Fill in Display Attributes
			display.setBatteryStatus(((DataBMS) data).getChargingStatus());
			display.setCellVoltages(((DataBMS) data).getCellVoltage());
			display.setElectricityValue(((DataBMS) data).getEntireCurrent());
			display.setTemperature(((DataBMS) data).getTemperature());
			boolean [] relays = {((DataBMS) data).isRelay1Set(), ((DataBMS) data).isRelay2Set(), ((DataBMS) data).isRelay3Set()};
			display.setPositionOfSchuetze(relays);
			display.setChargingStatus(((DataBMS) data).isCharging());
			dataListener.onNewDataReceived(display);
		}
		if(data instanceof DataLight){
			Log.d(Tags.LOG_TAG, "DataHandler received DataLight object");
			Log.d(Tags.LOG_TAG_TO_STRING, data.toString());
			//TODO: Fill in Display Attributes
			display.setLightOn(((DataLight) data).isLightOn());
			dataListener.onNewDataReceived(display);
		}
		if(data instanceof DataSpeedMeter){
			Log.d(Tags.LOG_TAG, "DataHandler received DataSpeedMeter object");
			Log.d(Tags.LOG_TAG_TO_STRING, data.toString());
			//TODO: Fill in Display Attributes
			display.setAverageSpeed(((DataSpeedMeter) data).getAverageSpeed());
			display.setMaximumSpeed(((DataSpeedMeter) data).getMaximumSpeed());
			display.setCurrentSpeed(((DataSpeedMeter) data).getCurrentSpeed());
			display.setMaximumAcceleration(((DataSpeedMeter) data).getMaximumVelocity());
			display.setCurrentAcceleration(((DataSpeedMeter) data).getCurrentVelocity());
			display.setDrivenKilometers(((DataSpeedMeter) data).getEntireDistance());
			display.setTrip(((DataSpeedMeter) data).getTrack());
			
			dataListener.onNewDataReceived(display);
		}
	}
	
	public Display getDisplay(){
		return this.display;
	}

}
