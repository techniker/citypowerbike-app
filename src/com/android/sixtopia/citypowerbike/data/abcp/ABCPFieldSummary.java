package com.android.sixtopia.citypowerbike.data.abcp;
/**
 * This class implements fields for specifying static data in the ABCP Protocol.
 *
 */
public abstract class ABCPFieldSummary {
	
	/**
	 * Start Byte of the ABCP Protocol, see specification.
	 */
	public static final byte START_BYTE = (byte) 0xAA;
	
	/**
	 * The length of the Header, see specification.
	 */
	public static final byte HEADER_LENGTH = 1;
	
	/**
	 * The Type ID of the Speed Meter Frame
	 */
	public static final byte TYPE_ID_SPEED_METER = 0x01;
	/**
	 * The Type ID of the BMS Frame
	 */
	public static final byte TYPE_ID_BMS = 0x02;
	/**
	 * The Type ID of the Biosignal Frame
	 */
	public static final byte TYPE_ID_BIOSIGNAL = 0x03;
	/**
	 * The Type ID of the Light Frame
	 */
	public static final byte TYPE_ID_LIGHTSIGNAL = 0x04;
	
	
}
