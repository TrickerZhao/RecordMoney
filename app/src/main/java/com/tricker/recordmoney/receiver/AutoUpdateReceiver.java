package com.tricker.recordmoney.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tricker.recordmoney.service.AutoUpdateService;


public class AutoUpdateReceiver extends BroadcastReceiver {
	public AutoUpdateReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context,AutoUpdateService.class);
		context.startService(i);
	}
}
