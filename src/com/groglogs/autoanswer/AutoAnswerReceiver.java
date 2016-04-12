package com.groglogs.autoanswer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import android.os.Environment;

import android.media.MediaPlayer;

import java.io.IOException;

public class AutoAnswerReceiver extends BroadcastReceiver {

            private MediaPlayer mediaPlayer ;

	@Override
	public void onReceive(Context context, Intent intent) {

		// Load preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		// Check phone state
		String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING) && prefs.getBoolean("enabled", false)) {
			// Check for "second call" restriction
			if (prefs.getBoolean("no_second_call", false)) {
				AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				if (am.getMode() == AudioManager.MODE_IN_CALL) {
					return;
				}
			}			

			// Call a service, since this could take a few seconds
			context.startService(new Intent(context, AutoAnswerIntentService.class));
		}		

		if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && prefs.getBoolean("enabled", false)) {

		   AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	           am.setSpeakerphoneOn(true);  
                   am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,  
                          am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),  
                          AudioManager.STREAM_VOICE_CALL); 

	           mediaPlayer = new MediaPlayer() ;
                   try {
        
                       mediaPlayer.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                            + "/tongnian.mp3");
                       mediaPlayer.prepare();
                   }
                   catch (IOException e){
                   }
        
                   mediaPlayer.start();

		}		
	}
}

