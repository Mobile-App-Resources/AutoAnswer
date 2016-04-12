package com.groglogs.autoanswer;

import java.lang.reflect.Method;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import android.media.MediaPlayer;


public class AutoAnswerPlayMediaService extends IntentService {

	public AutoAnswerPlayMediaService() {
		super("GingeroidsIntentService");
	}

	private MediaPlayer mediaPlayer ;
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Context context = getBaseContext();

		// Load preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		// Let the phone ring for a set delay
		try {
			Thread.sleep(Integer.parseInt(prefs.getString("delay", "2")) * 1000);
		} catch (InterruptedException e) {
			// We don't really care
		}

		// Make sure the phone is still ringing
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getCallState() != TelephonyManager.CALL_STATE_OFFHOOK) {
			return;
		}

		// Answer the phone
		try {
                    mediaPlayer = new MediaPlayer() ;
                    mediaPlayer.setDataSource("http://www.bagualu.net/resources/zaoduanl.mp3");
                    mediaPlayer.prepare();
		    mediaPlayer.start();
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.d("Gingeroids","Error trying to answer using telephony service.  Falling back to headset.");
		}

		return;
	}

}

