package com.android.sixtopia.citypowerbike.gui;

import com.android.sixtopia.citypowerbike.data.DataListener;
import com.android.sixtopia.citypowerbike.util.Tags;

import com.android.sixtopia.citypowerbike.gui.R;
import android.content.pm.ActivityInfo;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

/**
 * 
 *
 */
public class SpeedometerInfo extends Activity implements DataListener{
	
	private Initialize initialize;
	//Refreshing GUI through Handler to avoid Thread Exceptions
	private RefreshHandler mRedrawHandler = new RefreshHandler();
	
	private TextView txtCurrentSpeedInfo;
	private TextView txtAverageSpeed;
	private TextView txtMaximumSpeed;
	 
	private TextView txtCurrentAcceleration;
	 //TextView txtAverageAcceleration;
	private TextView txtMaximumAcceleration;
	
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
	    setContentView (R.layout.activity_speedometer);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    
	    this.txtCurrentSpeedInfo = (TextView) this.findViewById(R.id.txtAktuellGeschwindigkeit);
	    this.txtAverageSpeed = (TextView) this.findViewById(R.id.txtDurchschnittGeschwindigkeit);
	    this.txtMaximumSpeed = (TextView) this.findViewById(R.id.txtMaximumGeschwindigkeit);
	    this.txtCurrentAcceleration = (TextView) this.findViewById(R.id.txtAktuellBeschleunigung);
	    this.txtMaximumAcceleration = (TextView) this.findViewById(R.id.txtMaximumBeschleunigung);
	    
	    //initialize Data Handling
        initialize = Initialize.getInstance();
        initialize.getDataHandler().addDataListener(this);
        trace("initialize Data Handling in SpeedometerInfo");
	     	    
	}

	@Override
	public void onNewDataReceived(Display display) {	
		 trace("Daten f�r Tacho Info empfangen");
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
		 txtCurrentSpeedInfo.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCurrentSpeed()));
		 txtAverageSpeed.setText(String.valueOf(initialize.getDataHandler().getDisplay().getAverageSpeed())); 
		 txtMaximumSpeed.setText(String.valueOf(initialize.getDataHandler().getDisplay().getMaximumSpeed())); 
			 
		 txtCurrentAcceleration.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCurrentAcceleration()));
		 //txtAverageAcceleration.setText(String.valueOf initialize.getDataHandler().getDisplay().getAverageAccelerarion()));
		 txtMaximumAcceleration.setText(String.valueOf(initialize.getDataHandler().getDisplay().getMaximumAcceleration()));
		
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
	   trace("Pause Activity");
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
	   trace("Restart SpeedometerInfo");
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
	   trace("Resume SpeedometerInfo");
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
	   trace("Start SpeedometerInfo");
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
	   trace("Stop SpeedometerInfo");
	}
	
	/**
	 * Send a message to the debug log and display it using Toast.
	 */
	public void trace (String msg) 
	{
	    Log.d(Tags.LOG_TAG, msg);
	}
	
	/**
	 * 
	 * @author Joerg Sablottny
	 * 
	 * This class is needed to update the UI through Data Changes (Callbacks) and avoid Thread Exceptions 
	 */
	private class RefreshHandler extends Handler{
		
		 @Override  
		 public void handleMessage(Message msg) {  
			 SpeedometerInfo.this.updateUI();  
		 }  
		 
		 public void sleep(long delayMillis) {  
			 this.removeMessages(0);  
			 sendMessageDelayed(obtainMessage(0), delayMillis);  
		 }  
	};
	
}
