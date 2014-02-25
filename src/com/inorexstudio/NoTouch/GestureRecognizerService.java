package com.inorexstudio.NoTouch;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

public class GestureRecognizerService extends Service implements
		SensorEventListener {
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";
	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDSTOP = "stop";

	AudioManager mAudioManager;
	SensorManager sm;
	Sensor proxSensor;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		proxSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		sm.registerListener(GestureRecognizerService.this, proxSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sm.unregisterListener(GestureRecognizerService.this, proxSensor);
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}
	
	private void nextSong() {
		long eventtime = SystemClock.uptimeMillis();
		
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_MEDIA_NEXT, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		sendOrderedBroadcast(downIntent, null);
	
		Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent upEvent = new KeyEvent(eventtime, eventtime,
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
		upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
		sendOrderedBroadcast(upIntent, null);
	}

	private void prevSong() {
		long eventtime = SystemClock.uptimeMillis();
		
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		sendOrderedBroadcast(downIntent, null);
	
		Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent upEvent = new KeyEvent(eventtime, eventtime,
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
		upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
		sendOrderedBroadcast(upIntent, null);
	}
	
	private void playPause() {
		long eventtime = SystemClock.uptimeMillis();
		
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime,
				KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		sendOrderedBroadcast(downIntent, null);

		Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent upEvent = new KeyEvent(eventtime, eventtime,
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
		upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
		sendOrderedBroadcast(upIntent, null);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		if (event.values[0] == 0) {
			Log.e("proximity", "obstacle detected");
		} else {
			Log.e("proximity", "no obstacle");
			//nextSong();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public GestureRecognizerService getService() {
			return GestureRecognizerService.this;
		}
	}
}
