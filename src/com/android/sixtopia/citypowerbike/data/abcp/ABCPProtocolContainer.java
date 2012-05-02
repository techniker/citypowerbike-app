package com.android.sixtopia.citypowerbike.data.abcp;

import java.util.ArrayList;

import com.android.sixtopia.citypowerbike.data.*;
import com.android.sixtopia.citypowerbike.util.FormatCalculator;
import com.android.sixtopia.citypowerbike.util.Tags;

import android.util.Log;
/**
 * This class implements methods to extract data from a received frame and constructs objects out of them.
 * It is implemented according to the specification of the ABCP Protocol.
 * Developers can also construct frames.
 *
 */
public class ABCPProtocolContainer{
	
	private ArrayList<Byte> abcpHeader = new ArrayList<Byte>();
	private ArrayList<Byte> abcpPayload = new ArrayList<Byte>();
	
	private DataBiosignal dataBiosignal;
	private DataBMS dataBMS;
	private DataSpeedMeter dataSpeedMeter;
	private DataLight dataLight;
	
	//Light Frame
	private final byte lightON = 0x02;
	private final byte lightOFF = 0x00;
	
	/**
	 * This constructs a new Instance of a ABCPProtocolContainer with a ABCP-Header with the specified Type ID and Length.
	 * @param TYPE_ID the Type ID of the Frame to construct
	 * @param explicitLength the explicit length of the frame to construct
	 */
	public ABCPProtocolContainer(byte TYPE_ID, byte explicitLength){
		abcpHeader.add(ABCPFieldSummary.START_BYTE);
		abcpHeader.add(explicitLength);
		abcpHeader.add(TYPE_ID);
	}
	/**
	 * This constructs a new Instance of a ABCPProtocolContainer without constructing any ABCP-Header information.
	 */
	public ABCPProtocolContainer(){
	}
	/**
	 * This method constructs a ABCP-Header as specified in the ABCP-Protocol specification.
	 * @param TYPE_ID the Type ID of the Frame to construct
	 * @param explicitLength the explicit length of the frame to construct
	 * @return the ABCP-Header in form of a {@code ArrayList} in the specified order.
	 */
	public ArrayList<Byte> setABCPHeader(byte TYPE_ID, byte explicitLength){
		abcpHeader.clear();
		abcpHeader.add(ABCPFieldSummary.START_BYTE);
		abcpHeader.add(explicitLength);
		abcpHeader.add(TYPE_ID);
		return abcpHeader;
	}
	/**
	 * Resolves a received Biosignal-Frame according to the ABCP-Protocol specification. 
	 * @param biosignalData the byte array which contains <b>only</b> the payload of the Frame.
	 * @return the Frame represented by an {@code DataBiosignal} object.
	 * @see DataBiosignal
	 */
	public DataBiosignal resolveBiosignalFrame(byte [] biosignalData){
		Log.d(Tags.LOG_TAG, "Resolves Biosignal Frame...");
		dataBiosignal = new DataBiosignal();
		if((biosignalData[0] & 0x01) == 1)
			dataBiosignal.setLive(true);
		else
			dataBiosignal.setLive(false);
		dataBiosignal.setPuls(FormatCalculator.convert_8dot0_to_UnsignedInt(biosignalData[1]));
		Log.d(Tags.LOG_TAG, "Received Puls: "+dataBiosignal.getPuls());
		return dataBiosignal;		
	}
	/**
	 * Resolves a received BMS-Frame according to the ABCP-Protocol specification. 
	 * @param bmsData the byte array which contains <b>only</b> the payload of the Frame.
	 * @return the Frame represented by an {@code DataBMS} object.
	 * @see DataBMS
	 */
	public DataBMS resolveBMSFrame(byte [] bmsData){
		Log.d(Tags.LOG_TAG, "Resolves BMS Frame...");
		dataBMS = new DataBMS();
		if((bmsData[0] & 16) == 16) dataBMS.setCharging(true);
		else dataBMS.setCharging(false);
		if((bmsData[0] & 8) == 8) dataBMS.setRelay3Set(true);
		else dataBMS.setRelay3Set(false);
		if((bmsData[0] & 4) == 4) dataBMS.setRelay2Set(true);
		else dataBMS.setRelay2Set(false);
		if((bmsData[0] & 2) == 2) dataBMS.setRelay1Set(true);
		else dataBMS.setRelay1Set(false);
		if((bmsData[0] & 1) == 1) dataBMS.setLive(true);
		else dataBMS.setLive(false);
		dataBMS.setCellVoltage(FormatCalculator.convert_16dot0_to_UnsignedInt(extractBytes(bmsData, 1, 24)));
		dataBMS.setEntireCurrent(FormatCalculator.convert_16dot0_to_UnsignedInt(bmsData[25], bmsData[26]));
		dataBMS.setChargingStatus(FormatCalculator.convert_8dot0_to_UnsignedInt(bmsData[27]));
		dataBMS.setTemperature(bmsData[28]);
		return dataBMS;
	}
	/**
	 * Resolves a received Speed-Meter-Frame according to the ABCP-Protocol specification. 
	 * @param speedMeterData the byte array which contains <b>only</b> the payload of the Frame.
	 * @return the Frame represented by an {@code DataSpeedMeter} object.
	 * @see DataSpeedMeter
	 */
	public DataSpeedMeter resolveSpeedMeterFrame(byte [] speedMeterData){
		Log.d(Tags.LOG_TAG, "Resolves Speed Meter Frame...");
		dataSpeedMeter = new DataSpeedMeter();
		if((speedMeterData[0] & 0x01) == 1)
			dataSpeedMeter.setLive(true);
		else
			dataSpeedMeter.setLive(false);
		dataSpeedMeter.setCurrentSpeed(FormatCalculator.convert_8dot0_to_UnsignedInt(speedMeterData[1]));
		dataSpeedMeter.setMaximumSpeed(FormatCalculator.convert_8dot0_to_UnsignedInt(speedMeterData[2]));
		dataSpeedMeter.setAverageSpeed(FormatCalculator.convert_8dot0_to_UnsignedInt(speedMeterData[3]));
		dataSpeedMeter.setMaximumVelocity((float)FormatCalculator.convert_4dot4_to_SignedDouble(speedMeterData[4]));
		dataSpeedMeter.setCurrentVelocity((float)FormatCalculator.convert_4dot4_to_SignedDouble(speedMeterData[5]));
		dataSpeedMeter.setTrack((float)(FormatCalculator.convert_16dot0_to_UnsignedInt(speedMeterData[6], speedMeterData[7])/100));
		dataSpeedMeter.setEntireDistance(FormatCalculator.convert_16dot0_to_UnsignedInt(speedMeterData[8], speedMeterData[9]));
		return dataSpeedMeter;
	}
	/**
	 * Constructs a Light-Frame which will be send to the EBike via Bluetooth to switch the light on or off.
	 * @param LightON the requested status of the Light, when true switch light on.
	 * @return returns the Light-Frame represented by an {@code ArrayList}
	 */
	public ArrayList<Byte> setLightFrame(boolean LightON){
		abcpPayload.clear();
		if(LightON)
			abcpPayload.add(lightON);
		else
			abcpPayload.add(lightOFF);
		return abcpPayload;
	}
	/**
	 * Resolves a received Light-Frame according to the ABCP-Protocol specification. 
	 * @param lightData the byte array which contains <b>only</b> the payload of the Frame.
	 * @return the Frame represented by an {@code DataLight} object.
	 * @see DataLight
	 */
	public DataLight resolveLightFrame(byte [] lightData){
		Log.d(Tags.LOG_TAG, "Resolves Light Frame...");
		dataLight = new DataLight();
		if((lightData[0] & 0x01) == 1)
			dataLight.setLive(true);
		else
			dataLight.setLive(false);
		if((lightData[0] & 0x02) == 2)
			dataLight.setLightOn(true);
		else
			dataLight.setLightOn(false);
		if(dataLight.isLightOn())
			Log.d(Tags.LOG_TAG, "Light on");
		else
			Log.d(Tags.LOG_TAG, "Light off");
		return dataLight;
	}
	/**
	 * This method extracts bytes form an array with a specified offset.
	 * @param data the data array you want to extract bytes from
	 * @param offset the offset from where the data should be extracted.
	 * @return the extracted bytes as byte array.
	 */
	private byte [] extractBytes(byte [] data, int offset, int length){
		byte [] extract = new byte[length];
		for(int i = 0; i < length; i++){
			extract[i] = data[i + offset];
		}
		return extract;
	}
	/**
	 * This method will construct a byte array from two {@code ArrayList} object. 1st the ABCP-Header and 2nd the ABCP-Payload.
	 * @param abcpHeader the ABCP-Header represented by an {@code ArrayList}.
	 * @param abcpPayload the ABCP-Payload represented by an {@code ArrayList}.
	 * @return the byte array which can be send via Bluetooth to the EBike.
	 */
	public byte [] getBytes(ArrayList<Byte> abcpHeader, ArrayList<Byte> abcpPayload){
		Log.d(Tags.LOG_TAG, "getBytes Method in ABCPProtocolControler...");
		byte [] frame = new byte [abcpHeader.size() + abcpPayload.size()];
		Byte [] header = (Byte[]) abcpHeader.toArray();
		Byte [] payload = (Byte[]) abcpPayload.toArray();
		for(int i = 0; i < header.length; i++){
			frame[i] = header[i].byteValue();
		}
		for(int i = header.length-1; i < frame.length; i++){
			frame[i] = payload[i-(header.length-1)].byteValue();
		}
		return frame;
	}
}
