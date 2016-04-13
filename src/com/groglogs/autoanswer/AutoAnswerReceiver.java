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
import android.net.Uri;

import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log; 

public class AutoAnswerReceiver extends BroadcastReceiver {

            private MediaPlayer mediaPlayer ;

	@Override
	public void onReceive(Context context, Intent intent) {

		// Load preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	      	Log.w("mylog","phone event1");

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

	      	Log.w("mylog","phone is picked up");
		   AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	           am.setSpeakerphoneOn(true);  
                   am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,  
                          am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),  
                          AudioManager.STREAM_VOICE_CALL); 

	           //mediaPlayer = new MediaPlayer() ;
                   //try {
        
                   //    mediaPlayer.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                   //         + "/tongnian.mp3");
                   //    mediaPlayer.prepare();
                   //}
                   //catch (IOException e){
                   //}
        
                   //mediaPlayer.start();

		   //	context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bagualu.net")));

		   new Thread(){

			@Override
			public void run() {
                             try{
		                  Log.w("mylog","begin http request");
		                  HttpClient client = new DefaultHttpClient();
		                  Log.w("mylog","begin http request 2");
                                  HttpGet request = new HttpGet("http://192.168.0.103:8100/start");
		                  Log.w("mylog","begin http request 3");
		                  HttpResponse response = client.execute(request);
		                  Log.w("mylog","begin http request 4");
		                  response.getEntity().getContent().close();
		                  Log.w("mylog","finish http request");
                             }
                             catch (Exception e){
		                  Log.w("mylog","http request exception happened");
		                  e.printStackTrace();
                             }
			}
		   }.start() ;

		}		
	}
}

