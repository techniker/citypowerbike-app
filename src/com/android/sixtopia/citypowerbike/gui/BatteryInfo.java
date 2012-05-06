package com.android.sixtopia.citypowerbike.gui;

import com.android.sixtopia.citypowerbike.data.DataListener;
import com.android.sixtopia.citypowerbike.util.Tags;

import android.app.Activity;
import com.android.sixtopia.citypowerbike.gui.R;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;


public class BatteryInfo extends Activity implements DataListener{
	
	private Initialize initialize;
	//Refreshing GUI through Handler to avoid Thread Exceptions
	private RefreshHandler mRedrawHandler = new RefreshHandler();
	
	private TextView txtChargingState;
	private TextView txtTemperatur;
	private TextView txtElectricityValue;
	private TextView txtSchuetze1; 
	private TextView txtSchuetze2; 
	private TextView txtSchuetze3; 
	private TextView txtCell1; 
	private TextView txtCell2;
	private TextView txtCell3; 
	private TextView txtCell4; 
	private TextView txtCell5; 
	private TextView txtCell6; 
	private TextView txtCell7; 
	private TextView txtCell8; 
	private TextView txtCell9; 
	private TextView txtCell10; 
	private TextView txtCell11; 
	private TextView txtCell12; 
	
	/**
	 * onCreate
	 *
	 * Called when the activity is first created. 
	 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
	 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 *
	 * @param savedInstanceState Bundle
	 */

	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView (R.layout.activity_battery);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    
	    this.txtElectricityValue = (TextView) this.findViewById(R.id.txtStromstaerkeGesamt);
	    this.txtTemperatur = (TextView) this.findViewById(R.id.txtTemperatur);
	    this.txtChargingState = (TextView)this.findViewById(R.id.txtBatterieGesamt);
	    this.txtSchuetze1 = (TextView) this.findViewById(R.id.txtSchuetze1);
	    this.txtSchuetze2 = (TextView) this.findViewById(R.id.txtSchuetze2);
	    this.txtSchuetze3 = (TextView) this.findViewById(R.id.txtSchuetze3);
	    this.txtCell1 = (TextView) this.findViewById(R.id.txtZelle1);
	    this.txtCell2 = (TextView) this.findViewById(R.id.txtZelle2);
	    this.txtCell3 = (TextView) this.findViewById(R.id.txtZelle3);
	    this.txtCell4 = (TextView) this.findViewById(R.id.txtZelle4);
	    this.txtCell5 = (TextView) this.findViewById(R.id.txtZelle5);
	    this.txtCell6 = (TextView) this.findViewById(R.id.txtZelle6);
	    this.txtCell7 = (TextView) this.findViewById(R.id.txtZelle7);
	    this.txtCell8 = (TextView) this.findViewById(R.id.txtZelle8);
	    this.txtCell9 = (TextView) this.findViewById(R.id.txtZelle9);
	    this.txtCell10 = (TextView) this.findViewById(R.id.txtZelle10);
	    this.txtCell11 = (TextView) this.findViewById(R.id.txtZelle11);
	    this.txtCell12 = (TextView) this.findViewById(R.id.txtZelle12);
	    
	    //initialize Data Handling
        initialize = Initialize.getInstance();
        initialize.getDataHandler().addDataListener(this);
        trace("initialize Data Handling in BatterInfo");
	   
	}

	@Override
	public void onNewDataReceived(Display display) {
		trace("Daten f�r Batterie Info empfangen");
		mRedrawHandler.post(new Runnable() {
		    public void run()
		    {
		    	Message message = new Message();
		    	mRedrawHandler.handleMessage(message);
		    }
		});
	}
	
	public void updateUI() {
			//wait for a second Thread 
			 mRedrawHandler.sleep(1000);  
			 
			txtElectricityValue.setText(String.valueOf(initialize.getDataHandler().getDisplay().getElectricityValue())); 
			txtChargingState.setText(String.valueOf(initialize.getDataHandler().getDisplay().getBatteryStatus()));
			txtTemperatur.setText(String.valueOf(initialize.getDataHandler().getDisplay().getTemperature()));
			
			if (initialize.getDataHandler().getDisplay().getPositionOfSchuetze()[0]){
				txtSchuetze1.setText("1");
			}
			else if (!(initialize.getDataHandler().getDisplay().getPositionOfSchuetze()[0])){
				txtSchuetze1.setText("0");
			}
			
			if (initialize.getDataHandler().getDisplay().getPositionOfSchuetze()[1]){
				txtSchuetze2.setText("1");
			}
			else if (!(initialize.getDataHandler().getDisplay().getPositionOfSchuetze()[1])){
				txtSchuetze2.setText("0");
			}
			
			if (initialize.getDataHandler().getDisplay().getPositionOfSchuetze()[2]){
				txtSchuetze3.setText("1");
			}
			else if (!(initialize.getDataHandler().getDisplay().getPositionOfSchuetze()[2])){
				txtSchuetze3.setText("0");
			}
			
			txtCell1.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[0]));
			txtCell2.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[1])); 
			txtCell3.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[2])); 
			txtCell4.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[3])); 
			txtCell5.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[4])); 
			txtCell6.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[5])); 
			txtCell7.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[6])); 
			txtCell8.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[7])); 
			txtCell9.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[8])); 
			txtCell10.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[9])); 
			txtCell11.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[10])); 
			txtCell12.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCellVoltages()[11])); 
		
	}

	/**
	 * onPause
	 * Called when the system is about to start resuming a previous activity. 
	 * This is typically used to commit unsaved changes to persistent data, stop animations 
	 * and other things that may be consuming CPU, etc. 
	 * Implementations of this method must be very quick because the next activity will not be resumed 
	 * until this method returns.
	 * Followed by either onResume() if the activity returns back to the front, 
	 * or onStop() if it becomes invisible to the user.
	 *
	 */
	
	protected void onPause ()
	{
	   super.onPause ();
	   trace("Pause BatteryInfo");
	}
	
	/**
	 * onRestart
	 * Called after your activity has been stopped, prior to it being started again.
	 * Always followed by onStart().
	 *
	 */
	
	protected void onRestart ()
	{
	   super.onRestart ();
	   trace("Restart BatteryInfo");
	}
	
	/**
	 * onResume
	 * Called when the activity will start interacting with the user. 
	 * At this point your activity is at the top of the activity stack, with user input going to it.
	 * Always followed by onPause().
	 *
	 */
	
	protected void onResume ()
	{
	   super.onResume ();
	   trace("Resume BatteryInfo");
	}
	
	/**
	 * onStart
	 * Called when the activity is becoming visible to the user.
	 * Followed by onResume() if the activity comes to the foreground, or onStop() if it becomes hidden.
	 *
	 */
	
	protected void onStart ()
	{
		 
	   super.onStart ();
	   trace("Start BatteryInfo");
	}
	
	/**
	 * onStop
	 * Called when the activity is no longer visible to the user
	 * because another activity has been resumed and is covering this one. 
	 * This may happen either because a new activity is being started, an existing one 
	 * is being brought in front of this one, or this one is being destroyed.
	 *
	 * Followed by either onRestart() if this activity is coming back to interact with the user, 
	 * or onDestroy() if this activity is going away.
	 */
	
	protected void onStop ()
	{
	   super.onStop ();
	   trace("Stop BatteryInfo");
	}
	
	/**
	 * Send a message to the debug log and display it using Toast.
	 */
	public void trace (String msg) 
	{
	    Log.d(Tags.LOG_TAG, msg);
	}

	

	 // This class is needed to update the UI through Data Changes (Callbacks) and avoid Thread Exceptions 

	private class RefreshHandler extends Handler{
		
		 @Override  
		 public void handleMessage(Message msg) {  
			 BatteryInfo.this.updateUI();  
		 }  
		 
		 public void sleep(long delayMillis) {  
			 this.removeMessages(0);  
			 sendMessageDelayed(obtainMessage(0), delayMillis);  
		 }  
	};
	
	    
}
