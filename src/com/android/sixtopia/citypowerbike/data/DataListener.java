package com.android.sixtopia.citypowerbike.data;

import com.android.sixtopia.citypowerbike.gui.Display;

/**
 * This interface should be used to update all the data received from the cortex.
 */
public interface DataListener {

	/**
	 * Call Back function onDataChanged() should be overridden in every activity to update the data and display it in the corresponding textfields. 
	 * @param display
	 */
	public void onNewDataReceived(Display display);
	
}
