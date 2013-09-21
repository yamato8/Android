package com.example.simpleloggergps;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceScreen;

public class SummarySetting {

	public void printOut(PreferenceScreen preferenceScreen) {
		// TODO 自動生成されたメソッド・スタブ

		// 　プロバイダ選択基準
		CheckBoxPreference cbpProvider = (CheckBoxPreference) preferenceScreen.findPreference("providerSelect");
		providerSummary(cbpProvider);

		// 位置精度設定
		ListPreference lpAccuracy = (ListPreference) preferenceScreen.findPreference("accuracy");
		accuracySummary(lpAccuracy);

		// ベアリング精度設定
		ListPreference lpBearingAccuracy = (ListPreference) preferenceScreen.findPreference("bearingAccuracy");
		threeSummary(lpBearingAccuracy);

		// 水平精度設定
		ListPreference lpHorizontalAccuracy = (ListPreference) preferenceScreen.findPreference("horizontalAccuracy");
		fourItemSummary(lpHorizontalAccuracy);

		// 垂直精度設定
		ListPreference lpVerticalAccuracy = (ListPreference) preferenceScreen.findPreference("verticalAccuracy");
		fourItemSummary(lpVerticalAccuracy);

		// 電力要件の設定
		ListPreference lpPowerRequirement = (ListPreference) preferenceScreen.findPreference("powerRequirement");
		powerRequirementSummary(lpPowerRequirement);

		// 速度要件の設定
		ListPreference lpSpeedAccuracy = (ListPreference) preferenceScreen.findPreference("speedAccuracy");
		threeSummary(lpSpeedAccuracy);

		// コスト要件の設定
		ListPreference lpCostAllowed = (ListPreference) preferenceScreen.findPreference("costAllowed");
		costAllowedSummary(lpCostAllowed);

		// 通知間隔・時間の設定
		EditTextPreference etpMiniTime = (EditTextPreference) preferenceScreen.findPreference("minTime");
		etpMiniTime.setSummary(etpMiniTime.getText());

		// 通知間隔・距離の設定
		EditTextPreference etpMinDistance = (EditTextPreference) preferenceScreen.findPreference("minDistance");
		etpMinDistance.setSummary(etpMinDistance.getText());
	}

	/*
	 * 概要：コスト要件のサマリー
	 */
	private void costAllowedSummary(ListPreference lpCostAllowed ) {
		// TODO 自動生成されたメソッド・スタブ
		if ( lpCostAllowed.getValue().endsWith("0") ) {
			lpCostAllowed.setSummary( R.string.prefCostAllowed_false );//許可しない
		}else if( lpCostAllowed.getValue().endsWith("1") ){
			lpCostAllowed.setSummary( R.string.prefCostAllowed_true );// 許可する
		}
	}
	
	/*
	 * 概要：電力要件のサマリー
	 */
	private void powerRequirementSummary(ListPreference lpPowerRequirement) {
		// TODO 自動生成されたメソッド・スタブ
		if ( lpPowerRequirement.getValue().endsWith("0") ) {
			lpPowerRequirement.setSummary( R.string.prefAccuracy_no_requirement );//指定なし
		}else if( lpPowerRequirement.getValue().endsWith("1") ){
			lpPowerRequirement.setSummary( R.string.prefPowerRequirement_low );// 低精度
		}else if(  lpPowerRequirement.getValue().endsWith("2") ){
			lpPowerRequirement.setSummary( R.string.prefPowerRequirement_medium );//中精度
		}else if( lpPowerRequirement.getValue().endsWith("3") ){
			lpPowerRequirement.setSummary( R.string.prefPowerRequirement_hight );//高精度
		}
	}


	/*
	 * 概要：プロバイダのサマリー
	 */
	private void providerSummary(CheckBoxPreference cbpProvider) {
		// TODO 自動生成されたメソッド・スタブ
		if (cbpProvider.isChecked()) {
			cbpProvider.setSummary( R.string.prefProviderUse );
		} else {
			cbpProvider.setSummary( R.string.prefProviderDoNotUse );
		}
	}

	/*
	 * 概要：３個アイテムが在るサマリー
	 */
	private void fourItemSummary(ListPreference listPreference ) {
		// TODO 自動生成されたメソッド・スタブ
		if ( listPreference.getValue().endsWith("0") ) {
			listPreference.setSummary( R.string.prefAccuracy_no_requirement );//指定なし
		}else if( listPreference.getValue().endsWith("1") ){
			listPreference.setSummary( R.string.prefAccuracy_low );// 低精度
		}else if(  listPreference.getValue().endsWith("2") ){
			listPreference.setSummary( R.string.prefAccuracy_medium );//中精度
		}else if( listPreference.getValue().endsWith("3") ){
			listPreference.setSummary( R.string.prefAccuracy_hight );//高精度
		}
	}

	/*
	 * 概要：ベアリングのサマリー
	 */
	private void threeSummary(ListPreference lpBearingAccuracy) {
		// TODO 自動生成されたメソッド・スタブ
		if ( lpBearingAccuracy.getValue().endsWith("0") ) {
			lpBearingAccuracy.setSummary( R.string.prefAccuracy_no_requirement );//指定なし
		}else if( lpBearingAccuracy.getValue().endsWith("1") ){
			lpBearingAccuracy.setSummary( R.string.prefAccuracy_low );// 低精度
		}else if( lpBearingAccuracy.getValue().endsWith("3") ){
			lpBearingAccuracy.setSummary( R.string.prefAccuracy_hight );//高精度
		}
	}

	/*
	 * 概要：位置情報のサマリー
	 */
	private void accuracySummary(ListPreference lpAccuracy) {
		// TODO 自動生成されたメソッド・スタブ
		if ( lpAccuracy.getValue().endsWith("1") ) {
			lpAccuracy.setSummary( R.string.prefAccuracy_hight );//高精度
		}else if( lpAccuracy.getValue().endsWith("2") ){
			lpAccuracy.setSummary( R.string.prefAccuracy_low );//低精度
		}
	}

}
