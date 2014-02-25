package com.inorexstudio.NoTouch;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";
	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDSTOP = "stop";
	AudioManager mAudioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		findViewById(R.id.prev).setOnClickListener(this);
		findViewById(R.id.pause).setOnClickListener(this);
		findViewById(R.id.next).setOnClickListener(this);
		
		startService(new Intent(getBaseContext(), GestureRecognizerService.class));
	}

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(getBaseContext(), GestureRecognizerService.class));
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button b = (Button) v;
		switch (b.getId()) {
		case R.id.prev:
			Log.e("e", "prev");
			if (mAudioManager.isMusicActive()) {
				Intent i = new Intent(SERVICECMD);
				i.putExtra(CMDNAME, CMDPREVIOUS);
				MainActivity.this.sendBroadcast(i);
			}
			break;
		case R.id.pause:
			Log.e("e", "pause");

			if (mAudioManager.isMusicActive()) {
				Intent i = new Intent(SERVICECMD);
				i.putExtra(CMDNAME, CMDTOGGLEPAUSE);
				MainActivity.this.sendBroadcast(i);
			}
			break;
		case R.id.next:
			Log.e("e", "next");
			if (mAudioManager.isMusicActive()) {
				Intent i = new Intent(SERVICECMD);
				i.putExtra(CMDNAME, CMDNEXT);
				MainActivity.this.sendBroadcast(i);
			}
			break;
		}
	}

}
