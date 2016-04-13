package com.groglogs.autoanswer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import android.util.Log;


public class AutoAnswerPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private AutoAnswerNotifier mNotifier;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNotifier = new AutoAnswerNotifier(this);
                Log.v("mylog","autoanswer v is created");
                Log.i("mylog","autoanswer i is created");
                Log.d("mylog","autoanswer d is created");
                Log.w("mylog","autoanswer w is created");
		mNotifier.updateNotification();
		SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		addPreferencesFromResource(R.xml.preferences);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}
	
	//@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("enabled")) {
			mNotifier.updateNotification();
		}
	}
}
