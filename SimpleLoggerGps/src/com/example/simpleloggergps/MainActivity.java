package com.example.simpleloggergps;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.simpleloggergps.NmeaService.LocalBinder;
import com.example.simpleloggergps.NmeaService.UpdateData;

public class MainActivity extends Activity implements LocationListener,
		NmeaListener {

	public static class SampleHandler extends Handler {

		private MainActivity con;

		public SampleHandler(MainActivity mainActivity) {
			// TODO 自動生成されたコンストラクター・スタブ
			this.con = mainActivity;
		}
		 @Override
		  public void handleMessage(Message msg) {
		  
			 Bundle bundle = msg.getData();
		    
			 if( bundle != null){
				  TextView textView = (TextView) con.findViewById(R.id.tvPloviderList);
				  textView.setText( bundle.getString("mystring") );
				  
				  //Serializable Hash_Map = bundle.getSerializable("GPGSVmap");
			 }
		   
		    
		    // Serializable Hash_Map = bundle.getSerializable("GPGSVmap");
		   //Log.d(TAG, ""+ Hash_Map.toString() );

		  }
	}

	private static final String TAG = "simpleloggergps";
	private static final String ACTION_LOCATION_UPDATE = "com.android.practice.ACTION_LOCATION_UPDATE";
	private LocationManager locationManager;
	// private Myclass myclass;
	private ReceiveLocation receiv;
	private Context contex;
	private int mode;
	private LocationManager locationManagerNmea;
	private LocalBinder binder;
	private TextView tv;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR);//アクションパー
		
		setContentView(R.layout.activity_main);

		//Log.d(TAG, "onCreate");
		// myclass =new Myclass( getApplicationContext() );
		
		contex = getApplicationContext();

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//
		
		locationManagerNmea =  (LocationManager) getSystemService(LOCATION_SERVICE);//

		String prefFileName = getApplicationContext().getFilesDir().getParentFile() + "/shared_prefs/" + getPackageName() + "_preferences.xml";// 設定ファイル
		
		// 設定ファイルがあるか確認する
		File prefFile = new File( prefFileName );
		if (prefFile.exists()) {
			//Log.i("", "ファイルがある");
		} else {
			//Log.i("", "ファイルがない");
			// ファイルがない場合の処理
			noPrefFile();// 　初期値を入力する
		}
		
		prefFile = null;
	
	}

	@Override
	public void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
		// Bind

		tv = (TextView) findViewById(R.id.tvPloviderList);

		Intent intent = new Intent(getApplicationContext(), NmeaService.class);
		bindService(intent, mySrviceConnection, Context.BIND_AUTO_CREATE);//サービスをバインド

	}

	private ServiceConnection mySrviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO 自動生成されたメソッド・スタブ
			LocalBinder binder = (NmeaService.LocalBinder) service;

			if (binder != null) {
				//binder.getService(tv);
				//binder.getService(tv);
				binder.getService(new UpdateData() {

					@Override
					public void update(Message msg) {
						// TODO 自動生成されたメソッド・スタブ
						Bundle bundle = msg.getData();

						Log.i(TAG, "run001::" + bundle );
						Log.i(TAG, "run002:" + bundle.getString("text") );
						
						//Serializable text = bundle.getSerializable("data");
						
						//HashMap<String, String> Numbermap = new HashMap<String, String>();
						if(bundle!=null)
						{
							//Numbermap = ;
						}
						
						 @SuppressWarnings("unchecked")
						HashMap<String, ArrayList<String>> arrayListNMEA = (HashMap<String, ArrayList<String>>) bundle.getSerializable("NMEAmap") ;
						@SuppressWarnings("unchecked")
						HashMap<String, String> arrayListNmeaRawData = (HashMap<String, String>) bundle.getSerializable("NmeaRawData") ;

						 
						ArrayList<String> arrayListGPGGA = arrayListNMEA.get("GPGGA");
						String stringGPGGA = arrayListNmeaRawData.get("GPGGA");
						
						Log.i(TAG, "runYY003" + arrayListGPGGA );
						Log.i(TAG, "runYY004" + stringGPGGA );

						
						ArrayList<String> arrayListGPRMC = arrayListNMEA.get("GPRMC");

						if( arrayListGPGGA != null && !stringGPGGA.isEmpty() ){
							TextView tvGPGGArawdata = (TextView) findViewById(R.id.tvGPGGArawdata);// 生のデータ
							tvGPGGArawdata.setText( stringGPGGA );
							printOutGPGGA( arrayListGPGGA );
						}
						
						if ( arrayListGPRMC != null ){
							printOutGPRMC( arrayListGPRMC );
						}

						tv.setText(msg + "");
					}

				});

			} else {
				Log.i(TAG, "binder は　null");
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO 自動生成されたメソッド・スタブ

		}
	};

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume 計測スタート");
		
		Initialize();//初期化する

	// 　プロバイダの状態を確認する------------必要　移動
		//providerCheck();
		
		//SampleHandler handler = new SampleHandler(this);
		
/*		 Intent nmeaIntent = new Intent(MainActivity.this, NmeaService.class);
		 nmeaIntent.setAction("start");
		 nmeaIntent.putExtra("messenger", new Messenger(handler));
         startService(nmeaIntent);*/


	}


	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause 計測中止");

		if (locationManager != null) {
			locationManager.removeUpdates(this);
			locationManager.removeNmeaListener(this);
		}
		
		if (locationManagerNmea != null) {
			locationManagerNmea.removeUpdates(this);
			locationManagerNmea.removeNmeaListener(this);
		}
		
		if( mode == 1){
			unregisterReceiver(receiv);
		}

	}
	 @Override
	 public void onStop() {
	  Log.i(TAG, "onStop");
	  super.onStop();
	  // Unbind
	  unbindService(mySrviceConnection);//サービスをアンバインド
	 
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	// オプションメニューアイテムが選択された時に呼び出されます
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.item1:
			finish();
			break;

		default:
			break;
		}
		
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO 自動生成されたメソッド・スタブ
		//Log.d(TAG, "onLocationChanged");
		Log.v("Provider", String.valueOf(location.getProvider()));

		// 状態表示
		if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
			printOutNetwork(location);// 状態表示　ネットワークの場合
		} else if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
			//Log.v("Time", "************GPS_PROVIDER********");
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

		//Log.d("", nmea);//
		nmea = nmea.trim();

		// 区切り文字で分解
		String[] nmeaArray = nmea.split(",");

		ArrayList<String> nmeaList = new ArrayList<String>(
				Arrays.asList(nmeaArray));

		// 　チェックサムを処理
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
			Log.d("$GPGSV", nmea);
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
	 * 概要：初期化する
	 */
	private void Initialize() {
		// TODO 自動生成されたメソッド・スタブ
		
		// プロバイダ取得要件を利用するか調べる
		SharedPreferences pref  = getSharedPreferences("com.example.simpleloggergps_preferences", MODE_PRIVATE);
		boolean providerSelect = pref.getBoolean("providerSelect", false);

		long miniTimeLiong = Long.parseLong( pref.getString("minTime", "999") );
		float  miniDistanceFloat = Float.parseFloat( pref.getString("minDistance", "999") );
		Log.i(TAG, "更新間隔" + miniTimeLiong + "::" + miniDistanceFloat );


		LinearLayout lineLayoutGps = (LinearLayout) findViewById(R.id.lineLayoutGps);
		LinearLayout lineLayoutNetwork = (LinearLayout) findViewById(R.id.lineLayoutNetwork);

		if (providerSelect) {
			Log.i(TAG, "プロバイダ選択要件を使う場合");
			//プロバイダ取得要件を使う場合
			mode = 1;

			// 位置取得要件を読み込む　Criteria　
			Criteria criteria = readPrefCriteria();
			
			String provider = locationManager.getBestProvider(criteria, true);
			
			TextView tvPloviderList = (TextView) findViewById(R.id.tvPloviderList);
			if( provider.equals("network") ){
				Log.i(TAG, "ベストプロバイダ::network" );
				tvPloviderList.setText("network プロバイダが選択されました。");
				
				//gpsプロバイダ　表示部分を非表示にする
				lineLayoutGps.setVisibility(View.GONE);  


			}else if(  provider.equals("gps")  ){
				Log.i(TAG, "ベストプロバイダ::gps" );
				tvPloviderList.setText("gps プロバイダが選択されました。");
				lineLayoutNetwork.setVisibility(View.GONE);
			}
			
			mode1( criteria, miniTimeLiong, miniDistanceFloat );
		} else {
			Log.i(TAG, "プロバイダ選択要件を使わない場合");
			lineLayoutNetwork.setVisibility(View.VISIBLE);
			lineLayoutGps.setVisibility(View.VISIBLE);  

			mode = 0;
			providerCheck( miniTimeLiong, miniDistanceFloat);
		}
		
	}
	
	/*
	 * 概要：プロバイダ選択要件を使用する場合
	 */
	private void mode1(Criteria criteria, long miniTimeLiong, float miniDistanceFloat) {
		// TODO 自動生成されたメソッド・スタブ
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_LOCATION_UPDATE);
		
		MainActivity mainActivity = MainActivity.this;
		
		receiv = new ReceiveLocation( new Handler() ,mainActivity);
		registerReceiver(receiv, filter);
		
		 //PendingIntentの生成
		//Intent intent = new Intent(this, ReceiveLocation.class);
		Intent intent = new Intent();
		intent.setAction(ACTION_LOCATION_UPDATE);
		
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
 
        locationManager.requestLocationUpdates(miniTimeLiong, miniDistanceFloat, criteria, pi);//
        
        //locationManager.requestLocationUpdates (LocationManager.NETWORK_PROVIDER, 1000, 0, pi);
        //locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 1000, 0, pi);
        //locationManager.addNmeaListener(this);
        
        locationManagerNmea.requestLocationUpdates (LocationManager.GPS_PROVIDER, miniTimeLiong, miniDistanceFloat, this);
        //locationManagerNmea.addNmeaListener(this);//
	}

	/*
	 * 概要：プロバイダの状態をチェックする 引数：ロケーションマネージャー
	 */
	private void providerCheck(long miniTimeLiong, float miniDistanceFloat) {
		// TODO 自動生成されたメソッド・スタブ
		

		String providerStatus = "";

		List<String> providers = locationManager.getAllProviders();
		for (String provider : providers) {
			if (locationManager.isProviderEnabled(provider)) {
				//Log.i("getAllProviders", provider + "　は有効");
				providerStatus += provider + " : 有効\n";
				//locationManager.requestLocationUpdates(provider, 1000, 0, this);
				if ( provider.equals("gps") ){
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, miniTimeLiong, miniDistanceFloat, this);
					//locationManager.addNmeaListener(this);
				}else if (  provider.equals("network") ){
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, miniTimeLiong, miniDistanceFloat, this);
				}
			} else {
				// Log.i("getAllProviders", provider + "　無効");
				providerStatus += provider + " : 無効\n";
				// state = "無効";
				if ( provider.equals("gps") ){
					// GPS プロバイダが無効の場合 nmea を取得できないと表示する。
				}
			}
		}
		
		//TextView tvGpsState = (TextView) findViewById(R.id.tvPloviderList);//
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPloviderList)).setText(providerStatus);
		//tvGpsState.setText(providerStatus);
		providerStatus = null;
	}

	/*
	 * 概要：設定値を調べる
	 */
	private Criteria readPrefCriteria() {
		// TODO 自動生成されたメソッド・スタブ
		Criteria criteria = new Criteria();
		
		SharedPreferences pref  = getSharedPreferences("com.example.simpleloggergps_preferences", MODE_PRIVATE);
		
		String accuracy = pref.getString("accuracy", "読み込みエラー");					// 位置精度
		String horizontalAccuracy = pref.getString("horizontalAccuracy", "読み込みエラー");// 水平精度(緯経度)
		String verticalAccuracy = pref.getString("verticalAccuracy", "読み込みエラー");	// 垂直精度(高度)
		String bearingAccuracy = pref.getString("bearingAccuracy", "読み込みエラー");	// ベアリングの精度
		String powerRequirement = pref.getString("powerRequirement", "読み込みエラー");	// 電力要件
		String speedAccuracy = pref.getString("speedAccuracy", "読み込みエラー");			// 速度情報

		boolean altitudeRequired = pref.getBoolean("altitudeRequired", false);		// 標高
		boolean bearingRequired = pref.getBoolean("bearingRequired", false);		// ベアリング
		boolean speedRequired = pref.getBoolean("speedRequired", false);			// スピード
		boolean costAllowed = pref.getBoolean("costAllowed", false);				// 金銭コスト

		criteria.setAccuracy( Integer.parseInt( accuracy ) );						// 緯経度取得の位置精度
		criteria.setHorizontalAccuracy( Integer.parseInt( horizontalAccuracy ) );	// 水平精度(緯経度)
		criteria.setVerticalAccuracy( Integer.parseInt( verticalAccuracy ) );		// 垂直精度(高度)
		criteria.setPowerRequirement( Integer.parseInt( powerRequirement ) );		// 電力要件
		
		criteria.setBearingRequired( bearingRequired );						//ベアリングの情報を取得するか
		criteria.setBearingAccuracy( Integer.parseInt( bearingAccuracy ) );	// ベアリングの精度
		
		criteria.setSpeedRequired( speedRequired );
		criteria.setSpeedAccuracy( Integer.parseInt( speedAccuracy ) );
		
		criteria.setAltitudeRequired( altitudeRequired );	//標高の情報を取得するか
		criteria.setCostAllowed( costAllowed );				//コスト要件
		
		return criteria;
	}
	/*
	 * 設定ファイルが無かったら設定値を設定する
	 */
	private void noPrefFile() {
		// TODO 自動生成されたメソッド・スタブ
		Criteria criteria = new Criteria();

		// Log.i("", "精度 " + criteria.getAccuracy());

		// 　設定ファイルに書き込み
		prefWriteBoolean("providerSelect", false);// criteria を使うか
		
		prefWriteString("accuracy", String.valueOf(criteria.getAccuracy())); // 位置精度
		prefWriteString("bearingAccuracy",String.valueOf(criteria.getBearingAccuracy())); // ベアリングの精度
		
		prefWriteString("horizontalAccuracy",String.valueOf(criteria.getHorizontalAccuracy())); // 水平精度(緯経度)
		prefWriteString("verticalAccuracy",	String.valueOf(criteria.getVerticalAccuracy())); // 垂直精度(高度)
		prefWriteString("powerRequirement",String.valueOf(criteria.getPowerRequirement())); // 電力要件
		prefWriteString("speedAccuracy",String.valueOf(criteria.getSpeedAccuracy())); // 速度情報
		
		prefWriteBoolean("altitudeRequired", criteria.isAltitudeRequired());// 標高
		prefWriteBoolean("bearingRequired", criteria.isBearingRequired());// ベアリング
		prefWriteBoolean("costAllowed", criteria.isCostAllowed());// コスト
		prefWriteBoolean("speedRequired", criteria.isSpeedRequired());// スピード
		
		prefWriteString("minTime", String.valueOf(1 * 60 * 1000));// 更新間隔　時間(1分)
		prefWriteString("minDistance", String.valueOf(10));// 更新間隔　距離(10m)


		criteria = null;
	}

	/*
	 * 設定ファイルの書き込み ブーレン
	 */
	void prefWriteBoolean(String key, boolean b) {
		// TODO 自動生成されたメソッド・スタブ
		makeEditor().putBoolean(key, b).commit();
	}
	
	/*
	 * 設定ファイルの書き込み 数値設定
	 */
	void prefWriteString(String key, String string) {
		// TODO 自動生成されたメソッド・スタブ
		makeEditor().putString(key, string).commit();
	}

	/*
	 * 概要：設定ファイルに接続する
	 */
	private Editor makeEditor() {
		// TODO 自動生成されたメソッド・スタブ
		return getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE).edit();
	}



	/*
	 * 概要：ネットワークプロバイダの値を表示する 引数：Location
	 */
	private void printOutNetwork(Location location) {
		// TODO 自動生成されたメソッド・スタブ
		
		((TextView) getWindow().getDecorView().findViewById(R.id.tvNetTime)).setText( String.valueOf(location.getTime()) );// 時間
		((TextView) getWindow().getDecorView().findViewById(R.id.tvNetTimeConvert)).setText( dataformat(location.getTime()) );// 時間　変換値
		
		((TextView) getWindow().getDecorView().findViewById(R.id.tvNetLatitude)).setText( String.valueOf(location.getLatitude()) );// 緯度
		((TextView) getWindow().getDecorView().findViewById(R.id.tvNetLongitude)).setText( String.valueOf(location.getLongitude()) );// 経度
		((TextView) getWindow().getDecorView().findViewById(R.id.tvNetAltitude)).setText( String.valueOf(location.getAltitude()) );// 標高
		((TextView) getWindow().getDecorView().findViewById(R.id.tvNetSpeed)).setText( String.valueOf(location.getSpeed()) );// 速度
		((TextView) getWindow().getDecorView().findViewById(R.id.tvNetAccuracy)).setText( String.valueOf(location.getAccuracy()) );// 精度
		((TextView) getWindow().getDecorView().findViewById(R.id.tvNetBearing)).setText( String.valueOf(location.getBearing()) );// ベアリング
	}

	/*
	 * 概要：ＧＰＳプロバイダの値を表示する 引数：Location
	 */
	private void printOutGps(Location location) {
		// TODO 自動生成されたメソッド・スタブ
		
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGpsTime)).setText( String.valueOf(location.getTime()) );// 時間
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGpsTimeConvert)).setText( dataformat(location.getTime()) );// 時間　変換値
		
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGpsLatitude)).setText( String.valueOf(location.getLatitude()) );// 緯度
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGpsLongitude)).setText( String.valueOf(location.getLongitude()) );// 経度
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGpsAltitude)).setText( String.valueOf(location.getAltitude()) );// 標高
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGpsSpeed)).setText( String.valueOf(location.getSpeed()) );// 速度
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGpsAccuracy)).setText( String.valueOf(location.getAccuracy()) );// 精度
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGpsBearing)).setText( String.valueOf(location.getBearing()) );// ベアリング

	}

	// --------------------- NMEA ------------------------------
	/*
	 * 概要：NMEA GPGSV の表示
	 */
	private void printOutGPGSV(String nmea, long timestamp,	ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ

		//Log.i("", "ddddddddddddddddddddddddddd");
		
		// 時間変換
		//String date = dataformat(timestamp);

		// 時間の表示
		TextView tvGPGSVtimestamp = (TextView) findViewById(R.id.tvGPGSVtimestamp);// 時間

		tvGPGSVtimestamp.setText(timestamp + " = " + dataformat(timestamp) );

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
			linelayout = null;
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
			linelayout = null;
		}
		
		tvGPGSVtimestamp = null;
		nmeaList = null;
		tvGPGSVrawData = null;
		lineLayoutAddData = null;

	}

	/*
	 * 概要：NMEA GPGGA の表示
	 */
	private void printOutGPGGA(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ
		String time = null;


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

		
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA01)).setText( nmeaList.get(1) + time );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA02)).setText( nmeaList.get(2) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA03)).setText( nmeaList.get(3) + "  [N:北緯,S:南緯]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA04)).setText( nmeaList.get(4) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA05)).setText( nmeaList.get(5) + "  [E: 東経,W:西経]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA06)).setText( nmeaList.get(6) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA07)).setText( nmeaList.get(7) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA08)).setText( nmeaList.get(8) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA09)).setText( nmeaList.get(9) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA10)).setText( nmeaList.get(10) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA11)).setText( nmeaList.get(11) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA12)).setText( nmeaList.get(12) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA13)).setText( nmeaList.get(13) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA14)).setText( nmeaList.get(14) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPGGA15)).setText( nmeaList.get(15) );

		time = null;

	}

	/*
	 * NMEA GPRMCの表示
	 */
	private void printOutGPRMC(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ

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
		//String dataStatus = nmeaList.get(2) + "  [V:警告,A:有効]";
		
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC01)).setText( nmeaList.get(1) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC02)).setText( nmeaList.get(2) + "  [V:警告,A:有効]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC03)).setText( nmeaList.get(3) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC04)).setText( nmeaList.get(4) + "  [N:北緯,S:南緯]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC05)).setText( nmeaList.get(5) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC06)).setText( nmeaList.get(6) + "  [E: 東経,W:西経]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC07)).setText( nmeaList.get(7) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC08)).setText( nmeaList.get(8) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC09)).setText( nmeaList.get(9) + date );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC10)).setText( nmeaList.get(10) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC11)).setText( nmeaList.get(11) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPRMC12)).setText( nmeaList.get(12) );
		
		date = null;

	}

	/*
	 * NMEA GPVTG の表示
	 */
	private void printOutGPVTG(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ
		
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG01)).setText( nmeaList.get(1) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG02)).setText( nmeaList.get(2) + "  [T:真北]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG03)).setText( nmeaList.get(3) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG04)).setText( nmeaList.get(4) + "  [M:磁北]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG05)).setText( nmeaList.get(5) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG06)).setText( nmeaList.get(6) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG07)).setText( nmeaList.get(7) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG08)).setText( nmeaList.get(8) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG09)).setText( nmeaList.get(9) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvGPVTG10)).setText( nmeaList.get(10) );

	}

	/*
	 * NMEA GPGSA の表示
	 */
	private void printOutGPGSA(ArrayList<String> nmeaList) {
		// TODO 自動生成されたメソッド・スタブ
		
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA1)).setText( nmeaList.get(1) + "  [M:手動,A: 自動]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA2)).setText( nmeaList.get(2) + "  [1:存在しない,2:2D特定,3:3D特定]" );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA3)).setText( nmeaList.get(3) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA4)).setText( nmeaList.get(4) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA5)).setText( nmeaList.get(5) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA6)).setText( nmeaList.get(6) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA7)).setText( nmeaList.get(7) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA8)).setText( nmeaList.get(8) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA9)).setText( nmeaList.get(9) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA10)).setText( nmeaList.get(10) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA11)).setText( nmeaList.get(11) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA12)).setText( nmeaList.get(12) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA13)).setText( nmeaList.get(13) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA14)).setText( nmeaList.get(14) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA15)).setText( nmeaList.get(15) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA16)).setText( nmeaList.get(16) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA17)).setText( nmeaList.get(17) );
		((TextView) getWindow().getDecorView().findViewById(R.id.tvPGGSA18)).setText( nmeaList.get(18) );

	}

	/*
	 * UTC 時間を変換
	 */
	static String dataformat(long utcTime) {
		// TODO 自動生成されたメソッド・スタブ
		//SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS",Locale.JAPAN);

		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS",Locale.JAPAN).format(utcTime);
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
			// Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.buttonLog:
			Log.i("buttonTest", "ボタン２が押された");

			Intent intent2 = new Intent(this, LogDialogActivity.class);
			startActivity(intent2);
			
			break;

		}

	}
	


}
