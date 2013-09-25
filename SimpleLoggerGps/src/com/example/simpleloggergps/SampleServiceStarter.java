package com.example.simpleloggergps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SampleServiceStarter extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent serviceIntent = new Intent(context, LogService.class);
            serviceIntent.setAction("start");
            context.startService(serviceIntent);
        }
	}
}