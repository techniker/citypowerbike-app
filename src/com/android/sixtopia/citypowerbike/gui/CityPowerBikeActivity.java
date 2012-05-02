package com.android.sixtopia.citypowerbike.gui;


import java.io.IOException;

//import javax.security.auth.PrivateCredentialPermission;

import com.android.sixtopia.citypowerbike.data.DataListener;
import com.android.sixtopia.citypowerbike.gps.GPSLocation;
import com.android.sixtopia.citypowerbike.gps.GpsListener;
import com.android.sixtopia.citypowerbike.util.Tags;
//import java.text.DecimalFormat;
import android.app.Activity;
import android.app.AlertDialog;
import com.android.sixtopia.citypowerbike.gui.R;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.os.Handler;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * This class serves as the main application and central unit for connecting Bluetooth, GPS and GUI
 */
public class CityPowerBikeActivity extends Activity implements GpsListener, DataListener{
		
	/* ##################################################################
	 * Global Attributes, Textfields, Views etc.
	 * ##################################################################
	 */
	
	//static identifiers for setting the light
	private static final boolean lightOn = true;
	private static final boolean lightOff = false;
	private static final int REQUEST_ENABLE_BT = 1;
	private boolean bluetoothSet = false;
	
	//Refreshing GUI through Handler to avoid Thread Exceptions
	 private RefreshHandler mRedrawHandler = new RefreshHandler();
	
	
	//Canvas controls the arrow on the speedometer - not finished yet!
	//private Canvas canvas = new Canvas();
	
	//Create a Display that serves as interface between GUI and Bluetooth Data
	private Initialize initialize;
	private BluetoothAdapter mBluetoothAdapter;
		
	//Image Views
	private ImageView batteryStatus; 
	//Text fields 
	//Initialize Text field for displaying speed
    private TextView txtCurrentSpeed; 
	private TextView txtGradiant; 
	private TextView txtDrivenKilometers;
	private TextView txtTrip; 
	//Light Image
	private ImageButton lichtAn;
	private ImageButton lichtAus;
//	private ImageButton lichtNeuAn;
//	private ImageButton lichtNeuAus;
    //SpeedometerView speedometerView; 
	
	
	
	/* ##################################################################
	 * MENU - An Menu Option for About Menu (Copyright, Version etc.)
	 * ##################################################################
	 */
	
	/**
	 * onCreateOptionsMenu - called when the menu is first created
	 */
	 @Override
	 /** Called when menu instantiated */
	 public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.menu, menu);
	    return true;
	 }
	 
	 /** Called when menu accessed */
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	     	case R.id.about_menu:
	                // show about dialog
	                about();
	                return true;
	        }
	        return false;
	    }
	
	private void about() {
		// show about dialogue.
        AlertDialog builder;
        try {
                builder = AboutDialogBuilder.create(this);
                builder.show();
        } catch (NameNotFoundException e) {
                e.printStackTrace();
        }
        
	}
	
	/**
	 * Method for updating the UI
	 */
	public void updateUI() {
		//wait for a second Thread 
		mRedrawHandler.sleep(1000);  
		int intBatteryStatus = (int) initialize.getDataHandler().getDisplay().getBatteryStatus();
//		boolean lightStatus = initialize.getDataHandler().getDisplay().isLightOn();
		
		
		txtCurrentSpeed.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCurrentSpeed()));
	
		txtGradiant.setText(String.valueOf(initialize.getDataHandler().getDisplay().getGradient())); 
		txtDrivenKilometers.setText(String.valueOf(initialize.getDataHandler().getDisplay().getDrivenKilometers())); 
		txtTrip.setText(String.valueOf(initialize.getDataHandler().getDisplay().getTrip()));
		txtCurrentSpeed.setText(String.valueOf(initialize.getDataHandler().getDisplay().getCurrentSpeed()));
		
		//light on or off?
//		if (lightStatus){
//			lichtNeuAn = (ImageButton)findViewById(R.id.btnLichtAn);
//			lichtNeuAn.setImageResource(R.drawable.btnlichtan);
//			lichtNeuAn.setId(100);
//		}
//		else if (!lightStatus){
//			lichtNeuAn.setId(100);
//			lichtNeuAus = (ImageButton)findViewById(100);
//			lichtNeuAus.setImageResource(R.drawable.btnlichtaus);
//			lichtNeuAus.setId(R.id.btnLichtAn);
//		}
		
		//Battery Status
		if((intBatteryStatus <= 100) && (intBatteryStatus > 80)){
			batteryStatus.setImageResource(R.drawable.img100percent);
			trace("Battery Image changed to 100 %!");
		}
		else if((intBatteryStatus <= 80) && (intBatteryStatus > 60)){
			batteryStatus.setImageResource(R.drawable.img80percent);
			trace("Battery Image changed to 80 %!");
		}
		else if((intBatteryStatus <= 60) && (intBatteryStatus > 40)){
			batteryStatus.setImageResource(R.drawable.img60percent);
			trace("Battery Image changed to 60 %!");
		}
		else if((intBatteryStatus <= 40) && (intBatteryStatus > 20)){
			batteryStatus.setImageResource(R.drawable.img40percent);
			trace("Battery Image changed to 40 %!");
		}
		else if((intBatteryStatus <= 20) && (intBatteryStatus > 10)){
			batteryStatus.setImageResource(R.drawable.img20percent);
			trace("Battery Image changed to 20 %!");	
		}
		else if((intBatteryStatus <= 10) && (intBatteryStatus > 0)){
			batteryStatus.setImageResource(R.drawable.imglowerthan10percent);
			trace("Battery Image changed to lower than 10 %!");
		}
		else if(intBatteryStatus == 0){
			
			batteryStatus.setImageResource(R.drawable.imgempty0percentreloadplease);
			trace("Battery Image changed to Battery empty!");
		}
		
	}
	
	/* ##################################################################
	 * MAIN - onCreate serves as the MAIN - First method to be accessed
	 * ##################################################################
	 */
	
	/**
	 * onCreate - called when the activity is first created.
	 *
	 * Called when the activity is first created. 
	 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
	 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().
	 *
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        this.batteryStatus = (ImageView) this.findViewById(R.id.imgBatterieStatus);
        this.txtCurrentSpeed = (TextView) this.findViewById(R.id.txtGeschwindigkeit);
        this.txtGradiant = (TextView) this.findViewById(R.id.txtSteigung);
        this.txtDrivenKilometers = (TextView) this.findViewById(R.id.txtGesamt);
        this.txtTrip = (TextView) this.findViewById(R.id.txtTrip);
        this.lichtAn = (ImageButton)findViewById(R.id.btnLichtAn); 
       
        //this.speedometerView = (SpeedometerView) findViewById(R.id.imgTachometer);
            
        
        //Activate Bluetooth once
        if (!bluetoothSet){
        	 mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
             if (mBluetoothAdapter == null) {
                 trace("Das Ger�t verf�gt �ber kein Bluetooth");
             }
             
             if (!mBluetoothAdapter.isEnabled()) {
                 Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                 startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                 //wait 7 seconds for the user turning on bluetooth
                 synchronized(this) {
                 	try {
     					wait(7000);
     				} catch (InterruptedException e) {
     					e.printStackTrace();
     				}
                 }
                 trace("Bluetooth eingeschaltet!");
             }
             
                          
             //Speedometer will be set up
             makeArrowTransparent();
             
             /*Speedometer speedometer = new Speedometer(this);
             trace("Speedometer Setup successfully");
             speedometer.onDraw(canvas);*/
             
     		//GPS
             setupGPS();
             
             bluetoothSet = true;
        }
       
        //initialize Data Handling
        initialize = Initialize.getInstance();
        initialize.getDataHandler().addDataListener(this);
        trace("initialize Data Handling in CityPowerBikeActivity");
        
    }
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		   if (resultCode == Activity.RESULT_OK) {
			   trace("Bluetooth ist aktiv");
		   } else {
		       trace("Bluetooth wurde nicht aktiviert");
		   }
	}
	
	

	/**
	 * onDestroy
	 * The final call you receive before your activity is destroyed. 
	 * This can happen either because the activity is finishing (someone called finish() on it, 
	 * or because the system is temporarily destroying this instance of the activity to save space. 
	 * You can distinguish between these two scenarios with the isFinishing() method.
	 *
	 */
	
	/*protected void onDestroy ()
	{
	   super.onDestroy ();
	   trace("Destroy CityPowerBikeActivity");
	   super.finish();
	   trace("finish application and clear RAM");
	   System.exit(0);
	   trace("System.exit()");
	  
	}*/
	
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
	   trace("Pause CitypowerbikeAPPActivity");
	   
	   if(isFinishing()){
		  trace("Activity gets destroyed.."); 
	   }
	   else {
		   trace("Activity not finished yet!");
	   }
	   
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
	   trace("Restart CityPowerBikeActivity");
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
	   trace("Resume CityPowerBikeActivity");
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
	   trace("Start CityPowerBikeActivity");
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
	   trace("Stop CityPowerBikeActivity");
	}
	
	
	/**
	 * makes the arrow of the speedometer transparent
	 */
	private void makeArrowTransparent() {
		ImageView arrow = (ImageView)findViewById(R.id.imgArrowSpeedometer);
		arrow.setAlpha(128); 
	}
	
	
	/* ##################################################################
	 * SET/GET Display Object for Communication with Bluetooth
	 * ##################################################################
	 */
	
	
	
	/* ##################################################################
	 * DEBUG Traces
	 * ##################################################################
	 */
	/**
	 * Send a message to the debug log and display it using Toast.
	 */
	public void trace (String msg) 
	{
	    Log.d(Tags.LOG_TAG, msg);
	}

	
	/* ##################################################################
	 * Click Events
	 * ##################################################################
	 */

	/**
	 * Handle the click of a Feature button.
	 * 
	 * @param v View
	 * @return void
	 */

	public void onClickFeature (View v)
	{
	    int id = v.getId ();
	    
	    switch (id) {
	    
	      case R.id.btnBatterieInfo :
	    	   trace("Battery Info button pressed");   	   
	    	  
	    	   final Intent batteryInfo = new Intent(this, BatteryInfo.class);
	    	   batteryInfo.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    	   startActivity (batteryInfo);
	           
	    	   break;
	    
	      case R.id.btnLichtAn :
	    	   //Licht anschalten und Button �ndern
	    	   trace("Turns light on");
	    	   
	    	   lichtAn = (ImageButton)findViewById(R.id.btnLichtAn);
	           lichtAn.setImageResource(R.drawable.btnlichtan);
	           lichtAn.setId(100);
	           
	           try {
	        	   this.initialize.getDataHandler().setLight(lightOn);
				} catch (IOException e) {
					Log.e(Tags.LOG_TAG, "Sending LightONFrame failed...", e);
				}
				
	           break;
	           
	      case 100 :
	    	   trace("Turn light off!");
	    	   lichtAus = (ImageButton)findViewById(100);
	    	   
	           lichtAus.setImageResource(R.drawable.btnlichtaus);
	           lichtAus.setId(R.id.btnLichtAn); 
	           
	           try {
	        	   this.initialize.getDataHandler().setLight(lightOff);
				} catch (IOException e) {
					 Log.e(Tags.LOG_TAG, "Sending LightFrame failed...", e);
				}
				
	           break;
	           
	      case R.id.btnTachoInfo :
	    	   trace("More detailed Speed Info");
	    	  
	    	   final Intent tachoInfo = new Intent(this, SpeedometerInfo.class);
	    	   tachoInfo.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    	   try{
	    		   startActivity(tachoInfo);   
	    	   }catch(Exception e){
	    		   e.printStackTrace();
	    	   }
	    	   
	           
	    	   break;
	    	   
	      case R.id.btnReset :
	    	   trace("Reset kilometers");
	           EditText kilometerStand; 
	           kilometerStand = (EditText) findViewById(R.id.txtTrip);
	    	   kilometerStand.setText(R.string.txtGesamtReset);
	           break;
	           
	      case R.id.imgBtnExit :	//finish application
	    	   trace("Exit");
	    	   super.finish();
	    	   trace("finish application and clear RAM");
	    	   System.exit(0);
	    	   
	      default: 
	    	   break;
	    }
	    
	}

	
	
	/* ###############################################################
	 * GPS METHODS
	 * ###############################################################
	 * @see android.app.Activity#finish()
	 */
	
	private void setupGPS(){
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        trace("GPS initialized");
	}
	
	public void onLocationChanged(Location location){
	    if (location != null)
	    {
	      GPSLocation myLocation = new GPSLocation(location, true);
	      this.updateSpeed(myLocation);
	      trace("GPS Speed updated");
	    }
	}

	private void updateSpeed(GPSLocation location){
	   // double nCurrentSpeed = 0;
	    //DecimalFormat df = new DecimalFormat("0.00");
	    
	    
	    if( location!=null )
	    {
	    	if (location.hasSpeed()){
	    		trace("Speed was recognized");
	    		//nCurrentSpeed = location.getSpeed() * 1.0;
	    		//speedometerView.setValue(nCurrentSpeed);
	    	}
	 
	    }
	    
	    //round to 2 decimal places
	    //String strCurrentSpeed = df.format(nCurrentSpeed);
	    //show speed
	    //txtCurrentSpeed.setText(strCurrentSpeed);  
	     
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
		
	}
		
	
	 
	
	/* ##################################################################
	 * Private Inner Classes
	 * ##################################################################
	 */

	/**
	 * Serves as Content Builder for the About Menu Dialog
	 */
	private static class AboutDialogBuilder {
	        // simple dialog builder.
	        
	        public static AlertDialog create( Context context ) throws NameNotFoundException {
	                
	        		// grab the package details
	                PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
	                
	                // stuff the info into useful strings
	                String about_title = String.format("About %s", context.getString(R.string.app_name));
	                String version_string = String.format("Version: %s", pInfo.versionName);
	                String about_text = context.getString(R.string.about_string);
	                                
	                final TextView about_view = new TextView(context);
	                
	                final SpannableString s = new SpannableString(about_text);
	                about_view.setPadding(3, 1, 3, 1);
	                about_view.setText(version_string + "\n" + s);

	                // build and return the dialog
	                return new AlertDialog.Builder(context).setTitle(about_title).setCancelable(true).setPositiveButton(
	                         context.getString(android.R.string.ok), null).setView(about_view).create();
	        }
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
			 CityPowerBikeActivity.this.updateUI();  
		 }  
		 
		 public void sleep(long delayMillis) {  
			 this.removeMessages(0);  
			 sendMessageDelayed(obtainMessage(0), delayMillis);  
		 }  
	};
	 
	
	 /**
	  * 
	  * @author J�rg Sablottny
	  * 
	  * Speedometer Class holds the arrow image of the Speedometer - rotate arrow 
	  *
	  */
	 /*private class Speedometer extends View {

		    public Speedometer(Context context) {
		    	super(context);
		    }
		    
		    private Bitmap bitmap;

		    @Override
			protected void onDraw(Canvas canvas) {
				
				
				bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.imgarrow);
				Paint paint = new Paint();
				canvas.save();
				canvas.rotate(180, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
				canvas.drawBitmap(bitmap, 0.f, 0.f, paint);
			
				canvas.restore();
			}

			
	 }*/

	/**
	 * Call Back Function 
	 */
	@Override
	public void onNewDataReceived(Display display) {
		trace("get Data for main Activity");
		mRedrawHandler.post(new Runnable() {
		    public void run()
		    {
		    	Message message = new Message();
		    	mRedrawHandler.handleMessage(message);
		    }
		});
		
	}

	
	 	
}