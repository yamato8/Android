package com.example.simpleloggergps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class NmeaService extends Service {

	public interface UpdateData {
		//public void update(int data);
		public void update(Message data);
	}

	private static final String TAG = "NmeaService";
	private IBinder mBinder = new LocalBinder();
	private MyHandler handler;
	private MyThread myThred;
	private LocationManager locationManager;
	private HashMap<String, ArrayList<String>> NMEAmap;
	private HashMap<String, String> GPGSVmap;
	private HashMap<String, String> NmeaRawData;

	public static class MyHandler extends Handler {

		//private TextView tv;
		private UpdateData updata;

		public MyHandler(UpdateData updata) {
			// TODO 自動生成されたコンストラクター・スタブ
			this.updata = updata;
		}

		@Override
		public void handleMessage(Message msg) {
			//Log.i(TAG, "handleMessage::" + msg);
			//tv.setText(String.valueOf(msg.arg1));//ここでも変更可能
			//Message msg = Message.obtain();

			//Bundle data = new Bundle();

			//data.putString("mystring", "123");
			//msg.setData(data);

			updata.update(msg);
		}

	}

	public class MyThread  extends Thread implements Runnable, LocationListener, NmeaListener {

		private int data;
		private boolean flag;
		
		
		public MyThread(){
			Log.i(TAG, "MyThread");
			flag = true;
			
			GPGSVmap = new HashMap<String, String>();
			NMEAmap = new HashMap<String, ArrayList<String>>();
			NmeaRawData = new HashMap<String, String>();
			
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
			locationManager.addNmeaListener(this);
		}
		
		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			while (flag) {
				//Log.i(TAG, "001::" + data);

				//GPGSVmap.put("aaa", "aaaTexe");
				
				data++;
				//Message msg =  new Message();
				Message msg = handler.obtainMessage();

				Bundle bundle = new Bundle();

				//msg.arg1 = data;
				//msg.obj = "test";
				
				bundle.putString("text", "any string");
				bundle.putInt("number", 123);
				bundle.putSerializable("NMEAmap", NMEAmap);
				bundle.putSerializable("NmeaRawData", NmeaRawData);//NmeaRawData
				msg.setData(bundle);

				handler.sendMessage(msg);
				
				//Log.i(TAG, "002::" + bundle.getString("text"));
				//Log.i(TAG, "003::" + bundle.getSerializable("NMEAmap"));

				try {
					//Log.i(TAG, "run::" + NMEAmap);

					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}

		}
		public void halt(){
			Log.i(TAG, "スレッドの停止処理ｘｘｘ");

			 flag = false;
		     //interrupt();

			if (locationManager != null) {
				locationManager.removeUpdates(this);
				locationManager.removeNmeaListener(this);
			}
		}

		@Override
		public void onNmeaReceived(long timestamp, String nmea) {
			// TODO 自動生成されたメソッド・スタブ
			//Log.i(TAG, nmea);
			makeNmea( nmea );
		}



		@Override
		public void onLocationChanged(Location location) {
			// TODO 自動生成されたメソッド・スタブ
			
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

	}

	public class LocalBinder extends Binder {


		public void getService(UpdateData updata) {
			// TODO 自動生成されたメソッド・スタブ
			//new Thread(new MyThread()).start();
			myThred = new MyThread();
			myThred.start();

			handler = new MyHandler(updata);
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		Log.i(TAG, "onBind");//起動時に呼ばれる

		return mBinder;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

	}
	
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
	
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		
		myThred.halt();
	}
	
	// --------------------------- 自作メソッド ------------------------------------
	private void makeNmea(String nmea) {
		// TODO 自動生成されたメソッド・スタブ
		//Log.i("", nmea);//
		nmea = nmea.trim();

		// 区切り文字で分解
		String[] nmeaArray = nmea.split(",");

		ArrayList<String> nmeaList = new ArrayList<String>(
				Arrays.asList(nmeaArray));

		// チェックサムを処理
		String checx = nmeaList.get(nmeaList.size() - 1);// 最後の配列

		// 最後の配列を分解
		if (checx.indexOf("*") == 0) {
			nmeaList.set(nmeaList.size() - 1, "");// 最後の配列にチェックサムを抜いた文字列にする
		} else {
			nmeaList.set(nmeaList.size() - 1,
					checx.substring(0, checx.indexOf("*")));
		}

		String checksum = checx.substring(checx.indexOf("*"));// チェックサム
		nmeaList.add(checksum);

		if (nmeaArray[0].equals("$GPGSV")) {

		} else if (nmeaArray[0].equals("$GPGGA")) {
			NmeaRawData.put("GPGGA", nmea);
			NMEAmap.put("GPGGA", nmeaList);
		}
		else if (nmeaArray[0].equals("$GPRMC")) {
			//tvGPGGArawdata.setText(nmea);

			//printOutGPRMC(nmeaList);
			NMEAmap.put("GPRMC", nmeaList);
		} 
	}
}

































