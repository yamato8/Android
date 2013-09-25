package com.example.simpleloggergps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class LogService extends Service {

	public class LocationListener implements android.location.LocationListener{

		private Location mLastLocation;

		public LocationListener(String provider) {
			// TODO 自動生成されたコンストラクター・スタブ
			Log.e(TAG, "LocationListener " + provider);
	        mLastLocation = new Location(provider);
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO 自動生成されたメソッド・スタブ
			Log.e(TAG, "onLocationChanged: " + location);
			mLastLocation.set(location);

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO 自動生成されたメソッド・スタブ
			Log.e(TAG, "onProviderDisabled: " + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO 自動生成されたメソッド・スタブ
			Log.e(TAG, "onProviderEnabled: " + provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO 自動生成されたメソッド・スタブ
			Log.e(TAG, "onStatusChanged: " + provider);
		}

	}

	private static final String TAG = "LogService";
	private LocationManager mLocationManager = null;
	
	private static final int LOCATION_INTERVAL = 1000;
	private static final float LOCATION_DISTANCE = 10f;
	
	LocationListener[] mLocationListeners = new LocationListener[] {
	        new LocationListener(LocationManager.GPS_PROVIDER),
	        new LocationListener(LocationManager.NETWORK_PROVIDER)
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d(TAG, "onBind");
		return null;
	}

	public void onCreate() {
		Log.i(TAG, "onCreate");
		
		initializeLocationManager();
		
		mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                mLocationListeners[1]);
		
		mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                mLocationListeners[0]);
	}



	public void onStart(Intent intent, int StartId) {
		Log.i(TAG, "onStart");
		
		Intent i = new Intent();
		i.setClassName("com.example.simpleloggergps", "com.example.simpleloggergps.LogService");
		i.setAction("interval");
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		String intentAction = intent.getAction();
		if (intentAction.equals("start") || intentAction.equals("interval")) {
			//一定時間後に再度自身を呼び出す
			alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + (5000), pi);
		} else if (intentAction.equals("stop")) {
			alarmManager.cancel(pi);
		}
		
		//stopSelf();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);

		Log.i(TAG, "onStartCommand");
		return START_STICKY;
	}
	
	/*
	 * 概要：終了処理
	 */
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");

		for (int i = 0; i < mLocationListeners.length; i++) {
			try {
				mLocationManager.removeUpdates(mLocationListeners[i]);
			} catch (Exception ex) {
				Log.i(TAG, "fail to remove location listners, ignore", ex);
			}
		}
	}
	
	private void initializeLocationManager() {
		// TODO 自動生成されたメソッド・スタブ
		Log.e(TAG, "initializeLocationManager");
	    if (mLocationManager == null) {
	        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
	    }
	}

}
