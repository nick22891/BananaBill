package com.projectgrapevine.bananabill;

import com.projectgrapevine.bananabill.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundMusicService extends Service {
	
	MediaPlayer mediaPlayer;
	
	static boolean backbuttonflag = false;
	
	static boolean isPlaying = false;
	
	public IBinder onBind (Intent i){
		
		return null;
		
	}
	
	public void onCreate () {
		
		super.onCreate();
		
	}
	
	public int onStartCommand (Intent i, int flags, int startid) {
		
		super.onStartCommand(i, flags, startid);
		
		isPlaying = true;
		
		backbuttonflag = false;

		//mediaPlayer = new MediaPlayer();
		
		mediaPlayer = MediaPlayer.create(this, R.raw.popgoestheweasel);
		
		mediaPlayer.setLooping(true);
		
		mediaPlayer.setVolume(0.5f, 0.5f);
		
		mediaPlayer.start(); // no need to call prepare(); create() does that for you
		
		return Service.START_NOT_STICKY;
		
		}
	
	public void onDestroy () {
		
		mediaPlayer.release();
		
		mediaPlayer = null;
		
		isPlaying = false;
		
		stopSelf();
		
		super.onDestroy();
		
	}
	
}
