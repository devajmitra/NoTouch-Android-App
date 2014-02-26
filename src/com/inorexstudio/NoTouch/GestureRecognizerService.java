package com.inorexstudio.NoTouch;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class GestureRecognizerService extends Service implements
		SensorEventListener {
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
		makeForeground();
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

	private void makeForeground() {
		NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(
				this);
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		Notification notification = notiBuilder
				.setContentTitle("NoTouch Smart Gestures")
				.setContentText("Gesture Recognition On")
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pendingIntent)
				.setTicker("NoTouch Smart Gestures Active").build();
		startForeground(4242, notification);
	}

	private void removeForeground() {
		stopForeground(true);
	}

	private final IBinder mBinder = new LocalBinder();

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

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	private long gestureStartTime, gestureEndTime;
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.values[0] == 0) {
			Log.e("proximity", "obstacle detected");
			gestureStartTime = System.nanoTime();

		} else {
			Log.e("proximity", "no obstacle");
			gestureEndTime = System.nanoTime();
			long gesturePeriod = gestureEndTime - gestureStartTime;
			long gesturePeriodMillis = gesturePeriod / 1000000;
			
			if (gesturePeriodMillis > 100 && gesturePeriodMillis <= 750) {
				Log.e("gesture", "wave");
				onWaveGesture();
			} else if (gesturePeriodMillis > 750 && gesturePeriodMillis < 2000) {
				Log.e("gesture", "hover");
				onHoverGesture();
			}
		}
	}

	private int waveCount = 0;
	static final int MAXWAVES = 2;
	private void onWaveGesture() {
		waveCount++;
		
		if(waveCount == 1) {
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				  @Override
				  public void run() {
				    //Do something after 1000ms
					  finishWaveGesture();
				  }
				}, 760*(MAXWAVES - 1));
		}
	}
	
	private void finishWaveGesture() {
		switch(waveCount) {
		case 1:
			Log.e("wave", "Single wave");
			nextSong();
			break;
		case 2:
			Log.e("wave", "Double  wave");
			prevSong();
			break;
		}
		waveCount = 0;
	}
	
	private void onHoverGesture() {
		playPause();
	}

	private void nextSong() {
		if (mAudioManager.isMusicActive()) {
			long eventtime = SystemClock.uptimeMillis();

			Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
			KeyEvent downEvent = new KeyEvent(eventtime, eventtime,
					KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
			downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
			sendOrderedBroadcast(downIntent, null);

			Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
			KeyEvent upEvent = new KeyEvent(eventtime, eventtime,
					KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT, 0);
			upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
			sendOrderedBroadcast(upIntent, null);
			Toast.makeText(getBaseContext(), "Next Song", Toast.LENGTH_SHORT).show();
		}
	}

	private void prevSong() {
		if (mAudioManager.isMusicActive()) {
			long eventtime = SystemClock.uptimeMillis();

			Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
			KeyEvent downEvent = new KeyEvent(eventtime, eventtime,
					KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
			downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
			sendOrderedBroadcast(downIntent, null);

			Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
			KeyEvent upEvent = new KeyEvent(eventtime, eventtime,
					KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
			upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
			sendOrderedBroadcast(upIntent, null);
			Toast.makeText(getBaseContext(), "Previous Song", Toast.LENGTH_SHORT).show();
		}
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
		Toast.makeText(getBaseContext(), "Play/Pause", Toast.LENGTH_SHORT).show();
	}
}
