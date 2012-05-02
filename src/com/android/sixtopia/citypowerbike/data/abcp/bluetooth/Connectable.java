package com.android.sixtopia.citypowerbike.data.abcp.bluetooth;

import java.io.Closeable;
import java.io.IOException;

import android.os.Handler;
/**
 * Implementing this interface will add the functionality to a class to reconnect to a remote Bluetooth device after a connection reset<br>
 * and initiate a connection to a remote Bluetooth device.
 * @author Max Werner
 *
 */
public interface Connectable{
	/**
	 * This method will initiate a connection to a Bluetooth device with the specified UUID and Address.
	 * @param uuid the UUID which needs to be unique in the following format e.g.: 8ce255c0-200a-11e0-ac64-0800200c9a66
	 * @param btAddress the address of the Bluetooth device in the following format e.g.: 00:03:7A:84:3A:E8
	 * @param handler the Handler to send back messages to the UI.
	 * @throws IOException
	 */
	public void connect(String uuid, String btAddress, Handler handler) throws IOException;
	/**
	 * This method will reconnect to the remote Bluetooth device the {@code BluetoothConnection} was connected to after a connection reset occurred.<br>
	 * The method will automatically called after a connection reset.
	 * @throws IOException
	 */
	public void reconnect() throws IOException ;
}
