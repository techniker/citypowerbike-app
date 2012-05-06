package com.android.sixtopia.citypowerbike.data.abcp.bluetooth;
/**
 * This Interface must be implemented to receive complete byte-Frames from the {@code BluetoothStreamReader}. <br>
 * It contains a Callback-Method for the object which will implement this Interface.
 */
public interface ReceiveListener {
	/**
	 * This method will be called automatically when a new Frame in {@code BluetoothStreamReader} has been received.
	 * @param frame the complete ABCP-Frame.
	 * @see BluetoothStreamReader
	 */
	public void onNewFrameReceived(byte [] frame, int frameLength);
}
