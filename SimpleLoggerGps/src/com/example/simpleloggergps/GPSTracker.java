package com.example.simpleloggergps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener, NmeaListener {
	
	 private static final long MIN_TIME_BW_UPDATES = 1000;
	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
	private Context mContext;
	private LocationManager locationManager;
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	private boolean canGetLocation;
	private Location location;
	private double latitude;
	private double longitude;

	public GPSTracker(Context context) {
	        this.mContext = context;
	        getLocation();
	        Log.d("------------", "get Location");
	    }

	public Location getLocation() {
		// TODO 自動生成されたメソッド・スタブ
		Log.d("------------", "get Location");

		locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		locationManager.addNmeaListener(this);//
		return location;

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO 自動生成されたメソッド・スタブ
        Log.d("-----------", "onLocationChanged");

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public void getLatitude() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void getLongitude() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		// TODO 自動生成されたメソッド・スタブ
        Log.d("-----------", nmea );

	}

	/*
	 * 終了処理
	 */
	public void stopUsingGPS() {
		// TODO 自動生成されたメソッド・スタブ
		if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
            locationManager.removeNmeaListener(GPSTracker.this);
        }
	}

}
