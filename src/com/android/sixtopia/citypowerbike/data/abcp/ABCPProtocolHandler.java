package com.android.sixtopia.citypowerbike.data.abcp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.android.sixtopia.citypowerbike.data.abcp.bluetooth.BluetoothService;
import com.android.sixtopia.citypowerbike.data.abcp.bluetooth.ReceiveListener;
import com.android.sixtopia.citypowerbike.util.Tags;

import android.util.Log;
/**
 * This class handles incoming ABCP-Frames and their processing.<br>
 * It implements also methods for connecting to a Bluetooth device via a {@code BluetoothConnection}
 *
 */
public class ABCPProtocolHandler extends Observable implements ReceiveListener{
	
	private ABCPProtocolContainer abcpContainer = new ABCPProtocolContainer();
	private BluetoothService btService;
	private ArrayList<Byte> header;
	private ArrayList<Byte> payload;
	
	/**
	 * Constructs a new Instance of a ABCPProtocolHandler
	 */
	public ABCPProtocolHandler(Observer observer){
		this.addObserver(observer);
		btService = new BluetoothService(null, this);
	}
	/**
	 * 
	 */
	public void reconnect(){
		 if (btService != null){
			 btService.stop();
			 btService.start();
		 }
	}
	/**
	 * 
	 */
	public void connect(){ //TODO: Discovery before connecting!!
		Log.d(Tags.LOG_TAG, "in connect method...");
		 if (btService != null) {
	    	// Only if the state is STATE_NONE, do we know that we haven't started already
	    	if (btService.getState() == BluetoothService.STATE_NONE) {
	    		// Start the Bluetooth chat services
	    		btService.start();
	    		btService.connect(null); //TODO!!!! NICHT NULL �BERGEBEN!!! HARDCODING IN BluetoothService RAUS NEHMEN!
	    	}
		 }
		
	}
	
	public int getConnectionState() {
		return btService.getState();
	}
 
	/**
	 * This method will initiate a sending of a Light-Frame via Bluetooth to the EBike.
	 * @param lightOn when true it will send a request to switch on the light. Switch off when false.
	 * @throws IOException
	 */
	public void sendLightFrame(boolean lightOn) throws IOException{
		
		header = abcpContainer.setABCPHeader(ABCPFieldSummary.TYPE_ID_LIGHTSIGNAL, (byte) 0x03);
		payload = abcpContainer.setLightFrame(lightOn);
		//TODO:
//		btService.write(abcpContainer.getBytes(header, payload));
		byte [] msg = new byte[1];
		if(lightOn) msg[0] = 'a';
		else msg[0] = 'b';
		btService.write(msg);
	}
	/**
	 * This method will decide what kind of ABCP-Frame has been received and will<br>
	 * call according to its decision the corresponding resolve methods in {@code ABCPProtocolContainer}.
	 */
	@Override
	public void onNewFrameReceived(byte[] frame, int frameLength) {
		switch(frame[0]){
		case ABCPFieldSummary.TYPE_ID_BIOSIGNAL:
			Log.d(Tags.LOG_TAG, "Biosignal Frame received...");
			this.setChanged();
			this.notifyObservers(abcpContainer.resolveBiosignalFrame(extractDataFromFrame(frame)));
			break;
		case ABCPFieldSummary.TYPE_ID_BMS: 
			Log.d(Tags.LOG_TAG, "BMS Frame received...");
			this.setChanged();
			this.notifyObservers(abcpContainer.resolveBMSFrame(extractDataFromFrame(frame)));
			break;
		case ABCPFieldSummary.TYPE_ID_LIGHTSIGNAL: 
			Log.d(Tags.LOG_TAG, "Lightsignal Frame received...");
			this.setChanged();
			this.notifyObservers(abcpContainer.resolveLightFrame(extractDataFromFrame(frame)));
			break;
		case ABCPFieldSummary.TYPE_ID_SPEED_METER: 
			Log.d(Tags.LOG_TAG, "SpeedMeter Frame received...");
			this.setChanged();
			this.notifyObservers(abcpContainer.resolveSpeedMeterFrame(extractDataFromFrame(frame)));
			break;
		default:
			Log.d(Tags.LOG_TAG, "Undefined Type ID received!");
		}
	}
	/**
	 * This method extracts the ABCP-Payload of an ABCP-Frame.
	 * @param frame the whole ABCP-Frame as byte array
	 * @return the byte array with only the ABCP-Payload.
	 */
	private byte [] extractDataFromFrame(byte [] frame){
		byte [] data = new byte[frame.length - ABCPFieldSummary.HEADER_LENGTH];
		for(int i = 0; i < data.length; i++){
			data[i] = frame[i+ABCPFieldSummary.HEADER_LENGTH];
		}
		return data;
	}
}
