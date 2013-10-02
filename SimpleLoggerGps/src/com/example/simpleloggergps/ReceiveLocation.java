package com.example.simpleloggergps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

public class ReceiveLocation extends BroadcastReceiver{

	private static final String TAG = "BroadcastReceiver";
	private Handler handler;
	private TextView textView;
	private LayoutInflater inflater;
	private MainActivity mainActivity;
	private Location location;

	public ReceiveLocation(Handler handler, MainActivity mainActivity) {
		// TODO 自動生成されたコンストラクター・スタブ
		
		this.handler = handler;
		
		this.mainActivity = mainActivity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d(TAG, "onReceive----------------------");
	
		//textView = (TextView) mainActivity. findViewById(R.id.tvPloviderList);
		textView = (TextView)mainActivity .findViewById(R.id.tvNetLatitude);
		//setText( String.valueOf(location.getLatitude()) );// 緯度

		
		 String action = intent.getAction();
		 Log.d(TAG, action );
		 
		 location = (Location) intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
		 Log.d(TAG, location.getProvider() );
		 Log.d(TAG, String.valueOf( location.getLatitude() ));
		 Log.d(TAG, String.valueOf( location.getAltitude() ));
		 Log.d(TAG, String.valueOf( location.getSpeed() ));
		 

		 
		 handler.post(new Runnable() {
             @Override
             public void run() {
                 //Toast.makeText(context, "Toast from broadcast receiver", Toast.LENGTH_SHORT).show();
            	 textView.setText("123");
            	 
        		 if( location.getProvider().equals("network") ){
        			 printNetwork();
        		 }
            	 
             }
         });
	}

	protected void printNetwork() {
		// TODO 自動生成されたメソッド・スタブ
		((TextView)mainActivity.findViewById(R.id.tvNetTime)).setText( String.valueOf(location.getTime()) );// 時間
		((TextView)mainActivity.findViewById(R.id.tvNetTimeConvert)).setText( MainActivity.dataformat(location.getTime()) );// 時間　変換値
		
		((TextView)mainActivity.findViewById(R.id.tvNetLatitude)).setText( String.valueOf(location.getLatitude()) );// 緯度
		((TextView)mainActivity.findViewById(R.id.tvNetLongitude)).setText( String.valueOf(location.getLongitude()) );// 経度
		((TextView)mainActivity.findViewById(R.id.tvNetAltitude)).setText( String.valueOf(location.getAltitude()) );// 標高
		((TextView)mainActivity.findViewById(R.id.tvNetSpeed)).setText( String.valueOf(location.getSpeed()) );// 速度
		((TextView)mainActivity.findViewById(R.id.tvNetAccuracy)).setText( String.valueOf(location.getAccuracy()) );// 精度
		((TextView)mainActivity.findViewById(R.id.tvNetBearing)).setText( String.valueOf(location.getBearing()) );// ベアリング

	}

}
