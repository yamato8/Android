package com.example.simpleloggergps;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener, NmeaListener {

	private static final String TAG = "simpleloggergps";
	private static final String ACTION_LOCATION_UPDATE = "com.android.practice.ACTION_LOCATION_UPDATE";
	private LocationManager locationManager;
	//private Myclass myclass;
	private LocationUpdateReceiver receiv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d(TAG, "onCreate");
		//myclass =new Myclass( getApplicationContext() );
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// 　プロバイダの状態を確認する
		providerCheck(locationManager);
		
		
		Log.i(TAG, getPackageName() + "_preferences.xml");//
		
		
		// 設定ファイルがあるか確認する
		File f = new File("/data/data/" + getPackageName() +  "/shared_prefs/" + getPackageName() + "_preferences.xml"  );
		if( f.exists() ){
			Log.i("", "ファイルがある" );
		}else{
			Log.i("", "ファイルがない" );
			//ファイルがない場合の処理
			//noPrefFile();
			//　初期値を入力する
			
			Log.d("Myclass", "Myclass　の　setting　が呼ばれた");
			Criteria criteria = new Criteria();

			// Log.i("", "精度 " + criteria.getAccuracy());

			// 　設定ファイルに書き込み
			prefWriteString("accuracy", String.valueOf(criteria.getAccuracy()) ); // 位置精度
			prefWriteString("bearingAccuracy", String.valueOf(criteria.getBearingAccuracy() )); // ベアリングの精度
			prefWriteString("horizontalAccuracy", String.valueOf(criteria.getHorizontalAccuracy() )); // 水平精度(緯経度)
			prefWriteString("verticalAccuracy", String.valueOf(criteria.getVerticalAccuracy() )); // 垂直精度(高度)
			prefWriteString("powerRequirement", String.valueOf(criteria.getPowerRequirement()) ); // 電力要件
			prefWriteString("speedAccuracy", String.valueOf(criteria.getSpeedAccuracy())) ; // 速度情報
			prefWriteString("costAllowed", String.valueOf( criteria.isCostAllowed() )  );// 金銭コスト
			
	
			prefWriteString( "minTime", String.valueOf( 1 * 60 * 1000 ) );//更新間隔　時間(1分)
			
			prefWriteString("minDistance", String.valueOf( 10 ) );//更新間隔　距離(10m)
			
			prefWriteBoolean("providerSelect", false );// criteria を使うか
			
		}
		

		// IntentFilter filter = new IntentFilter( );
		 // filter.addAction(ACTION_LOCATION_UPDATE);
		 
		 // receiv = new LocationUpdateReceiver( MainActivity.this);
		  //registerReceiver(receiv, filter);
	}
	





	/*
	 * 設定ファイルの書き込み ブーレン
	 */
	void prefWriteBoolean(String key, boolean b) {
		// TODO 自動生成されたメソッド・スタブ
		String prefFileName = getPackageName() + "_preferences";//設定ファイル名
		SharedPreferences pref = getSharedPreferences(prefFileName, Context.MODE_PRIVATE);//
		
		Editor e = pref.edit();
		e.putBoolean(key, b);
		e.commit();
	}





	/*
	 * 設定ファイルの書き込み 数値設定
	 */
	void prefWriteString(String key, String string) {
		// TODO 自動生成されたメソッド・スタブ
		String prefFileName = getPackageName() + "_preferences";//設定ファイル名
		SharedPreferences pref = getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
		
		Editor e = pref.edit();
		e.putString(key, string);
		e.commit();
	}







	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		
		// 位置取得要件を読み込む　Criteria　
		Criteria criteria = new Criteria();
		//Criteria myCriteria = myclass.readPrefFile(criteria);
		
		// 更新間隔の設定値を調べる
		//long miniTime = myclass.timeSpan();
		//float minDistance = myclass.minDistance();
		
		//Log.d(TAG, "" + miniTime + "::" + minDistance );//
		
		//PendingIntentの生成
        //Intent nextIntent = new Intent(this, ReceiveLocation.class);
        //PendingIntent pi = PendingIntent.getBroadcast(this, 0x432f, nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        
		//locationManager.requestLocationUpdates( miniTime, minDistance, myCriteria, pi);
		
		// Intent intent = new Intent();
		 //intent.setAction(ACTION_LOCATION_UPDATE);
		 
		// PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 //locationManager.requestLocationUpdates(miniTime, minDistance, myCriteria,pendingIntent );
		
		//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		//locationManager.addNmeaListener(this);
		
	}


	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");

		if (locationManager != null) {
			locationManager.removeUpdates(this);
			locationManager.removeNmeaListener(this);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d(TAG, "onLocationChanged");
		Log.v("Provider", String.valueOf(location.getProvider()));

		// 状態表示
		if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
			printOutNetwork(location);// 状態表示　ネットワークの場合
		} else if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			Log.v("Time", "************GPS_PROVIDER********");
			printOutGps(location);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onNmeaReceived(long timestamp, String nmea) {
		// TODO 自動生成されたメソッド・スタブ

		// Log.d("", nmea);//
		nmea = nmea.trim();

		// 区切り文字で分解
		String[] nmeaArray = nmea.split(",");

		ArrayList<String> nmeaList = new ArrayList<String>(Arrays.asList(nmeaArray));

		// 　チェックサムを処理
		String checx = nmeaList.get(nmeaList.size() - 1);// 最後の配列

		// 最後の配列を分解
		if (checx.indexOf("*") == 0) {
			nmeaList.set(nmeaList.size() - 1, "");// 最後の配列にチェックサムを抜いた文字列にする
		} else {
			nmeaList.set(nmeaList.size() - 1, checx.substring(0, checx.indexOf("*")));
		}

		String checksum = checx.substring(checx.indexOf("*"));// チェックサム
		nmeaList.add(checksum);

		if (nmeaArray[0].equals("$GPGSV")) {
			printOutGPGSV(nmea, timestamp, nmeaList);
		} else if (nmeaArray[0].equals("$GPGGA")) {
			// Log.d("$GPGGA", nmea);
			TextView tvGPGGArawdata = (TextView) findViewById(R.id.tvGPGGArawdata);// 生のデータ
			tvGPGGArawdata.setText(nmea);

			printOutGPGGA(nmeaList);
		} else if (nmeaArray[0].equals("$GPRMC")) {
			// Log.d("$GPRMC", nmea);
			TextView tvGPGGArawdata = (TextView) findViewById(R.id.tvGPRMCrawdata);// 生のデータ
			tvGPGGArawdata.setText(nmea);

			printOutGPRMC(nmeaList);
		} else if (nmeaArray[0].equals("$GPVTG")) {
			// Log.d("$GPRMC", nmea);
			TextView tvGPGGArawdata = (TextView) findViewById(R.id.tvGPVTGrawData);// 生のデータ
			tvGPGGArawdata.setText(nmea);

			printOutGPVTG(nmeaList);
		} else if (nmeaArray[0].equals("$GPGSA")) {
			TextView tvGPGSArawdata = (TextView) findViewById(R.id.tvGPGSArawdata);// 生のデータ
			tvGPGSArawdata.setText(nmea);

			printOutGPGSA(nmeaList);
		}

	}

	// ---------------- 以下　自作メソッド ----------------
	
	/*
	 * 概要：プロバイダの状態をチェックする 引数：ロケーションマネージャー
	 */
	private void providerCheck(LocationManager locationManager2) {
		// TODO 自動生成されたメソッド・スタブ
		TextView tvGpsState = (TextView) findViewById(R.id.tvPloviderList);//

		String providerStatus = "";

		List<String> providers = locationManager.getAllProviders();
		for (String provider : providers) {
			if (locationManager.isProviderEnabled(provider)) {
				// Log.i("getAllProviders", provider + "　は有効");
				providerStatus += provider + " : 有効\n";
			} else {
				// Log.i("getAllProviders", provider + "　無効");
				providerStatus += provider + " : 無効";
				// state = "無効";
			}
		}
		tvGpsState.setText(providerStatus);
	}

	/*
	 * 概要：ネットワークプロバイダの値を表示する 引数：Location
	 */
	private void printOutNetwork(Location location) {
		// TODO 自動生成されたメソッド・スタブ
		TextView tvNwtTime = (TextView) findViewById(R.id.tvNetTime);// 時間
		TextView tvNetTimeConvert = (TextView) findViewById(R.id.tvNetTimeConvert);// 時間
		TextView tvNwtLatitude = (TextView) findViewById(R.id.tvNetLatitude);// 緯度
		TextView tvNwtLongitude = (TextView) findViewById(R.id.tvNetLongitude);// 経度
		TextView tvNetAltitude = (TextView) findViewById(R.id.tvNetAltitude);// 標高
		TextView tvNetSpeed = (TextView) findViewById(R.id.tvNetSpeed);// 速度
		TextView tvNetAccuracy = (TextView) findViewById(R.id.tvNetAccuracy);// 精度
		TextView tvNetBearing = (TextView) findViewById(R.id.tvNetBearing);// ベアリング
		// TextView tvNetBundle = (TextView)
		// findViewById(R.id.tvNetBundle);//バンドル

		tvNwtLatitude.setText(String.valueOf(location.getLatitude()));
		tvNwtLongitude.setText(String.valueOf(location.getLongitude()));
		tvNetAltitude.setText(String.valueOf(location.getAltitude()));
		tvNetSpeed.setText(String.valueOf(location.getSpeed()));
		tvNetAccuracy.setText(String.valueOf(location.getAccuracy()));
		tvNetBearing.setText(String.valueOf(location.getBearing()));
		// tvNetBundle.setText( String.valueOf(location.getExtras()) );

		// 時間変換
		String date = dataformat(location.getTime());

		tvNwtTime.setText(String.valueOf(location.getTime()));
		tvNetTimeConvert.setText(date);
	}

	/*
	 * 概要：ＧＰＳプロバイダの値を表示する 引数：Location
	 */
	private void printOutGps(Location location) {
		// TODO 自動生成されたメソッド・スタブ
		TextView tvGpsTime = (TextView) findViewById(R.id.tvGpsTime);// 時間
		TextView tvGpsTimeConvert = (TextView) findViewById(R.id.tvGpsTimeConvert);// 時間
		TextView tvGpsLatitude = (TextView) findViewById(R.id.tvGpsLatitude);// 緯度
		TextView tvGpsLongitude = (TextView) findViewById(R.id.tvGpsLongitude);// 経度
		TextView tvGpsAltitude = (TextView) findViewById(R.id.tvGpsAltitude);// 標高
		TextView tvGpsSpeed = (TextView) findViewById(R.id.tvGpsSpeed);// 速度
		TextView tvGpsAccuracy = (TextView) findViewById(R.id.tvGpsAccuracy);// 精度
		TextView tvGpsBearing = (TextView) findViewById(R.id.tvGpsBearing);// ベアリング
		// TextView tvGpsBundle = (TextView)
		// findViewById(R.id.tvGpsBundle);//バンドル

		tvGpsLatitude.setText(String.valueOf(location.getLatitude()));
		tvGpsLongitude.setText(String.valueOf(location.getLongitude()));
		tvGpsAltitude.setText(String.valueOf(location.getAltitude()));
		tvGpsSpeed.setText(String.valueOf(location.getSpeed()));
		tvGpsAccuracy.setText(String.valueOf(location.getAccuracy()));
		tvGpsBearing.setText(String.valueOf(location.getBearing()));
		// tvGpsBundle.setText( String.valueOf(location.getExtras()) );

		// 時間変換
		String date = dataformat(location.getTime());

		tvGpsTime.setText(String.valueOf(location.getTime()));
		tvGpsTimeConvert.setText(date);
	}

	// --------------------- NMEA ------------------------------
	/*
	 * 概要：NMEA GPGSV の表示
	 */
	private void printOutGPGSV(String nmea, long timestamp, ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ

		// 時間変換
		String date = dataformat(timestamp);

		// 時間の表示
		TextView tvGPGSVtimestamp = (TextView) findViewById(R.id.tvGPGSVtimestamp);// 時間
		tvGPGSVtimestamp.setText(timestamp + " = " + date);

		// データの数をチェックして足りなかったら空データを入れる
		nmeaList = dataCountChexk(nmeaList);
		// 生データの表示
		TextView tvGPGSVrawData = (TextView) findViewById(R.id.tvGPGSVrawData);// 生のデータ

		// データ表示用の空のラインレイアウト
		LinearLayout lineLayoutAddData = (LinearLayout) findViewById(R.id.llGPGSVdata);// データを表示するLineLayout

		// メッセージの順番を判定　最初のメッセージの場合
		if (nmeaList.get(2).endsWith("1")) {
			tvGPGSVrawData.setText(nmea);// 生データ表示

			// 子を削除
			lineLayoutAddData.removeAllViews();

			// Linelayout を作成
			LinearLayout linelayout = new LinearLayout(this);
			linelayout.setOrientation(LinearLayout.VERTICAL);

			for (int i = 1; i < nmeaList.size(); i++) {
				TextView textView = new TextView(this);
				textView.setText(nmeaList.get(i));

				linelayout.addView(textView);
			}

			lineLayoutAddData.addView(linelayout);
		} else {
			tvGPGSVrawData.append("\n" + nmea);

			// Linelayout を作成
			LinearLayout linelayout = new LinearLayout(this);
			linelayout.setOrientation(LinearLayout.VERTICAL);
			linelayout.setPadding(16, 0, 0, 0);

			for (int i = 1; i < nmeaList.size(); i++) {
				TextView textView = new TextView(this);
				textView.setText(nmeaList.get(i));

				linelayout.addView(textView);
			}

			lineLayoutAddData.addView(linelayout);
		}

	}

	/*
	 * 概要：NMEA GPGGA の表示
	 */
	private void printOutGPGGA(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ
		String time = null;

		TextView tvGPGGA01 = (TextView) findViewById(R.id.tvGPGGA01);
		TextView tvGPGGA02 = (TextView) findViewById(R.id.tvGPGGA02);
		TextView tvGPGGA03 = (TextView) findViewById(R.id.tvGPGGA03);
		TextView tvGPGGA04 = (TextView) findViewById(R.id.tvGPGGA04);
		TextView tvGPGGA05 = (TextView) findViewById(R.id.tvGPGGA05);
		TextView tvGPGGA06 = (TextView) findViewById(R.id.tvGPGGA06);
		TextView tvGPGGA07 = (TextView) findViewById(R.id.tvGPGGA07);
		TextView tvGPGGA08 = (TextView) findViewById(R.id.tvGPGGA08);
		TextView tvGPGGA09 = (TextView) findViewById(R.id.tvGPGGA09);
		TextView tvGPGGA10 = (TextView) findViewById(R.id.tvGPGGA10);
		TextView tvGPGGA11 = (TextView) findViewById(R.id.tvGPGGA11);
		TextView tvGPGGA12 = (TextView) findViewById(R.id.tvGPGGA12);
		TextView tvGPGGA13 = (TextView) findViewById(R.id.tvGPGGA13);
		TextView tvGPGGA14 = (TextView) findViewById(R.id.tvGPGGA14);
		TextView tvGPGGA15 = (TextView) findViewById(R.id.tvGPGGA15);

		// Log.d("$GPGGA", nmeaList.get(1));
		// final SimpleDateFormat formatter = new SimpleDateFormat("HHmmss.SS");
		if (!nmeaList.get(1).equals("")) {
			String hours = nmeaList.get(1).substring(0, 2);
			String minutes = nmeaList.get(1).substring(2, 4);
			String Seconds = nmeaList.get(1).substring(4);
			time = " = " + hours + "時" + minutes + "分" + Seconds + "秒";
		} else {
			time = "";
		}

		// 　緯度・経度方向
		String latitudeDirection = nmeaList.get(3) + "  [N:北緯,S:南緯]";
		String LatitudeDirection = nmeaList.get(5) + "  [E: 東経,W:西経]";

		tvGPGGA01.setText(nmeaList.get(1) + time);
		tvGPGGA02.setText(nmeaList.get(2));
		tvGPGGA03.setText(latitudeDirection);
		tvGPGGA04.setText(nmeaList.get(4));
		tvGPGGA05.setText(LatitudeDirection);
		tvGPGGA06.setText(nmeaList.get(6));
		tvGPGGA07.setText(nmeaList.get(7));
		tvGPGGA08.setText(nmeaList.get(8));
		tvGPGGA09.setText(nmeaList.get(9));
		tvGPGGA10.setText(nmeaList.get(10));
		tvGPGGA11.setText(nmeaList.get(11));
		tvGPGGA12.setText(nmeaList.get(12));
		tvGPGGA13.setText(nmeaList.get(13));
		tvGPGGA14.setText(nmeaList.get(14));
		tvGPGGA15.setText(nmeaList.get(15));

	}

	/*
	 * NMEA GPRMCの表示
	 */
	private void printOutGPRMC(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ
		TextView tvGPRMC01 = (TextView) findViewById(R.id.tvGPRMC01);
		TextView tvGPRMC02 = (TextView) findViewById(R.id.tvGPRMC02);// データ有効性
		TextView tvGPRMC03 = (TextView) findViewById(R.id.tvGPRMC03);
		TextView tvGPRMC04 = (TextView) findViewById(R.id.tvGPRMC04);
		TextView tvGPRMC05 = (TextView) findViewById(R.id.tvGPRMC05);
		TextView tvGPRMC06 = (TextView) findViewById(R.id.tvGPRMC06);
		TextView tvGPRMC07 = (TextView) findViewById(R.id.tvGPRMC07);
		TextView tvGPRMC08 = (TextView) findViewById(R.id.tvGPRMC08);
		TextView tvGPRMC09 = (TextView) findViewById(R.id.tvGPRMC09);// 日付
		TextView tvGPRMC10 = (TextView) findViewById(R.id.tvGPRMC10);
		TextView tvGPRMC11 = (TextView) findViewById(R.id.tvGPRMC11);
		TextView tvGPRMC12 = (TextView) findViewById(R.id.tvGPRMC12);

		String date = null;

		// 日付のフォーマット
		if (!nmeaList.get(9).equals("")) {
			String day = nmeaList.get(9).substring(0, 2);
			String month = nmeaList.get(9).substring(2, 4);
			String year = nmeaList.get(9).substring(4);
			date = " = " + year + "年" + month + "月" + day + "日";
		} else {
			date = "";
		}

		// データ有効性
		String dataStatus = nmeaList.get(2) + "  [V:警告,A:有効]";

		// 　緯度・経度方向
		String latitudeDirection = nmeaList.get(4) + "  [N:北緯,S:南緯]";
		String LatitudeDirection = nmeaList.get(6) + "  [E: 東経,W:西経]";

		tvGPRMC01.setText(nmeaList.get(1));
		tvGPRMC02.setText(dataStatus);
		tvGPRMC03.setText(nmeaList.get(3));
		tvGPRMC04.setText(latitudeDirection);
		tvGPRMC05.setText(nmeaList.get(5));
		tvGPRMC06.setText(LatitudeDirection);
		tvGPRMC07.setText(nmeaList.get(7));
		tvGPRMC08.setText(nmeaList.get(8));
		tvGPRMC09.setText(nmeaList.get(9) + date);
		tvGPRMC10.setText(nmeaList.get(10));
		tvGPRMC11.setText(nmeaList.get(11));
		tvGPRMC12.setText(nmeaList.get(12));
	}

	/*
	 * NMEA GPVTG の表示
	 */
	private void printOutGPVTG(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ
		TextView tvGPVTG01 = (TextView) findViewById(R.id.tvGPVTG01);
		TextView tvGPVTG02 = (TextView) findViewById(R.id.tvGPVTG02);
		TextView tvGPVTG03 = (TextView) findViewById(R.id.tvGPVTG03);
		TextView tvGPVTG04 = (TextView) findViewById(R.id.tvGPVTG04);
		TextView tvGPVTG05 = (TextView) findViewById(R.id.tvGPVTG05);
		TextView tvGPVTG06 = (TextView) findViewById(R.id.tvGPVTG06);
		TextView tvGPVTG07 = (TextView) findViewById(R.id.tvGPVTG07);
		TextView tvGPVTG08 = (TextView) findViewById(R.id.tvGPVTG08);
		TextView tvGPVTG09 = (TextView) findViewById(R.id.tvGPVTG09);
		TextView tvGPVTG10 = (TextView) findViewById(R.id.tvGPVTG10);

		// 方向基準　真北
		String trueTrack = nmeaList.get(2) + "  [T:真北]";
		String magneticTrack = nmeaList.get(4) + "  [M:磁北]";

		tvGPVTG01.setText(nmeaList.get(1));
		tvGPVTG02.setText(trueTrack);
		tvGPVTG03.setText(nmeaList.get(3));
		tvGPVTG04.setText(magneticTrack);
		tvGPVTG05.setText(nmeaList.get(5));
		tvGPVTG06.setText(nmeaList.get(6));
		tvGPVTG07.setText(nmeaList.get(7));
		tvGPVTG08.setText(nmeaList.get(8));
		tvGPVTG09.setText(nmeaList.get(9));
		tvGPVTG10.setText(nmeaList.get(10));
	}

	/*
	 * NMEA GPGSA の表示
	 */
	private void printOutGPGSA(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ
		TextView tvPGGSA1 = (TextView) findViewById(R.id.tvPGGSA1);
		TextView tvPGGSA2 = (TextView) findViewById(R.id.tvPGGSA2);
		TextView tvPGGSA3 = (TextView) findViewById(R.id.tvPGGSA3);
		TextView tvPGGSA4 = (TextView) findViewById(R.id.tvPGGSA4);
		TextView tvPGGSA5 = (TextView) findViewById(R.id.tvPGGSA5);
		TextView tvPGGSA6 = (TextView) findViewById(R.id.tvPGGSA6);
		TextView tvPGGSA7 = (TextView) findViewById(R.id.tvPGGSA7);
		TextView tvPGGSA8 = (TextView) findViewById(R.id.tvPGGSA8);
		TextView tvPGGSA9 = (TextView) findViewById(R.id.tvPGGSA9);
		TextView tvPGGSA10 = (TextView) findViewById(R.id.tvPGGSA10);
		TextView tvPGGSA11 = (TextView) findViewById(R.id.tvPGGSA11);
		TextView tvPGGSA12 = (TextView) findViewById(R.id.tvPGGSA12);
		TextView tvPGGSA13 = (TextView) findViewById(R.id.tvPGGSA13);
		TextView tvPGGSA14 = (TextView) findViewById(R.id.tvPGGSA14);
		TextView tvPGGSA15 = (TextView) findViewById(R.id.tvPGGSA15);
		TextView tvPGGSA16 = (TextView) findViewById(R.id.tvPGGSA16);
		TextView tvPGGSA17 = (TextView) findViewById(R.id.tvPGGSA17);
		TextView tvPGGSA18 = (TextView) findViewById(R.id.tvPGGSA18);

		// 方向基準　真北
		String mode = nmeaList.get(1) + "  [M:手動,A: 自動]";
		String type = nmeaList.get(2) + "  [1:存在しない,2:2D特定,3:3D特定]";

		tvPGGSA1.setText(mode);
		tvPGGSA2.setText(type);
		tvPGGSA3.setText(nmeaList.get(3));
		tvPGGSA4.setText(nmeaList.get(4));
		tvPGGSA5.setText(nmeaList.get(5));
		tvPGGSA6.setText(nmeaList.get(6));
		tvPGGSA7.setText(nmeaList.get(7));
		tvPGGSA8.setText(nmeaList.get(8));
		tvPGGSA9.setText(nmeaList.get(9));
		tvPGGSA10.setText(nmeaList.get(10));
		tvPGGSA11.setText(nmeaList.get(11));
		tvPGGSA12.setText(nmeaList.get(12));
		tvPGGSA13.setText(nmeaList.get(13));
		tvPGGSA14.setText(nmeaList.get(14));
		tvPGGSA15.setText(nmeaList.get(15));
		tvPGGSA16.setText(nmeaList.get(16));
		tvPGGSA17.setText(nmeaList.get(17));
		tvPGGSA18.setText(nmeaList.get(18));

	}

	/*
	 * UTC 時間を変換
	 */
	private String dataformat(long utcTime) {
		// TODO 自動生成されたメソッド・スタブ
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.JAPAN);

		return df.format(utcTime);
	}

	/*
	 * 概要：データの数をチェックして、足りない場合は追加する 引数：配列 戻値：配列
	 */
	private ArrayList<String> dataCountChexk(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ
		// Log.d("xxxxx",""+ nmeaList.size() );

		if (nmeaList.size() < 21) {
			String lastData = nmeaList.get(nmeaList.size() - 1);
			// Log.d("yyyyy",""+ lastData );

			// 最後の要素削除
			nmeaList.remove(nmeaList.size() - 1);

			int num = 21 - nmeaList.size();
			// Log.d("xxxxx","足りない数　　" + num );//12 一個目だけの場合
			for (int i = 0; i < num; i++) {
				// Log.d("yyyyy",""+i );
				nmeaList.add("");
			}

			nmeaList.set(nmeaList.size() - 1, lastData);
		}

		// Log.d("yyyyy",""+ nmeaList.size() );
		// Log.d("yyyyy",""+ nmeaList );

		return nmeaList;
	}

	/*
	 * ボタンがクリックされた時の処理
	 */
	public void ButtonOnClick(View v) {

		switch (v.getId()) {
		case R.id.buttonSetting:
			Log.i("buttonTest", "ボタン１が押された");
			Intent intent = new Intent(this, SettingPreferenceActivity.class);
			//Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.buttonLog:
			Log.i("buttonTest", "ボタン２が押された");
			//Intent intent2 = new Intent(this, Pref.class);
			//startActivity(intent2);
			break;

		}

	}
}
































