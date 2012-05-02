package com.android.sixtopia.citypowerbike.gps;
import android.location.Location;

/**
 * 
 *	This class provides the GPS functionality for getting the Speed and other Features
 */
public class GPSLocation extends Location
{
  private static boolean bUseMetricUnits = true;
  
  public GPSLocation(Location location)
  {
    this(location, true);
  }

  public GPSLocation(Location location, boolean bUseMetricUnits)
  {
    super(location);
    GPSLocation.setbUseMetricUnits(bUseMetricUnits);
  }


  @Override
  public float distanceTo(Location dest)
  {
    float nDistance = super.distanceTo(dest);
    return nDistance;
  }

  @Override
  public float getAccuracy()
  {
    float nAccuracy = super.getAccuracy();
    return nAccuracy;
  }

  @Override
  public double getAltitude()
  {
    double nAltitude = super.getAltitude();
    return nAltitude;
  }

  @Override
  public float getSpeed()
  {
    float nSpeed = super.getSpeed();
    //in km/h
    nSpeed *= 3.6;
    return nSpeed;
  }

	public static void setbUseMetricUnits(boolean bUseMetricUnits) {
		GPSLocation.bUseMetricUnits = bUseMetricUnits;
	}
	
	public static boolean isbUseMetricUnits() {
		return bUseMetricUnits;
	}
}
