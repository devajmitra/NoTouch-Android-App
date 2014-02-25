package com.inorexstudio.NoTouch;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (GestureRecognizerService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button start = (Button)findViewById(R.id.startButton);
		final Button stop = (Button)findViewById(R.id.stopButton);
		
		start.setEnabled(!isMyServiceRunning());
		stop.setEnabled(isMyServiceRunning());
		
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startService(new Intent(getBaseContext(),GestureRecognizerService.class));
				//doBindService();
				start.setEnabled(false);
				stop.setEnabled(true);
				Toast.makeText(MainActivity.this, "NoTouch Activated!", Toast.LENGTH_SHORT).show();

			}
		});


		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				stopService(new Intent(getBaseContext(),GestureRecognizerService.class));
				//doUnbindService();
				stop.setEnabled(false);
				start.setEnabled(true);
				Toast.makeText(MainActivity.this, "NoTouch Stopped", Toast.LENGTH_SHORT).show();

			}
		});
		
		//startService(new Intent(getBaseContext(), GestureRecognizerService.class));
	}

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//stopService(new Intent(getBaseContext(), GestureRecognizerService.class));
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
