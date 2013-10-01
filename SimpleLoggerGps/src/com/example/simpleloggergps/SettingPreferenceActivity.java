package com.example.simpleloggergps;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Menu;

public class SettingPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private static final String TAG = "";// 8
	private SummarySetting summarySetting11;
	private boolean virsionFlag;

	//(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "onCreate");

		// version3.0 より前 <11
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			virsionFlag = true;
			Log.i(TAG, "3.0より前");
			PriorToo11();

		} else {
			// version3.0 以降
			virsionFlag = false;
			Log.i(TAG, "以降");
			than11();
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
	    super.onResume();
	    
	    if( virsionFlag ){
	    	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener( this );
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * １３以前
	 */
	@SuppressWarnings("deprecation")
	private void PriorToo11() {
		// TODO 自動生成されたメソッド・スタブ
		addPreferencesFromResource(R.xml.setting_preference);

		summarySetting11 = new SummarySetting();
		summarySetting11.printOut(getPreferenceScreen());
	}

	@TargetApi(11)
	private void than11() {
		// TODO 自動生成されたメソッド・スタブ
		getFragmentManager().beginTransaction().replace(android.R.id.content, new prefFragment()).commit();

	}
	
	@TargetApi(11)
	public static class prefFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
		private SummarySetting summarySetting;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.setting_preference);

			//Preference exercisesPref = findPreference("transmit");
			//exercisesPref.setSummary(R.string.hello_world);
			summarySetting = new SummarySetting();
			//com.example.simpleloggergps.Pref.this.summarySetting();
			//summarySetting.printOut();
			
			try{
			 summarySetting.printOut( getPreferenceScreen() );
			}catch(Exception e){
		          Log.e("Hello", "例外出力", e);
	        }
		}

		@Override
		public void onResume() {
		    super.onResume();
		    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		   
		}
		/*
		 * 概要：設定値が変更された時の処理
		 */
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			// TODO 自動生成されたメソッド・スタブ
			summarySetting.printOut( getPreferenceScreen() );
		}

	}

	// 11 以前のリスナー
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO 自動生成されたメソッド・スタブ
		summarySetting11.printOut( getPreferenceScreen() );
	}


	}