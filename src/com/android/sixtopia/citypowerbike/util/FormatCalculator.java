package com.android.sixtopia.citypowerbike.util;

import android.util.Log;

/**
 * This class offers static methods for Data-Format conversion.
 *
 */
public class FormatCalculator {
	/**
	 * This method converts one byte in unsigned 8.0 format to an unsigned Integer.
	 * @param format8dot0 the byte in 8.0 format
	 * @return the unsigned Integer
	 */
	public static int convert_8dot0_to_UnsignedInt(byte format8dot0){
		int unsignedInt = (int) (0x00FF) & format8dot0;
		Log.d(Tags.LOG_TAG, "convert_8dot0_to_UnsignedInt: " + unsignedInt);
		return unsignedInt;
	}
	/**
	 * This method converts one byte in signed 4.4 format to a signed Double
	 * @param format4dot4 the byte in signed 4.4 format
	 * @return the signed Double
	 */
	public static double convert_4dot4_to_SignedDouble(byte format4dot4){
		double signedDouble = ((double)format4dot4) * Math.pow(2, (-4));
		Log.d(Tags.LOG_TAG, "convert_6dot2_to_SignedDouble: " + signedDouble);
		return signedDouble;
	}
	/**
	 * This method converts two bytes which belong together into an unsigned Integer
	 * @param msb the first 8 bit of the Integer
	 * @param lsb the last 8 bit of the Integer
	 * @return the 16 bit long Integer
	 */
	public static int convert_16dot0_to_UnsignedInt(byte msb, byte lsb){
		int unsignedInt = (int) (((0x00FF & msb) << 8) | (0x00FF & lsb));
		Log.d(Tags.LOG_TAG, "convert_16dot0_to_UnsignedInt: " + unsignedInt);
		return unsignedInt;
	}
	/**
	 * 
	 * @param format16dot0
	 * @return
	 */
	public static int [] convert_16dot0_to_UnsignedInt(byte [] format16dot0){
		Log.d(Tags.LOG_TAG, "Length of format16dot0: "+format16dot0.length);
		if((format16dot0.length % 2) == 0){
			int [] conversion = new int[format16dot0.length/2];
			
			for(int i = 0; i < format16dot0.length; i+=2){
				conversion[i/2] = (int) (((0x00FF & format16dot0[i]) << 8) | (0x00FF & format16dot0[i+1]));
				Log.d(Tags.LOG_TAG, (i/2)+". Element of Converion: "+conversion[i/2]);
			}
			return conversion;
		}
		else{
			throw new IllegalArgumentException("The array length of the array in the parameter list is odd! Expect an even number of elements.");
		}
	}
}
