package com.example.simpleloggergps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ReceiveLocation extends BroadcastReceiver{

	private static final String TAG = "BroadcastReceiver";
	private Handler handler;
	private TextView textView;
	private LayoutInflater inflater;
	private MainActivity mainActivity;

	public ReceiveLocation(Handler handler, MainActivity aaa) {
		// TODO 自動生成されたコンストラクター・スタブ
		
		this.handler = handler;
		
		mainActivity = aaa;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d(TAG, "onReceive----------------------");
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View v = inflater.inflate(R.layout.activity_main, null);
		
		textView = (TextView) mainActivity. findViewById(R.id.tvPloviderList);
		
		 String action = intent.getAction();
		 Log.d(TAG, action );
		 
		 Location location = (Location) intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
		 Log.d(TAG, location.getProvider() );
		 Log.d(TAG, String.valueOf( location.getLatitude() ));
		 Log.d(TAG, String.valueOf( location.getAltitude() ));
		 Log.d(TAG, String.valueOf( location.getSpeed() ));
		 
		 handler.post(new Runnable() {
             @Override
             public void run() {
                 //Toast.makeText(context, "Toast from broadcast receiver", Toast.LENGTH_SHORT).show();
            	 textView.setText("123");
             }
         });
	}

}
