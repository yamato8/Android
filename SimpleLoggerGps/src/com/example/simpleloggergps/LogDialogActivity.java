package com.example.simpleloggergps;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class LogDialogActivity extends Activity {

	private static final String TAG = "LogDialogActivity";
	private CheckBox checkBoxSaveDialogGps;
	private CheckBox checkBoxSaveDialogNetwork;
	private CheckBox checkBoxSaveDialogGPGSV;
	private CheckBox checkBoxSaveDialogGPGSA;
	private CheckBox checkBoxSaveDialogGPRMC;
	private CheckBox checkBoxSaveDialogGPVTG;
	private CheckBox checkBoxSaveDialogGPGGA;
	private EditText editTextSaveDialogTimeSpan;
	private EditText editTextSaveDialogTime;
	private Spinner spinnerTime;
	private Spinner spinnerSpan;
	private String check_gps;
	private String check_network;
	private String check_GPGSV;
	private String check_GPGSA;
	private String check_GPRMC;
	private String check_GPVTG;
	private String check_GPGGA;
	private String timeSpanUnit;
	private String timeUnit;
	private SharedPreferences pref;
	private Editor editor;
	private String editText_time;
	private String editText_temeSpan;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.log_dialog_activity); // レイアウトを決定  

		Log.i(TAG, "onCreate" );

		//setTitle("保存設定");

		checkBoxSaveDialogGps = (CheckBox) findViewById(R.id.CheckBoxSaveDialogGps);
		checkBoxSaveDialogNetwork = (CheckBox) findViewById(R.id.CheckBoxSaveDialogNetwork);

		checkBoxSaveDialogGPGSV = (CheckBox) findViewById(R.id.CheckBoxSaveDialogGPGSV);
		checkBoxSaveDialogGPGSA = (CheckBox) findViewById(R.id.CheckBoxSaveDialogGPGSA);
		checkBoxSaveDialogGPRMC = (CheckBox) findViewById(R.id.CheckBoxSaveDialogGPRMC);
		checkBoxSaveDialogGPVTG = (CheckBox) findViewById(R.id.CheckBoxSaveDialogGPVTG);
		checkBoxSaveDialogGPGGA = (CheckBox) findViewById(R.id.CheckBoxSaveDialogGPGGA);

		editTextSaveDialogTimeSpan = (EditText) findViewById(R.id.ETspanTime);
		editTextSaveDialogTime = (EditText) findViewById(R.id.ETtime);
		
		spinnerSpan = (Spinner) findViewById(R.id.spinnerTimeSpan);//保存間隔
		spinnerSpan.setOnItemSelectedListener( spinnerCheck );
		
		spinnerTime = (Spinner) findViewById(R.id.spinnerTime);		// 時間
		spinnerTime.setOnItemSelectedListener( spinnerCheck );
		
		//editTextSaveDialogTimeSpan.setOnFocusChangeListener( editTextFocusChange );
		//editTextSaveDialogTime.setOnFocusChangeListener( editTextFocusChange );//

/*		String saveingSettingFile = getApplicationContext().getFilesDir().getParentFile() + "/shared_prefs/savingPrefFile.xml";// 設定ファイル　ログ保存
		// 設定ファイルがあるか確認する
		File savingPewfFile = new File( saveingSettingFile );
		if ( savingPewfFile.exists()) {
			Log.i(TAG, "ファイルがある");
		} else {
			Log.i(TAG, "ファイルがない");
			// ファイルがない場合の処理
			//noPrefFile();// 　初期値を入力する
		}*/
		

		check_gps =  "gps";
		check_network =   "network";
		check_GPGSV =   "GPGSV";
		check_GPGSA =   "GPGSA";
		check_GPRMC =   "GPRMC";
		check_GPVTG =   "GPVTG";
		check_GPGGA =   "GPGGA";
		
        editText_temeSpan = "temeSpan";//保存間隔
        timeSpanUnit = "timeSpanUnit";//保存間隔 単位
        
        editText_time = "time";
        timeUnit = "timeUnit";
        
     // プリファレンスの準備 //
        pref = getApplicationContext().getSharedPreferences( "savingPrefFile", Context.MODE_PRIVATE );
     // プリファレンスに書き込むためのEditorオブジェクト取得 //
        editor = pref.edit();

	}

	  @Override
	    protected void onStart() {
	        super.onStart();
			Log.i(TAG, "onStart" );
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
			Log.i(TAG, "onResume"  );

			readPref();// 設定がいるを読み込んで反映
	    }


		@Override
	    protected void onRestart() {
	        super.onRestart();
			Log.i(TAG, "onRestart"  );
	    }
	    
	    @Override
	    protected void onPause() {
	        super.onPause();
			Log.i(TAG, "onPause"  );
			
			// 保存
			prefSave();
	    }

		@Override
	    protected void onStop() {
	        super.onStop();
			Log.i(TAG, "onStop"  );
	    }
	    
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        
	        //終了処理
			Log.i(TAG, "onDestroy"  );
			editor.clear();
	    }
	    
	    // -------------------------- 自作メソッド  --------------------------

	    /*
	     * 概要：スピナーが選択された時の処理　書き込み処理
	     */
	    private OnItemSelectedListener spinnerCheck = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> av, View v, int i, long arg3) {
			// TODO 自動生成されたメソッド・スタブ
			//Log.i(TAG, "onItemSelected");
			
			//spinnerTimeSpan
			switch (av.getId()) {
			case R.id.spinnerTimeSpan:
				prefWriteInt( timeSpanUnit, i );
				//Log.i("保存間隔", "1");

				break;
			case R.id.spinnerTime:
				//prefWrite( check_gps, status );
				prefWriteInt( timeUnit, i );

				//Log.i("時間", "2");

				break;
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO 自動生成されたメソッド・スタブ
			//Log.i(TAG, "onNothingSelected");

		}
	};
	    
	    /*
	     * 概要：設定値を読み込んで反映
	     */
	    private void readPref() {
			// TODO 自動生成されたメソッド・スタブ
	        Log.i(TAG, "設定値を読み込んで反映" );
			
			// 設定値読み込み
	 			
	        // キーで保存されている値を読み出す
	        boolean cheked = pref.getBoolean( check_gps, false );
	        checkBoxSaveDialogGps.setChecked(cheked);
	        cheked = pref.getBoolean( check_network, false );
	        checkBoxSaveDialogNetwork.setChecked(cheked);

	        cheked = pref.getBoolean( check_GPGSV, false );
	        checkBoxSaveDialogGPGSV.setChecked(cheked);
	        cheked = pref.getBoolean( check_GPGSA, false );
	        checkBoxSaveDialogGPGSA.setChecked(cheked);
	        cheked = pref.getBoolean( check_GPRMC, false );
	        checkBoxSaveDialogGPRMC.setChecked(cheked);
	        cheked = pref.getBoolean( check_GPVTG, false );
	        checkBoxSaveDialogGPVTG.setChecked(cheked);
	        cheked = pref.getBoolean( check_GPGGA, false );
	        checkBoxSaveDialogGPGGA.setChecked(cheked);
	        
	        int spanTime = pref.getInt( editText_temeSpan, 1 );//エディットボックス
	        editTextSaveDialogTimeSpan.setText( String.valueOf( spanTime ) );
	        int spanTimeUnit = pref.getInt(timeSpanUnit, 1);
			spinnerSpan.setSelection( spanTimeUnit );//既定値：１

	        
	        int time = pref.getInt( editText_time, 1 );//
	        editTextSaveDialogTime.setText( String.valueOf( time ) );
	        int spinnerTimeUnit = pref.getInt( timeUnit, 2);
	        spinnerTime.setSelection( spinnerTimeUnit );//既定値 2

		}
	    
	    /*
	     * 概要：チェックが押された時　の書き込み処理
	     */
	public void onCheck(View v) {
		//Log.i(TAG, "チェック"  );
		boolean status;

		if (((CheckBox) v).isChecked() == true) {
			//Log.i("状態チェック", "　クリック");
			status = true;
		} else {
			//Log.i("状態チェック", "　アンクリック");
			status = false;
		}

		switch (v.getId()) {
		case R.id.CheckBoxSaveDialogGps:
			prefWrite( check_gps, status );
			break;
		case R.id.CheckBoxSaveDialogNetwork:
			prefWrite( check_network, status );
			break;
		case R.id.CheckBoxSaveDialogGPGSV:
			prefWrite( check_GPGSV, status );
			break;
		case R.id.CheckBoxSaveDialogGPGSA:
			prefWrite( check_GPGSA, status );
			break;
		case R.id.CheckBoxSaveDialogGPRMC:
			prefWrite( check_GPRMC, status );
			break;
		case R.id.CheckBoxSaveDialogGPVTG:
			prefWrite( check_GPVTG, status );
			break;
		case R.id.CheckBoxSaveDialogGPGGA:
			prefWrite( check_GPGGA, status );
			break;
		}
		
		/*

		

        editor.putInt( "spanTeme", 12 );
        editor.putInt( "spanTemeUnit", 0 );

        editor.putInt( "time", 24 );
        editor.putInt( "spanTemeUnit", 0 );

        // 書き込みの確定（実際にファイルに書き込む）
        editor.commit();*/

	}
	
	/*
	 * 概要：設定値を書き込み
	 */

	private void prefWrite(String string, boolean status) {
		// TODO 自動生成されたメソッド・スタブ

		editor.putBoolean( string, status);

		// 書き込みの確定（実際にファイルに書き込む）
		editor.commit();
	}

	/*
	 * 概要：スピナーの設定値を保存
	 */
	private void prefWriteInt(String string, int i) {
		// TODO 自動生成されたメソッド・スタブ

		editor.putInt( string, i );

		// 書き込みの確定（実際にファイルに書き込む）
		editor.commit();
	}
	/*
	     * 概要：ボタンがクリックされた時の処理
	     */
	public void buttonClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		switch (v.getId()) {
		case R.id.logSaveDialogStart:
			//Log.i("状態チェック", "dialogBtnStart");
			
			if(checkBoxSaveDialogGps.isChecked() == true) {
				Log.i("状態チェック", "GPS　クリック");
			}else{
				Log.i("状態チェック", "GPS　アンクリック");	
			}
			
			if(checkBoxSaveDialogNetwork.isChecked() == true) {
				Log.i("状態チェック", "Neteork　クリック");
			}else{
				Log.i("状態チェック", "Network　アンクリック");	
			}
			
			if(checkBoxSaveDialogGPGSV.isChecked() == true) {
				Log.i("状態チェック", "GPGSV　クリック");
			}else{
				Log.i("状態チェック", "GPGSV　アンクリック");	
			}
		
			if(checkBoxSaveDialogGPGSA.isChecked() == true) {
				Log.i("状態チェック", "GPGSA　クリック");
			}else{
				Log.i("状態チェック", "GPGSA　アンクリック");	
			}
			
			if(checkBoxSaveDialogGPRMC.isChecked() == true) {
				Log.i("状態チェック", "GPRMC　クリック");
			}else{
				Log.i("状態チェック", "GPRMC　アンクリック");	
			}
			
			if(checkBoxSaveDialogGPVTG.isChecked() == true) {
				Log.i("状態チェック", "GPVTG　クリック");
			}else{
				Log.i("状態チェック", "GPVTG　アンクリック");	
			}
			
			if(checkBoxSaveDialogGPGGA.isChecked() == true) {
				Log.i("状態チェック", "GPGGA　クリック");
			}else{
				Log.i("状態チェック", "GPGGA　アンクリック");	
			}
			
			
			String spanTime = editTextSaveDialogTimeSpan.getText().toString();//間隔
			Log.i("状態チェック",  spanTime );
			long timeSpanUnit = spinnerSpan.getSelectedItemId();			//間隔
			Log.i("状態チェック", "timeSpanUnit" + timeSpanUnit );
			
			String time = editTextSaveDialogTime.getText().toString();//時間
			Log.i("状態チェック",  time );	
			long timeUnit = spinnerTime.getSelectedItemId();
			Log.i("状態チェック", "timeUnit" + timeUnit );// 0-秒 1-分 2-時間  3-日

			//サービスで保存処理
		

			
			break;
		case R.id.logSaveDialogCancel:
			Log.i("dialogBtnCancel", "キャンセルボタンが押された");
			
			finish();
			break;
		default:
			Log.i("default", "default");
		}
	}
	/*
	 * 概要：保存処理
	 */
    private void prefSave() {
		// TODO 自動生成されたメソッド・スタブ
    	//エディットボックスを保存
		prefWriteInt ( editText_temeSpan, Integer.valueOf( editTextSaveDialogTimeSpan.getText().toString() )  );
		prefWriteInt ( editText_time, Integer.valueOf( editTextSaveDialogTime.getText().toString() )  );
		
	}
}
