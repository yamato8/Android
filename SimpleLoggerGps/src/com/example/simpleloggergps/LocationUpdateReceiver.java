package com.example.simpleloggergps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class LocationUpdateReceiver extends BroadcastReceiver {

	private static final String TAG = "LocationUpdateReceiver";
	private MainActivity activity;

	public LocationUpdateReceiver(MainActivity mainActivity) {
		// TODO 自動生成されたコンストラクター・スタブ
		
		activity = mainActivity;
				
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d(TAG, "onReceive");
		
		Bundle b = intent.getExtras();
	    Location loc = (Location)b.get(android.location.LocationManager.KEY_LOCATION_CHANGED);
		Log.d(TAG, "onReceive getProvider" + loc.getProvider() );
		
		if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
			Log.d(TAG, "プロバイダチェック" );
			
			if (!intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, true)) {
				//Toast.makeText(context, "Provider enabled", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Provider enabled");
			} else {
				//Toast.makeText(context, "Provider enabled", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Provider enabledxxxxxxxxx");//
			}
		}
		
		if (intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
			Log.d(TAG, "KEY_LOCATION_CHANGED");

			LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

			Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			String message = "Location\n" + "Longitude：" + location.getLongitude() + "\n" + "Latitude：" + location.getLatitude();

			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			Log.d("ReceiveLocation", message);
			
			final TextView textView = (TextView) activity.findViewById(R.id.tvPloviderList);
			textView.setText( "緯度" );
			

		}
	}

}
