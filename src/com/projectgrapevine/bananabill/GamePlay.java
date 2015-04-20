package com.projectgrapevine.bananabill;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.flurry.android.FlurryAgent;

public class GamePlay extends Activity {

	private UiLifecycleHelper uiHelper;
	
Intent intent, i;

AnimationDrawable starAnimation;

int level;
int maxlevel;

boolean musicStopped = false;

boolean sfx = true;

boolean arcademode = false;
	
private FrameLayout myLayout;
	
	//private LinearLayout lvlLayout;
	
	private MyView mView;
	
	private ImageView animview;
	
	private ImageView thumbsUp, thumbsDown;
	
	public int width, height;
	
	public Session.StatusCallback myCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		//animview.setMargins(0, 0, 0, 0);
		
        super.onCreate(savedInstanceState);
        
        uiHelper = new UiLifecycleHelper(this, myCallback);
        
        myCallback = new Session.StatusCallback() {
        	
        	//called when session changes state
        	@SuppressWarnings("deprecation")
			public void call(Session session, SessionState state, Exception exception) {
        		
        		Log.e("mycallback", "runs!");
        		
        	}
        	
        };
        
        musicStopped = false;
        
        level = 1;
        
        intent = getIntent();
        
        level = intent.getIntExtra("level", 1);
        
        maxlevel = intent.getIntExtra("maxlevel", 1);
        
        sfx = intent.getBooleanExtra("sfx", true);
        
        arcademode = intent.getBooleanExtra("arcademode", false);
        
        if (arcademode) level = 32;
        
        i = (Intent)intent.getParcelableExtra("musicIntent");
        
        myLayout = new FrameLayout(this);
        
        Display display = getWindowManager().getDefaultDisplay();
        
        Point size = new Point();
                
        //if (sfx) myClick.start();
        
        if (android.os.Build.VERSION.SDK_INT >= 13) { 
        	
        	display.getSize(size);
        	            
            width = size.x;
            
            height = size.y;
        	
        }
        
        else {
        	
        	width = display.getWidth();
        	
        	height = display.getHeight();
        	
        }
        
		animview = new ImageView(this);
		
		thumbsUp = new ImageView(this);
		
		thumbsDown = new ImageView(this);
		
		//animview.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
		
		animview.setLayoutParams(new Gallery.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
		  FrameLayout.LayoutParams.WRAP_CONTENT));
		
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int)(width * 0.75), (int)(height * 0.17));
		params.setMargins((int)(width * 0.125), (int)(height * 0.01), (int)(width * 0.875), (int)(height * 0.18));
		
		params.gravity = Gravity.TOP;
		
		//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
		
		animview.setLayoutParams(params);
		
		thumbsUp.setLayoutParams(new Gallery.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				  FrameLayout.LayoutParams.WRAP_CONTENT));
				
				FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams((int)(width * 0.50), (int)(height * 0.28));
				params2.setMargins((int)(width * 0.25), (int)(height * 0.20), (int)(width * 0.75), (int)(height * 0.48));
				
				params2.gravity = Gravity.TOP;
				
				//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
				
				thumbsUp.setLayoutParams(params2);
				
			thumbsDown.setLayoutParams(new Gallery.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT));
						
					FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams((int)(width * 0.50), (int)(height * 0.28));
					params3.setMargins((int)(width * 0.25), (int)(height * 0.20), (int)(width * 0.75), (int)(height * 0.48));
						
					params3.gravity = Gravity.TOP;
						
						//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
						
					thumbsDown.setLayoutParams(params3);
				
		
		
        mView = new MyView(this, width, height, level, maxlevel, sfx);
        
        myLayout.addView(mView);
        
        myLayout.addView(animview);
        
        myLayout.addView(thumbsUp);
        
        myLayout.addView(thumbsDown);
        
        setContentView(myLayout);
                       
    }
    
    //public void startGame () {
    	
    //	myLayout.addView(mView);
    	
   // }

    public void setupBouncingThumb (int upordown) {
    	
    	TranslateAnimation transAnimation = new TranslateAnimation (0f, 0f, (float) ((-1)*(height * 0.05)), (float)(height * 0.05));
    	
    	transAnimation.setDuration(400);
        
        transAnimation.setRepeatMode(TranslateAnimation.REVERSE);
        
        transAnimation.setRepeatCount(10000);
        
        transAnimation.setFillEnabled(true);
        
        transAnimation.setFillAfter(true);
        
        switch (upordown) {
        
        	case 1 : 
        		
        		thumbsUp.setBackgroundResource(R.drawable.thumbsup);
        		
        		thumbsUp.startAnimation(transAnimation);
        	
        	break;
        	
        	case 2 : 
        		
        		thumbsDown.setBackgroundResource(R.drawable.thumbsdown);
        		
        		thumbsDown.startAnimation(transAnimation);
        	
        	break;
        
        }
    	
    }
    
    public void threestars () {
		
    	animview.setBackgroundResource(R.anim.threestaranim);
    	
    	starAnimation = (AnimationDrawable) animview.getBackground();

    	//starAnimation.setBounds((int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14));

    	starAnimation.start();
    	
    }
    
    public void twostars () {
		  	
    	animview.setBackgroundResource(R.anim.twostaranim);
		
		starAnimation = (AnimationDrawable) animview.getBackground();
		
		//starAnimation.setBounds((int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14));
		
		starAnimation.start();
    	
    }

    public void onestar () {
		
    	animview.setBackgroundResource(R.anim.onestaranim);
    			
		starAnimation = (AnimationDrawable) animview.getBackground();
		
		//starAnimation.setBounds((int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14));
		
		starAnimation.start();
	
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onPause() {
    	
    	super.onPause();
    	
    	uiHelper.onPause();
    	
    	Log.d("Log:", "Pause");
    	
    	mView.gamepaused = true;
    	
    	try {
    	
    	//stopService(i);
			
		//musicStopped = true;
		
    	}
    	
    	catch (Exception e) {
    		    		
    	}
    	
    }
    
    public void onResume() {
    	    	
    	super.onResume();
    	
    	uiHelper.onResume();
    	
    	Log.d("Log:", "Resume");
    	
    	mView.gamepaused = false;
    	
    	mView.myHandler.post(mView.myRunnable);
    	
    }
    
    public void onBackPressed () {
    	
    	mView.gamepaused = true;
    	
    	mView.cleanUp();
    	
    	BackgroundMusicService.backbuttonflag = true;
    	
    	//mView = null;
    	
    	//System.gc();
    	
    	//myClick.release();
    	
    	finish();
    	
    }
    
    public void nextLevel (Intent tempI) {
    	
    	mView.cleanUp();
    	
    	finish();
    	
    	startActivity(tempI);
    	
    }
    
    public void myRecreate (Intent tempI) {
    	    	
    	mView.cleanUp();
    	    	
    	finish();
    	
    	startActivity(tempI);
    	
    }
    
 // This function will invoke the Feed Dialog to post to a user's Timeline and News Feed
    // It will attempt to use the Facebook Native Share dialog
    // If that's not supported we'll fall back to the web based dialog.

    public void shareBtn (int level) {
    
    //GraphUser currentFBUser = getCurrentFBUser();

    // This first parameter is used for deep linking so that anyone who clicks the link will start smashing this user
    // who sent the post
    String link = "https://play.google.com/store/apps/details?id=com.projectgrapevine.bananabill&hl=en";
    //if (currentFBUser != null) {
     //   link += currentFBUser.getId();
    //}

    // Define the other parameters
    String name = "I just beat level " + level + " in Banana Bill!";
    String caption = "Can you beat my high score in Arcade mode?";
    String description = "Saved Bill again!";
    String picture = "http://www.nickjwill.com/bbbutton.png";

    if (FacebookDialog.canPresentShareDialog(this, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {

        // Create the Native Share dialog
        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
        .setLink(link)
        .setName(name)
        .setCaption(caption)
        .setPicture(picture)
        .build();

        // Show the Native Share dialog
        uiHelper.trackPendingDialogCall(shareDialog.present());
    } else {

        // Prepare the web dialog parameters
        Bundle params = new Bundle();
        params.putString("link", link);
        params.putString("name", caption);
        params.putString("caption", caption);
        params.putString("description", description);
        params.putString("picture", picture);
    }

        // Show FBDialog without a notification bar
        //showDialogWithoutNotificationBar("feed", params);
    }

    private boolean isAppForeground() {

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = mActivityManager
                .getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            RunningAppProcessInfo info = i.next();

            if (info.uid == getApplicationInfo().uid && info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) 
                {
                    return true;
               }
           }
        return false;
    }
    
    
 	
 	public void onStop () {
 		
 		super.onStop();

 		uiHelper.onStop();
 		
 		FlurryAgent.onEndSession(this);
 		
    	Log.d("Log:", "Stop");
 		/*		
 		if (!isAppForeground()) 
 			
 			{*/
 			
    	if (!isFinishing()) {
    	
 			try {stopService(i);} catch (Exception e) {}
 			
    	}
 			/*musicStopped = true;
 			
 			}
 		*/
 	}
 	

	public void onSaveInstanceState(Bundle outState) {
		
		uiHelper.onSaveInstanceState(outState);
		
	}
 	
 	public void onStart () {
 		
 		super.onStart();
 		
 		FlurryAgent.onStartSession(this, "4WSGV3PW4YTZPQDD7X8Q");
 		
 	}
 	
     public void onRestart() {
     	
     	super.onRestart();
     	
    	Log.d("Log:", "Restart");
     	
 		if (!BackgroundMusicService.isPlaying) {
 			
 			try {startService(i);} catch (Exception e) {}
 		
 			musicStopped = false;
 			
 		}
     	
     }


}

class MyView extends View {
	
	boolean tutorialToggle = false;
	
	int arcadehighscore = 0;
	
	String data, fname, temp; //temporarily stores string value of high score variable
	
	int squeak, eat, win, lose, glueSound, splash, rhino, rhinocharging, click, metal;
	
	SoundPool mySoundpool;
	
	boolean soundpoolloaded;
	
	int tutorialScreenCounter = 1;
	
	static int EAT = 1;
	
	static int SQUEAK = 2;
	
	static int WIN = 3;
	
	static int LOSE = 4;
	
	static int SPLASH = 5;
	
	static int RHINO = 6;
	
	static int RHINOCHARGING = 7;
	
	int moveIncrement = 5;
	
	int moveIncBuffer = 0; //to temporarily store moveIncrement when it has been modified by something and needs to be reset (like glue for example)
	
	final int UPLEFT = 1;
	
	final int DOWNLEFT = 2;
	
	final int UPRIGHT = 3;
	
	final int DOWNRIGHT = 4;
	
	int mouseRadius;
	
	int animCounter = 0;
	
	int runCounter = 0;//counts the number of times the run function goes (20 per second)
	
	private Drawable clock, tutorial, playbtn, mouseDownLeft1, mouseDownRight1, mouseUpLeft1, mouseUpRight1, mouseDownLeft2, mouseDownRight2, mouseUpLeft2, mouseUpRight2, myApple, myMonsterLeft, myMonsterRight, myBrick, myVine, Background, GlueTexture, WaterTexture, AttilaRhinoRight1, AttilaRhinoLeft1, AttilaRhinoRight2, AttilaRhinoLeft2, AttilaRhinoRight3, AttilaRhinoLeft3, AttilaShadowLeft, AttilaShadowRight, billlifeicon, nostar, onestar, twostar, threestar, retrybutton, menubutton,forwardbutton, leaderboard, fbshare, winscreen, losescreen, pausebtn;
	
	private Drawable tut1, tut2, tut3, tut4, tut5, tut6, tut7, tut8, tut9, tut10, tut11, tut12;
	
	private Drawable gold, silver, bronze;
	
	private Drawable billshare;
	
	//private ImageView animview;
	
	private Runnable soundPoolLoader, playMetal, playEat, playSqueak, playWin, playLose, playGlueSound, playSplash, playRhino, playRhinoCharging;
	
	Typeface pressStart;
	
	private Paint myPaint, topbarpaint;
	
	boolean gameover = false;
	
	boolean sfx = true;
	
	boolean glue = false;
	
	int maxlevel;
		
	int x, y;   
	
	int downX, downY, upX, upY;
	
	int appleX, appleY;
	
	int spiderDirection = DOWNRIGHT;
	
	int width, height;
	
	int score, remainingTime, scoreAcc; //score counts bananas - scoreAcc counts points
	
	int glueSoundStart;
	
	float t;
	
	boolean appleEaten, kill, saved;
	
	//int score;
	
	int lives;
	
	int immunity;
	
	int billyellow, topbargreen;//color
	
	boolean valid;
		
	boolean userwon = false;

	boolean gamepaused = false;
	
	boolean timeout = false;//timeout after fame ends before accepting input
	
	int [] highscores = new int[32];
	
	int [] besttimes = new int[32];
	
	Context context;
	
	Level[] levelArray = new Level[33];
	
	int currentLevel = 0;
	
	Monster[] monsterArray = new Monster[5];
	
	Obstacle[] obstacleArray = new Obstacle[3];
	
	Rhino Attila;
	
	Vibrator myVibrate;
	
	GamePlay GPtemp;
	
	HandlerThread mediaThread;
	
	final Handler mediaHandler;
	
	Handler myHandler;
	
	Runnable myRunnable;
	
	boolean arcademode = false;
	
	public MyView (Context c, int w, int h, int l, int m, boolean s) { //maybe pass level here
		
		super(c);
		
		x = 0;
		
		y = (int) (width * 0.15);
		
		this.sfx = s;
		
		for (int i = 0; i < 32; i++) {
			
			highscores[i] = 0;
			
			besttimes[i] = 0;
			
		}
		
		Resources res = this.getResources();
				
		GPtemp = (GamePlay) this.getContext();
		
		if (l == 0) tutorialToggle = true;
		
		soundpoolloaded = false;
		
		gamepaused = false;
		
		mediaThread = new HandlerThread("MediaThread");
		
		mediaThread.start();
		
		Looper looper = mediaThread.getLooper();
		
		mediaHandler = new Handler(looper);
		
		context = c;
		
		mySoundpool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		
		soundPoolLoader = new Runnable () {
			
			public void run () {
				
				squeak = mySoundpool.load(context,  R.raw.squeak, 1);
				
				//squeak = MediaPlayer.create(context, R.raw.squeak);

				//squeak.setVolume (0.75f, 0.75f);
				
				rhino = mySoundpool.load(context,  R.raw.rhino, 1);
				
				//rhino.setVolume (1f, 1f);
				
				eat = mySoundpool.load(context,  R.raw.eat, 1);
				
				win = mySoundpool.load(context,  R.raw.win, 1);
				
				lose = mySoundpool.load(context,  R.raw.lose, 1);
				
				glueSound = mySoundpool.load(context,  R.raw.glue, 1);
				
				splash = mySoundpool.load(context,  R.raw.splash, 1);
				
				rhinocharging = mySoundpool.load(context,  R.raw.rhinocharge, 1);
				
				click = mySoundpool.load(context,  R.raw.click, 1);
				
				metal = mySoundpool.load(context,  R.raw.metal, 1);
				
				if (sfx) mySoundpool.play(click, 1, 1, 1, 0, 1.0f);
				
				soundpoolloaded = true;
				
			}
			
		};
		
		mediaHandler.post(soundPoolLoader);
		
		myVibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		
		data = null;
		
		temp = null;
		
		fname = "Android/data/com.projectgrapevine.bananabill";
		
		currentLevel = l;
		
		maxlevel = m;
		
		this.width = w;
		
		this.height = h;
		
		scoreAcc = -10;
		
		moveIncrement = (int) (((double) width / 240) * 5); 
					
		glueSoundStart = 0;
		
		levelArray[0] = new Level(width, height, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[1] = new Level(width, height, 1, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[2] = new Level(width, height, 2, 10, 1, 110, 160, 20, 160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[3] = new Level(width, height, 2, 10, 2, 75, 100, 25, 150, 145, 100, 25, 150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[4] = new Level(width, height, 2, 10, 2, 110, 190, 20, 130, 110, 0, 20, 130, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[5] = new Level(width, height, 2, 10, 2, 0, 140, 90, 40, 150, 140, 100, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[6] = new Level(width, height, 2, 10, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 200, 190, 40, 140);
		
		levelArray[7] = new Level(width, height, 3, 10, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 200, 190, 40, 140);
		
		levelArray[8] = new Level(width, height, 2, 10, 3, 140, 0, 100, 40, 0, 140, 100, 40, 140, 230, 100, 40, 0, 0, 0, 0, 140, 270, 100, 50);
		
		levelArray[9] = new Level(width, height, 3, 10, 2, 0, 100, 100, 40, 190, 140, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[10] = new Level(width, height, 2, 5, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 100, 0, 40, 320, 0, 0, 0, 0);
		
		levelArray[11] = new Level(width, height, 2, 10, 2, 110, 190, 20, 130, 110, 0, 20, 130, 0, 0, 0, 0, 0, 0, 0, 0, 130, 0, 110, 320);
		
		levelArray[12] = new Level(width, height, 2, 10, 2, 75, 0, 25, 140, 160, 180, 25, 140, 0, 0, 0, 0, 0, 140, 240, 40, 0, 0, 0, 0);
		
		levelArray[13] = new Level(width, height, 4, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[14] = new Level(width, height, 2, 10, 2, 0, 170, 140, 40, 140, 70, 40, 140, 0, 0, 0, 0, 140, 20, 40, 50, 0, 0, 0, 0);
		
		levelArray[15] = new Level(width, height, 2, 10, 2, 0, 90, 200, 40, 40, 220, 200, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[16] = new Level(width, height, 3, 10, 2, 0, 90, 200, 40, 40, 220, 200, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[17] = new Level(width, height, 2, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[18] = new Level(width, height, 3, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[19] = new Level(width, height, 4, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[20] = new Level(width, height, 3, 10, 1, 110, 160, 20, 160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[21] = new Level(width, height, 4, 10, 1, 110, 160, 20, 160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[22] = new Level(width, height, 3, 10, 2, 75, 100, 25, 150, 145, 100, 25, 150, 0, 0, 0, 0, 100, 100, 45, 150, 0, 0, 0, 0);
		
		levelArray[23] = new Level(width, height, 3, 10, 2, 110, 190, 20, 130, 110, 0, 20, 130, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[24] = new Level(width, height, 3, 10, 2, 0, 140, 90, 40, 150, 140, 100, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[25] = new Level(width, height, 3, 10, 3, 140, 0, 100, 40, 0, 140, 100, 40, 140, 230, 100, 40, 0, 0, 0, 0, 140, 270, 100, 50);
		
		levelArray[26] = new Level(width, height, 4, 10, 2, 0, 100, 100, 40, 190, 140, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[27] = new Level(width, height, 3, 5, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 100, 0, 40, 320, 0, 0, 0, 0);
		
		levelArray[28] = new Level(width, height, 3, 10, 2, 110, 190, 20, 130, 110, 0, 20, 130, 0, 0, 0, 0, 110, 130, 20, 60, 130, 0, 110, 320);
		
		levelArray[29] = new Level(width, height, 3, 10, 2, 0, 170, 140, 40, 140, 70, 40, 140, 0, 0, 0, 0, 140, 20, 40, 50, 0, 0, 0, 0);
		
		levelArray[30] = new Level(width, height, 4, 10, 2, 0, 90, 200, 40, 40, 220, 200, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[31] = new Level(width, height, 4, 10, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 200, 190, 40, 140);
		
		levelArray[32] = new Level(width, height, 5, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		//currentLevel = 0; //equates to level 1 - this will actually be selected by user based on unlocking
				
		mouseRadius = (int) (width * 0.05);
		
		for (int i = 0; i < levelArray[currentLevel].getNumberOfObstacles(); i++) {
			
			obstacleArray[i] = new Obstacle(levelArray[currentLevel].getObstacleX(i), levelArray[currentLevel].getObstacleY(i), levelArray[currentLevel].getObstacleWidth(i), levelArray[currentLevel].getObstacleHeight(i), mouseRadius, width, height);
			
		}
		
		for (int i = 0; i < levelArray[currentLevel].getNumberOfEnemies(); i++) {
			
			monsterArray[i] = new Monster(width, height, currentLevel);
			
		}
		
		Attila = new Rhino (width, height, currentLevel, 0);
		
		appleEaten = true;
		
		kill = false;
		
		saved = false;
		
		score = -1;
		
		remainingTime = 90;
		
		t = 90;
		
		lives = 4;
		
		immunity = 50;
		
		arcademode = ((GamePlay)c).arcademode;
        
		tutorial = res.getDrawable(R.drawable.tutorial);
		
		mouseDownLeft1 = res.getDrawable(R.drawable.mousedownleft1);
		
		mouseUpLeft1 = res.getDrawable(R.drawable.mouseupleft1);
		
		mouseDownRight1 = res.getDrawable(R.drawable.mousedownright1);
		
		mouseUpRight1 = res.getDrawable(R.drawable.mouseupright1);
		
		mouseDownLeft2 = res.getDrawable(R.drawable.mousedownleft2);
		
		mouseUpLeft2 = res.getDrawable(R.drawable.mouseupleft2);
		
		mouseDownRight2 = res.getDrawable(R.drawable.mousedownright2);
		
		mouseUpRight2 = res.getDrawable(R.drawable.mouseupright2);
		
		myApple = res.getDrawable(R.drawable.apple);
		
		myMonsterLeft = res.getDrawable(R.drawable.monsterleft);
		
		myMonsterRight = res.getDrawable(R.drawable.monsterright);
		
		myBrick = res.getDrawable(R.drawable.block);
		
		myVine = res.getDrawable(R.drawable.vine);
		
		Background = res.getDrawable(R.drawable.bg);
		
		GlueTexture = res.getDrawable(R.drawable.gluetexture);
		
		WaterTexture = res.getDrawable(R.drawable.watertexture);
		
		AttilaRhinoRight1 = res.getDrawable(R.drawable.attila1right);
		
		AttilaRhinoRight2 = res.getDrawable(R.drawable.attila2right);
		
		AttilaRhinoRight3 = res.getDrawable(R.drawable.attila3right);
		
		AttilaRhinoLeft1 = res.getDrawable(R.drawable.attila1left);
		
		AttilaRhinoLeft2 = res.getDrawable(R.drawable.attila2left);
		
		AttilaRhinoLeft3 = res.getDrawable(R.drawable.attila3left);
	
		billlifeicon = res.getDrawable(R.drawable.billlifeicon);
		
		AttilaShadowRight = res.getDrawable(R.drawable.rhinoshadowright);
		
		AttilaShadowLeft = res.getDrawable(R.drawable.rhinoshadowleft);
		
		nostar = res.getDrawable(R.drawable.nostars); 
		
		onestar = res.getDrawable(R.drawable.onestar);
		
		twostar = res.getDrawable(R.drawable.twostars); 
		
		threestar = res.getDrawable(R.drawable.threestars);
		
		retrybutton = res.getDrawable(R.drawable.retrybutton);
		
		menubutton = res.getDrawable(R.drawable.menubutton);
		
		forwardbutton = res.getDrawable(R.drawable.forwardbutton);
		
		leaderboard = res.getDrawable(R.drawable.leaderboard);
		
		fbshare = res.getDrawable(R.drawable.fbshare);
		
		winscreen = res.getDrawable(R.drawable.winscreen);
		
		losescreen = res.getDrawable(R.drawable.losescreen);
		
		pausebtn = res.getDrawable(R.drawable.pause);
		
		playbtn = res.getDrawable(R.drawable.play);
		
		clock = res.getDrawable(R.drawable.clock);
		
		tut1 = res.getDrawable(R.drawable.tut1);
		
		tut2 = res.getDrawable(R.drawable.tut2);
		
		tut3 = res.getDrawable(R.drawable.tut3);
		
		tut4 = res.getDrawable(R.drawable.tut4);
		
		tut5 = res.getDrawable(R.drawable.tut5);
		
		tut6 = res.getDrawable(R.drawable.tut6);
		
		tut7 = res.getDrawable(R.drawable.tut7);
		
		tut8 = res.getDrawable(R.drawable.tut8);
		
		tut9 = res.getDrawable(R.drawable.tut9);
		
		tut10 = res.getDrawable(R.drawable.tut10);
		
		tut11 = res.getDrawable(R.drawable.tut11);
		
		gold = res.getDrawable(R.drawable.gold);
		
		silver = res.getDrawable(R.drawable.silver);
		
		bronze = res.getDrawable(R.drawable.bronze);
		
		billshare = res.getDrawable(R.drawable.billshare);
		
		pressStart = Typeface.createFromAsset(c.getAssets(), "PrStart.ttf");
		
		myPaint = new Paint();
		
		topbarpaint = new Paint();
		
		topbarpaint.setStyle(Paint.Style.FILL);
		
		topbargreen = res.getColor(R.color.topbargreen);
		
		topbarpaint.setColor(topbargreen);
		
		billyellow = res.getColor(R.color.billyellow);
		
        myPaint.setColor(billyellow);
        
        myPaint.setTextSize((float) ((0.0353) * width));
        
        myPaint.setAntiAlias(true);
        myPaint.setFakeBoldText(true);
        myPaint.setTypeface(pressStart);
        myPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextAlign(Paint.Align.LEFT);
		
		playSqueak = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(squeak, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playWin = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(win, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playLose = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(lose, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playEat = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(eat, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playRhino = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(rhino, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playRhinoCharging = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(rhinocharging, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playSqueak = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(squeak, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playGlueSound = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(glueSound, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playSplash = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(splash, 1, 1, 1, 0, 1.0f);
						
		}};
		
		playMetal = new Runnable () { public void run () {
			
			if (sfx && soundpoolloaded) mySoundpool.play(metal, 1, 1, 1, 0, 1.0f);

		}};
		
		
		
		
		myHandler = new Handler();
		
		myRunnable = new Runnable(){@Override public void run() {
			
//if (lives < 1) removeSelf();
			
			runCounter++;
			
			if (tutorialToggle) gamepaused = true; 
			
			t -= 0.05;
			
			remainingTime = (int) t;
						
			if (immunity > 0) immunity--;
			
			for (int i = 0; i < levelArray[currentLevel].getNumberOfObstacles(); i++) {
			
				if (obstacleArray[i].collisionCheck(x, y, spiderDirection) != 0) {
				
					spiderDirection = obstacleArray[i].collisionCheck(x, y, spiderDirection);
				
				}
			
			}
						
			if (x > levelArray[currentLevel].getGlueX() && x < (levelArray[currentLevel].getGlueX() + levelArray[currentLevel].getGlueWidth()) && y > levelArray[currentLevel].getGlueY() && y < (levelArray[currentLevel].getGlueY() + levelArray[currentLevel].getGlueHeight())) {//checking if in glue trap
			
				glue = true;
				
			}
			
			else glue = false;
				
			if (glue == true) {
				
				moveIncBuffer = moveIncrement;
				
				moveIncrement = moveIncrement / 3;
				
				if (glueSoundStart == 0 || glueSoundStart > (remainingTime + 3)) {
					
					mediaHandler.post(playGlueSound);
				
					glueSoundStart = remainingTime;
				
				}
				
			}
			
			switch (spiderDirection) {
		
				case UPLEFT : 
			
					x-=moveIncrement;
		
					y-=moveIncrement;
			
				break;
				
				case UPRIGHT : 
					
					x+=moveIncrement;
		
					y-=moveIncrement;
			
				break;
				
				case DOWNLEFT : 
					
					x-=moveIncrement;
		
					y+=moveIncrement;
			
				break;
				
				case DOWNRIGHT : 
					
					x+=moveIncrement;
		
					y+=moveIncrement;
			
				break;
					
			}
			
			if (glue) {
				
				moveIncrement = moveIncBuffer;
				
			}
			
			if (x < mouseRadius && spiderDirection == UPLEFT) {
				
				spiderDirection = UPRIGHT;
				
			}
			
			if (x < mouseRadius && spiderDirection == DOWNLEFT) {
				
				spiderDirection = DOWNRIGHT;
				
			}
			
			if (x > (width - mouseRadius) && spiderDirection == UPRIGHT) {
				
				spiderDirection = UPLEFT;
				
			}
			
			if (x > (width - mouseRadius) && spiderDirection == DOWNRIGHT) {
				
				spiderDirection = DOWNLEFT;
				
			}
			
			if (y < (mouseRadius + (width * 0.1)) && spiderDirection == UPLEFT) {
				
				spiderDirection = DOWNLEFT;
				
			}
			
			if (y < (mouseRadius + (width * 0.1)) && spiderDirection == UPRIGHT) {
				
				spiderDirection = DOWNRIGHT;
				
			}
			
			if (y > (height - mouseRadius) && spiderDirection == DOWNRIGHT) {
				
				spiderDirection = UPRIGHT;
				
			}
			
			if (y > (height - mouseRadius) && spiderDirection == DOWNLEFT) {
				
				spiderDirection = UPLEFT;
				
			}
			
			if (appleEaten == true) {
					
				if (sfx) mediaHandler.post(playEat);
					
				switch (lives) {
				
					case 1 : scoreAcc+=1;
					
					break;
					
					case 2 : scoreAcc+=3;
					
					break;
					
					case 3 : scoreAcc+=7;
					
					break;
					
					case 4 : scoreAcc+=10;
					
					break;
					
				}//maybe put point above head animation here
								
				do {
				
				valid = true;//this variable will determine whether or not random spot is on obstacle
				
				appleX = (int) ((Math.random() * (width - (mouseRadius*2))) + mouseRadius); //never mind --> add 9 was before the last bracket previously
				
				appleY = (int) ((Math.random() * ((height - (width*0.1)) - (mouseRadius*2))) + (mouseRadius + (width * 0.1))); //-18 was - 9
				
				for (int i = 0; i < levelArray[currentLevel].getNumberOfObstacles(); i++) {
				
					if (appleX > obstacleArray[i].getX() && appleX < (obstacleArray[i].getX() + obstacleArray[i].getWidth())) {
					
						if (appleY > obstacleArray[i].getY() && appleY < (obstacleArray[i].getY() + obstacleArray[i].getHeight())) {
						
							valid = false;
						
						}
					
					}
				
				}
				
				if (appleX > levelArray[currentLevel].getWaterX() && appleX < (levelArray[currentLevel].getWaterX() + levelArray[currentLevel].getWaterWidth()) && appleY > levelArray[currentLevel].getWaterY() && appleY < (levelArray[currentLevel].getWaterY() + levelArray[currentLevel].getWaterHeight())) valid = false;//checking if in glue trap
				
				} while (valid == false);
				
				appleEaten = false; 
				
				score++;
			
			}	
			
			if ((x + mouseRadius) > (appleX - (mouseRadius * 0.5)) && (x - mouseRadius) < (appleX + mouseRadius)) { //same column
				
				if ((y + mouseRadius) > (appleY - (mouseRadius * 0.5)) && (y - mouseRadius) < (appleY + mouseRadius)) { //same row
					
					appleEaten = true;
					
				}
			
			}
							
			kill = Attila.move(x, y);
			
			int tempNOE = levelArray[currentLevel].getNumberOfEnemies();
			
			if (arcademode) {
				
				if (remainingTime > 80) tempNOE = 1;
				
				else if (remainingTime > 70) tempNOE = 2;
				
				else if (remainingTime > 60) tempNOE = 3;
				
				else if (remainingTime > 50) tempNOE = 4;
				
				else tempNOE = 5;
				
			}
			
			for (int i = 0; i < tempNOE; i++) {
				
				if (!kill) kill = monsterArray[i].move(x, y);
				
				else monsterArray[i].move(x, y);
				
				//this part kills jacques if hes in water
				
				if (x > levelArray[currentLevel].getWaterX() && x < (levelArray[currentLevel].getWaterX() + levelArray[currentLevel].getWaterWidth()) && y > levelArray[currentLevel].getWaterY() && y < (levelArray[currentLevel].getWaterY() + levelArray[currentLevel].getWaterHeight())) {//checking if in glue trap
					
					kill = true;
					
					if (sfx == true) {
						
						mediaHandler.post(playSplash);
						
					}
					
				}
				
				//this part kills jacques if hes in water
				
				if (kill == true && immunity == 0) {
										
					if (sfx) mediaHandler.post(playSqueak);
					
					if (android.os.Build.VERSION.SDK_INT >= 13) { 
					
						if (myVibrate.hasVibrator()) myVibrate.vibrate(300);
						
					}
					
					else myVibrate.vibrate(300);
					
					lives--;
					
					x = 0;
					
					y = (int) (width * 0.15);
					
					kill = false;
					
					immunity = 50;
					
				}		
				
				else kill = false;
								
			}
			
			if ((score >= levelArray[currentLevel].getApplesRequired() && !arcademode) || lives < 1) gameover = true;
		
			postInvalidate();
			
			if (!gamepaused && !gameover) myHandler.postDelayed(myRunnable, 50);
			
		}
		
		};//end of timertask
		
		//myHandler.post(myRunnable);
		
		//myTimer.scheduleAtFixedRate (myTimerTask, 50, 50);
            
	}
	@Override
	protected void onDraw (Canvas c) {
		
		if (!tutorialToggle) animCounter++;
		
		if (animCounter % 400 == 0) {
			
			mediaHandler.post(playRhino);
			
			mediaHandler.post(playRhinoCharging);
			
			if (animCounter % 800 == 0) Attila.getSet(width,height,currentLevel, 2);
			
			else Attila.getSet(width,height,currentLevel, 1);
			
		}
		
		boolean highscore = false;
		
		if (remainingTime == 0) {
			
			gameover = true;
			
			lives = 0;
			
		}
		
		if (!gameover) {
		
		Background.setBounds (0, (int) (width * 0.0), width, height);
		
		Background.draw(c);
		
		if (currentLevel == 0 && animCounter < 100) {
			
			tutorial.setBounds((int) (width * 0.20), (int) (width * 0.33), (int) (width * 0.80), (int) (width * 0.93));
			
			tutorial.draw(c);
			
		}
		
		GlueTexture.setBounds (levelArray[currentLevel].getGlueX(), levelArray[currentLevel].getGlueY(), (levelArray[currentLevel].getGlueX() + levelArray[currentLevel].getGlueWidth()), (levelArray[currentLevel].getGlueY() + levelArray[currentLevel].getGlueHeight()));
		
		GlueTexture.draw(c);
		
		WaterTexture.setBounds (levelArray[currentLevel].getWaterX(), levelArray[currentLevel].getWaterY(), (levelArray[currentLevel].getWaterX() + levelArray[currentLevel].getWaterWidth()), (levelArray[currentLevel].getWaterY() + levelArray[currentLevel].getWaterHeight()));
		
		WaterTexture.draw(c);
		
		for (int i = 0; i < levelArray[currentLevel].getNumberOfObstacles(); i++) {
		
			myBrick.setBounds(obstacleArray[i].getX(), obstacleArray[i].getY(), obstacleArray[i].getX() + obstacleArray[i].getWidth(), obstacleArray[i].getY() + obstacleArray[i].getHeight());
			
			myVine.setBounds((obstacleArray[i].getX() - (int) (((double) obstacleArray[i].getWidth()/2))), obstacleArray[i].getY(), obstacleArray[i].getX() + (int) (((double) obstacleArray[i].getWidth() * 1.5)), obstacleArray[i].getY() + obstacleArray[i].getHeight());
			
			if (obstacleArray[i].getHeight() > obstacleArray[i].getWidth()) myVine.draw(c);
			
			else myBrick.draw(c);
			
		}
		
        switch (spiderDirection) {
        
        case DOWNLEFT : 
        
        mouseDownLeft1.setBounds(x-mouseRadius, y-mouseRadius, x + mouseRadius, y + mouseRadius);
		
        mouseDownLeft2.setBounds(x-mouseRadius, y-mouseRadius, x + mouseRadius, y + mouseRadius);
        
        if (immunity % 2 == 0 && animCounter % 10 < 5) mouseDownLeft1.draw(c);
        
        else if (immunity % 2 == 0 && animCounter % 10 > 4) mouseDownLeft2.draw(c);
        
        break;
        
        case DOWNRIGHT : 
            
        	mouseDownRight1.setBounds(x-mouseRadius, y-mouseRadius, x + mouseRadius, y + mouseRadius);
    		
        	mouseDownRight2.setBounds(x-mouseRadius, y-mouseRadius, x + mouseRadius, y + mouseRadius);
        	
    		if (immunity % 2 == 0 && animCounter % 10 < 5) mouseDownRight1.draw(c);
            
            else if (immunity % 2 == 0 && animCounter % 10 > 4) mouseDownRight2.draw(c);
                        
            break;
            
        case UPLEFT : 
            
        	mouseUpLeft1.setBounds(x-mouseRadius, y-mouseRadius, x + mouseRadius, y + mouseRadius);
    		
        	mouseUpLeft2.setBounds(x-mouseRadius, y-mouseRadius, x + mouseRadius, y + mouseRadius);
        	
    		if (immunity % 2 == 0 && animCounter % 10 < 5) mouseUpLeft1.draw(c);
            
            else if (immunity % 2 == 0 && animCounter % 10 > 4) mouseUpLeft2.draw(c);
            
            
            break;
            
        case UPRIGHT : 
            
        	mouseUpRight1.setBounds(x-mouseRadius, y-mouseRadius, x + mouseRadius, y + mouseRadius);
    		
        	mouseUpRight2.setBounds(x-mouseRadius, y-mouseRadius, x + mouseRadius, y + mouseRadius);
        	
    		if (immunity % 2 == 0 && animCounter % 10 < 5) mouseUpRight1.draw(c);
            
            else if (immunity % 2 == 0 && animCounter % 10 > 4) mouseUpRight2.draw(c);
                        
            break;
            
        }
        
        myApple.setBounds((appleX - mouseRadius), (appleY - mouseRadius), (appleX + mouseRadius), (appleY + mouseRadius));
		
        myApple.draw(c);
        
        AttilaRhinoRight1.setBounds(Attila.getX() - (mouseRadius * 3), Attila.getY() - (mouseRadius), Attila.getX() + (mouseRadius * 3), Attila.getY() + (mouseRadius));
        
        AttilaRhinoRight2.setBounds(Attila.getX() - (mouseRadius * 3), Attila.getY() - (mouseRadius), Attila.getX() + (mouseRadius * 3), Attila.getY() + (mouseRadius));
        
        AttilaRhinoRight3.setBounds(Attila.getX() - (mouseRadius * 3), Attila.getY() - (mouseRadius), Attila.getX() + (mouseRadius * 3), Attila.getY() + (mouseRadius));
        
        AttilaShadowRight.setBounds(Attila.getX() - (mouseRadius * 3), Attila.getY() + (mouseRadius), Attila.getX() + (mouseRadius * 3), Attila.getY() + (mouseRadius * 3));
        
        AttilaRhinoLeft1.setBounds(Attila.getX() - (mouseRadius * 3), Attila.getY() - (mouseRadius), Attila.getX() + (mouseRadius * 3), Attila.getY() + (mouseRadius));
        
        AttilaRhinoLeft2.setBounds(Attila.getX() - (mouseRadius * 3), Attila.getY() - (mouseRadius), Attila.getX() + (mouseRadius * 3), Attila.getY() + (mouseRadius));
        
        AttilaRhinoLeft3.setBounds(Attila.getX() - (mouseRadius * 3), Attila.getY() - (mouseRadius), Attila.getX() + (mouseRadius * 3), Attila.getY() + (mouseRadius));
        
        AttilaShadowLeft.setBounds(Attila.getX() - (mouseRadius * 3), Attila.getY() + (mouseRadius), Attila.getX() + (mouseRadius * 3), Attila.getY() + (mouseRadius * 3));
        
        if (Attila.getDir() == 2) { 
        	
        	if (runCounter % 6 == 0 || runCounter % 6 == 1) AttilaRhinoRight1.draw(c);
        	
        	else if (runCounter % 6 == 2 || runCounter % 6 == 3) AttilaRhinoRight2.draw(c);
        	
        	else if (runCounter % 6 == 4 || runCounter % 6 == 5) AttilaRhinoRight3.draw(c);
        	
        	if (runCounter % 6 == 0 || runCounter % 6 == 1) AttilaRhinoRight1.draw(c);
        	
        	if (Attila.shadow) AttilaShadowRight.draw(c);
        	
        }
        
        else if (Attila.getDir() == 1) {
        	
        	if (runCounter % 6 == 0 || runCounter % 6 == 1) AttilaRhinoLeft1.draw(c);
        	
        	else if (runCounter % 6 == 2 || runCounter % 6 == 3) AttilaRhinoLeft2.draw(c);
        	
        	else if (runCounter % 6 == 4 || runCounter % 6 == 5) AttilaRhinoLeft3.draw(c);
        	
        	if (Attila.shadow) AttilaShadowLeft.draw(c);
        	
        }
        
        //AttilaRhino.setBounds(100, 100, 150, 150);
        
        //AttilaRhino.draw(c);
        
        //myMonsterLeft.setBounds(150, 150, 200, 200);
		
        //myMonsterLeft.draw(c);

		int tempNOE = levelArray[currentLevel].getNumberOfEnemies();
		
		if (arcademode) {
			
			if (remainingTime > 80) tempNOE = 1;
			
			else if (remainingTime > 70) tempNOE = 2;
			
			else if (remainingTime > 60) tempNOE = 3;
			
			else if (remainingTime > 50) tempNOE = 4;
			
			else tempNOE = 5;
			
		}
		       
        
        for (int i = 0;i<tempNOE;i++) {
              	
        	switch (monsterArray[i].getDirection()) {
        	
        		case UPLEFT :
        	        	
        		myMonsterLeft.setBounds(monsterArray[i].getX() - mouseRadius, monsterArray[i].getY() - mouseRadius, monsterArray[i].getX() + mouseRadius, monsterArray[i].getY() + mouseRadius);
        	
        		myMonsterLeft.draw(c);
        		
        		break;
        		
        		case DOWNLEFT :
    	        	
        			myMonsterLeft.setBounds(monsterArray[i].getX() - mouseRadius, monsterArray[i].getY() - mouseRadius, monsterArray[i].getX() + mouseRadius, monsterArray[i].getY() + mouseRadius);
            	
            		myMonsterLeft.draw(c);
            		
            	break;
            	
        		case UPRIGHT :
    	        	
        			myMonsterRight.setBounds(monsterArray[i].getX() - mouseRadius, monsterArray[i].getY() - mouseRadius, monsterArray[i].getX() + mouseRadius, monsterArray[i].getY() + mouseRadius);
            	
            		myMonsterRight.draw(c);
            		
            		break;
            		
        		case DOWNRIGHT :
    	        	
        			myMonsterRight.setBounds(monsterArray[i].getX() - mouseRadius, monsterArray[i].getY() - mouseRadius, monsterArray[i].getX() + mouseRadius, monsterArray[i].getY() + mouseRadius);
            	
            		myMonsterRight.draw(c);
            		
            		break;
                    		
        	}
        
        }
        
        //myPaint.setColor(Color.RED);
        c.drawRect((float) 0, (float) 0, (float) width, (float) (width*0.1), topbarpaint);
        
        myPaint.setColor(billyellow);
        
        myApple.setBounds((int) (width * 0.02), (int) (width * 0.02), (int) (width * 0.1), (int) (width * 0.09));
    	
    	myApple.draw(c);
        
        if (!arcademode) c.drawText(":" + score + "/" + levelArray[currentLevel].getApplesRequired(), (float) (width * 0.12), (float) (width * 0.068), myPaint);
        
        else c.drawText(":" + score, (float) (width * 0.12), (float) (width * 0.068), myPaint);
        
        myPaint.setColor(Color.WHITE);
        
        //c.drawText("LIVES:" + lives, (float) ((width * 0.77) - 25), (float) (height * 0.03), myPaint);

        //render life counter here
        
        for (int i = 1; i <= lives;i++) {
        	
        	billlifeicon.setBounds((int) (width - ((width * 0.08) * i)), (int) (height * 0.01), (int) (width - ((width * 0.08) * (i - 1))), (int) (width * 0.08));
        	
        	billlifeicon.draw(c);
        	
        }

        //c.drawText("|" + remainingTime + "|",(float) (width * 0.45), (float) (width * 0.068), myPaint);
        //no more score in story mode
        
        clock.setBounds ((int) (width * 0.015), (int) (height * 0.93), (int) (width * 0.08), (int) (height * 0.98));
        
        clock.draw(c);
        
        c.drawText("" + remainingTime + "s", (float) (width*0.10), (float) (height * 0.968), myPaint);
        //this draws timer now
        pausebtn.setBounds ((int) (width * 0.9), (int) (height * 0.945), (int) width, (int) (height * 0.985));
        
        playbtn.setBounds ((int) (width * 0.9), (int) (height * 0.93), (int) width, (int) height);
        
        if (!gamepaused) pausebtn.draw(c);
        
        else if (gamepaused) playbtn.draw(c);
        
		}//else if game is over
		
		else {
				
				Runnable timeOut = new Runnable(){@Override public void run() {
			
				timeout = true;
				
				}};
				
				if (!timeout) myHandler.postDelayed(timeOut, 1000);
				
				if (score >= levelArray[currentLevel].getApplesRequired() || ((GamePlay) this.getContext()).arcademode) {
			
				if (!saved) {//preventing multiple writes
					
				if (!((GamePlay) this.getContext()).arcademode) {//if this is not arcade mode
					
				File f = new File(Environment.getExternalStorageDirectory(), fname);
		    	
				boolean dummy = f.mkdirs();
				
				f = new File (f, "lvl.txt");
				
				File hscores = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill");
					    	
				boolean dummy2 = hscores.mkdirs();
				
				hscores = new File(hscores, "highscores.txt");
				
				File btimes = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill");
		    	
				boolean dummy3 = btimes.mkdirs();
				
				btimes = new File(btimes, "besttimes.txt");
				
				if ((currentLevel + 1) == maxlevel && maxlevel < 32) data = Integer.toString(maxlevel + 1); //increment current level to account for it being an array index (therefore one less than actual level)
				//20 prevents it from setting it any higher
				else data = Integer.toString(maxlevel);
				
		    	try {
		    	
		    		BufferedWriter buf = new BufferedWriter(new FileWriter(f));
		    		
		    		buf.write(data);
		    		
		    		buf.flush();
		    		
		    		buf.close();
		    	
		    	}
		    	
		    	catch (Exception e) {
		    		
		    		e.printStackTrace();
		    		
		    	}

				score = scoreAcc;
		    	
		    	try {
		    		
		    		BufferedReader buf = new BufferedReader(new FileReader(hscores));
		    		
		    		for (int i = 0; i < 32; i++) {
		    			
		    			highscores[i] = Integer.parseInt(buf.readLine());
		    			
		    		}
		    		
		    		buf.close();
		    		
		    	}
		    	
		    	catch (Exception e) {
		    		
		    		e.printStackTrace();
		    		
		    	}
		    			    		
		    		if (scoreAcc > highscores[currentLevel]) {
		    			
		    			highscores[currentLevel] = scoreAcc;
		    				    	
		    			highscore = true;
		    		
		    		}
		    		
		    		else highscore = false;
		    		
		    		try {
		    		
		    		BufferedWriter buf2 = new BufferedWriter(new FileWriter (hscores));
		    				    		
		    		for (int i = 0; i < 32; i++) {
		    			
		    			temp = Integer.toString(highscores[i]);
		    			
		    			buf2.write(temp);
		    			
		    			buf2.newLine();
		    		
		    		}
		    		
		    		buf2.flush();
		    		
		    		buf2.close();
		    		
		    	}
		    	
		    	catch (Exception e) {
		    		
		    		e.printStackTrace();
		    		
		    	}
		    	
		    		
		    		//
		    		
		    		
		    		try {
			    		
			    		BufferedReader buf3 = new BufferedReader(new FileReader(btimes));
			    		
			    		for (int i = 0; i < 32; i++) {
			    			
			    			besttimes[i] = Integer.parseInt(buf3.readLine());
			    			
			    		}
			    		
			    		buf3.close();
			    		
			    	}
			    	
			    	catch (Exception e) {
			    		
			    		e.printStackTrace();
			    		
			    	}
			    			    		
			    		if ((90 - remainingTime) < besttimes[currentLevel] || besttimes[currentLevel] == 0) {
			    			
			    			besttimes[currentLevel] = 90 - remainingTime;
			    				    	
			    			//besttime = true;
			    		
			    		}
			    		
			    		//else highscore = false;
			    		
			    		try {
			    		
			    		BufferedWriter buf3 = new BufferedWriter(new FileWriter (btimes));
			    				    		
			    		for (int i = 0; i < 32; i++) {
			    			
			    			temp = Integer.toString(besttimes[i]);
			    			
			    			buf3.write(temp);
			    			
			    			buf3.newLine();
			    		
			    		}
			    		
			    		buf3.flush();
			    		
			    		buf3.close();
			    		
			    	}
			    	
			    	catch (Exception e) {
			    		
			    		e.printStackTrace();
			    		
			    	}
			    		
			    		((GamePlay) this.getContext()).shareBtn(currentLevel+1);
			    	
				}//endif it's not arcade mode
				
				else {//if it is arcade mode
					
					//
					
					File arcadehighscoref = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill");
					
					boolean newdummy = arcadehighscoref.mkdirs();
					
					arcadehighscoref = new File(arcadehighscoref, "arcadehighscore.txt");
					
					try {
			    		
			    		BufferedReader buf = new BufferedReader(new FileReader(arcadehighscoref));
			    		
			    		//for (int i = 0; i < 32; i++) {
			    			
			    			arcadehighscore = Integer.parseInt(buf.readLine());
			    			
			    		//}
			    		
			    		buf.close();
			    		
			    	}
			    	
			    	catch (Exception e) {
			    		
			    		e.printStackTrace();
			    		
			    	}
			    	
					if (score > arcadehighscore) arcadehighscore = score;
					
					try {
						
						String temporary = Integer.toString(arcadehighscore);
				    	
			    		BufferedWriter buf = new BufferedWriter(new FileWriter(arcadehighscoref));
			    		
			    		buf.write(temporary);
			    		
			    		buf.flush();
			    		
			    		buf.close();
			    	
			    	}
			    	
			    	catch (Exception e) {
			    		
			    		e.printStackTrace();
			    		
			    	}
					
				}
			    
			    	//
		    		
		    		
		    	saved = true;
		    	
				}
				//start visuals for winning level here
				if (!((GamePlay) this.getContext()).arcademode) GPtemp.setupBouncingThumb(1);
				//rweqrwqt
				
				winscreen.setBounds (0, 0, width, height);
				
				winscreen.draw(c);
				
				userwon = true;
				
				if (!((GamePlay) this.getContext()).arcademode) {
				
				if (scoreAcc > 66) {
					
					mediaHandler.postDelayed(playMetal, 1000);
					
					mediaHandler.postDelayed(playMetal, 1500);
					
					mediaHandler.postDelayed(playMetal, 2000);
					
					GPtemp.threestars();
					
					threestar.setBounds((int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14));
					//threestar.draw(c);					
					
				}
				
				else if (scoreAcc > 33) {
					
					mediaHandler.postDelayed(playMetal, 1000);
					
					mediaHandler.postDelayed(playMetal, 1500);
					
					GPtemp.twostars();
					
					twostar.setBounds((int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14));
					//twostar.draw(c);					
					
				}

				else if (scoreAcc > 0) {
					
					mediaHandler.postDelayed(playMetal, 1000);
	
					GPtemp.onestar();
					
					onestar.setBounds((int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14));
					//onestar.draw(c);
					
				}

				else {
	
					nostar.setBounds((int)(width * 0.25), (int)(height * 0.025), (int)(width * 0.75), (int)(height * 0.125));
					nostar.draw(c);					
	
				}
				
				}//end check for arcade mode
				
				else { //handle result screen for arcade mode here
					
					if (score > arcadehighscore) {
						
						//draw NEW HIGH SCORE!
						
					}
					
					else {
						
						//DRAW GAME OVER
						
					}
					
					if (score > arcadehighscore) {
						
						//gold trophy
						//0.25 width and 0.2 height
						gold.setBounds((int) (0.25 * width), (int) (0.20 * height), (int) (0.75 * width), (int)(0.5 * height));
						
						gold.draw(c);
						
					}
					
					else if (score > ((int)(arcadehighscore * 0.5))) {
						
						//silver trophy
						
						silver.setBounds((int) (0.25 * width), (int) (0.20 * height), (int) (0.75 * width), (int)(0.5 * height));
						
						silver.draw(c);
						
					}
					
					else {
						
						//bronze trophy
						
						bronze.setBounds((int) (0.25 * width), (int) (0.20 * height), (int) (0.75 * width), (int)(0.5 * height));
						
						bronze.draw(c);
						
					}
					
					//output score and output best score below it
					
					
				}
				
				//levelComplete.setBounds (0, 0, width, (int) (height * 0.75));
				
				//levelHighScore.setBounds (0, 0, width, (int) (height * 0.75));
				
				mediaHandler.post(playWin);
			
				//if (highscore == false) levelComplete.draw(c);
				
				//else levelHighScore.draw(c);
				
				//myPaint.setTextSize(40f);
				
				float textSize = 8f;
				
				textSize = (float) (textSize * ((double) width / 240));
				
				myPaint.setTextSize(textSize);
				
				myPaint.setTypeface(pressStart);
				
				myPaint.setColor(Color.rgb(255,255,255));
						
				//c.drawText("SCORE: " + scoreAcc, (float) (0.03 * width), (float) (height * 0.55), myPaint);
				
				//c.drawText("TIME: " + (90 - remainingTime), (float) (0.03 * width), (float) (height * 0.63), myPaint);
				
				if (!((GamePlay) this.getContext()).arcademode) {
				
				c.drawText("LEVEL " + (currentLevel + 1) + " CLEARED!", (float) (0.23 * width), (float) (height * 0.55), myPaint);
				
				//c.drawText("BEST SCORE: " + highscores[currentLevel], (float) (0.48 * width), (float) (height * 0.55), myPaint);
				
				if (besttimes[currentLevel] != 0) c.drawText("TIME : " + (90 - remainingTime) + " | BEST : " + besttimes[currentLevel], (float) (0.155 * width), (float) (height * 0.63), myPaint);
				
				else c.drawText("TIME : " + (90 - remainingTime) + " | BEST : " + (90 - remainingTime), (float) (0.155 * width), (float) (height * 0.63), myPaint);
				
				//draw three buttons at 0.6875 of height
				
				}
				
				else {
								
					float tS = 11f;
					
					tS = (float) (tS * ((double) width / 240));
					
					myPaint.setTextSize(tS);
					
					if (score >= arcadehighscore) c.drawText("NEW HIGH SCORE!", (float) (0.16 * width), (float) (height * 0.10), myPaint);
					
					else c.drawText("GAME OVER!", (float) (0.26 * width), (float) (height * 0.10), myPaint);
					
					tS = 8f;
					
					tS = (float) (tS * ((double) width / 240));
					
					myPaint.setTextSize(tS);
					
					//c.drawText("BEST SCORE: " + highscores[currentLevel], (float) (0.48 * width), (float) (height * 0.55), myPaint);
					
					c.drawText("SCORE : " + score + " | BEST : " + arcadehighscore, (float) (0.14 * width), (float) (height * 0.63), myPaint);
					
					//else c.drawText("TIME : " + (90 - remainingTime) + " | BEST : " + (90 - remainingTime), (float) (0.155 * width), (float) (height * 0.63), myPaint);
					
					//draw three buttons at 0.6875 of height
					
					
				}
				
				menubutton.setBounds((int) (width * 0.10), (int) ((height * 0.6875) - (width * 0.02)), (int) (width * 0.26), (int) ((height * 0.6875) + (width * 0.16)));
				
				menubutton.draw(c);
				
				retrybutton.setBounds((int) (width * 0.42), (int) ((height * 0.6875) - (width * 0.02)), (int) (width * 0.58), (int) ((height * 0.6875) + (width * 0.16)));

				retrybutton.draw(c);
				
				forwardbutton.setBounds((int) (width * 0.74), (int) ((height * 0.6875) - (width * 0.02)), (int) (width * 0.90), (int) ((height * 0.6875) + (width * 0.16)));
				
				forwardbutton.draw(c);
				
				billshare.setBounds((int) (width * 0.42), (int) ((height * 0.6875) + (width * 0.16)), width, height);
				
				billshare.draw(c);
				
				//325/400 to 385/400 - 0.8125 to 0.9625
				
				//fbshare.setBounds((int) (width * 0.02), (int) (height * 0.84), (int) (width * 0.49), (int) (height * 0.99));
				
				//fbshare.draw(c);
				
				//leaderboard.setBounds((int) (width * 0.02), (int) (height * 0.84), (int) (width * 0.98), (int) (height * 0.99));
				
				//leaderboard.draw(c);
				
			}//preventing multiple writes
		
				
			else { //it never gets here because the first if statement runs if it's arcade mode as well
				
				mediaHandler.post(playLose);
				
				GPtemp.setupBouncingThumb(2);
				
				//levelFailedLives.setBounds (0, 0, width, height);
				
				//levelFailedTime.setBounds (0, 0, width, height);
				
				//if (remainingTime != 0) levelFailedLives.draw(c);
				
				//else levelFailedTime.draw(c);
				
				if (!saved) {
				
				File hscores = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill");
					    	
				boolean dummy2 = hscores.mkdirs();
				
				hscores = new File(hscores, "highscores.txt");
				
				try {
		    		
		    		BufferedReader buf = new BufferedReader(new FileReader(hscores));
		    		
		    		for (int i = 0; i < 32; i++) {
		    			
		    			highscores[i] = Integer.parseInt(buf.readLine());
		    			
		    		}
		    		
		    		buf.close();
		    		
		    	}
		    	
		    	catch (Exception e) {
		    		
		    		e.printStackTrace();
		    		
		    	}
								
				if (scoreAcc > highscores[currentLevel]) {
	    			
	    			highscores[currentLevel] = scoreAcc;
	    				    	
	    			highscore = true;
	    		
	    		}
	    		
	    		else highscore = false;
	    		
	    		try {
	    		
	    		BufferedWriter buf2 = new BufferedWriter(new FileWriter (hscores));
	    				    		
	    		for (int i = 0; i < 32; i++) {
	    			
	    			temp = Integer.toString(highscores[i]);
	    			
	    			buf2.write(temp);
	    			
	    			buf2.newLine();
	    		
	    		}
	    		
	    		buf2.flush();
	    		
	    		buf2.close();
	    		
	    	}
	    	
	    	catch (Exception e) {
	    		
	    		e.printStackTrace();
	    		
	    	}
	    		
	    		File btimes = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill");
		    	
				boolean dummy3 = btimes.mkdirs();
				
				btimes = new File(btimes, "besttimes.txt");
				
				try {
		    		
		    		BufferedReader buf4 = new BufferedReader(new FileReader(btimes));
		    		
		    		for (int i = 0; i < 32; i++) {
		    			
		    			besttimes[i] = Integer.parseInt(buf4.readLine());
		    			
		    		}
		    		
		    		buf4.close();
		    		
		    	}
		    	
		    	catch (Exception e) {
		    		
		    		e.printStackTrace();
		    		
		    	}
	    	
	    		saved = true;
	    		
			}
				/*
				 *little stars below bananas on levels, rhino shadow, check new gameover lose screen, 
				 *mrs campbell essay, michael interview
				 *
				 *PAUSE AFER LEVEL
				 *
				 */
				losescreen.setBounds (0, 0, width, height);
				
				losescreen.draw(c);
				
				userwon = false;

				float textSize = 8f;
				
				textSize = (float) (textSize * ((double) width / 240));
				
				myPaint.setTextSize(textSize);
				
				myPaint.setTypeface(pressStart);
						
				if (lives == 0) c.drawText("YOU RAN OUT OF LIVES!", (float) (0.155 * width), (float) (height * 0.59), myPaint);
				
				else c.drawText("YOU RAN OUT OF TIME!", (float) (0.155 * width), (float) (height * 0.59), myPaint);
				
				//c.drawText("SCORE: " + scoreAcc, (float) (0.03 * width), (float) (height * 0.55), myPaint);
				
				//c.drawText("TIME: " + "N/A" , (float) (0.03 * width), (float) (height * 0.63), myPaint);
				
				//c.drawText("BEST SCORE: " + highscores[currentLevel], (float) (0.48 * width), (float) (height * 0.55), myPaint);
				
				if (besttimes[currentLevel] != 0) {
				
				//c.drawText("BEST TIME: " + besttimes[currentLevel], (float) (0.48 * width), (float) (height * 0.63), myPaint);
				
				}
				
				else {
					
					//c.drawText("BEST TIME: " + "N/A", (float) (0.48 * width), (float) (height * 0.63), myPaint);
					
					}
				
				textSize = 14f;
				
				textSize = (float) (textSize * ((double) width / 240));
				
				myPaint.setTextSize(textSize);
				
				myPaint.setColor(Color.rgb(255,255,255));
				
				c.drawText("GAME OVER", (float) (0.25 * width), (float) (0.1 * height), myPaint);
				
				//draw three buttons at 0.6875 of height
				
				menubutton.setBounds((int) (width * 0.26), (int) ((height * 0.6875) - (width * 0.02)), (int) (width * 0.42), (int) ((height * 0.6875) + (width * 0.13)));
				
				menubutton.draw(c);
				
				retrybutton.setBounds((int) (width * 0.58), (int) ((height * 0.6875) - (width * 0.02)), (int) (width * 0.74), (int) ((height * 0.6875) + (width * 0.13)));
				
				retrybutton.draw(c);
				
				//325/400 to 385/400 - 0.8125 to 0.9625
				
				//fbshare.setBounds((int) (width * 0.02), (int) (height * 0.84), (int) (width * 0.49), (int) (height * 0.99));
				
				//fbshare.draw(c);
				
				//leaderboard.setBounds((int) (width * 0.51), (int) (height * 0.84), (int) (width * 0.98), (int) (height * 0.99));
				
				//leaderboard.draw(c);
				
			}
			
		}
        
if (tutorialToggle) {
			
			//tutorial screen counter
			gamepaused = true;
	
	
			switch (tutorialScreenCounter) {
			
				case 1 : 
					
					tut1.setBounds(0, 0, width, height);
					
					tut1.draw(c);
					
				break;
				
				case 2 : 
					
					tut2.setBounds(0, 0, width, height);
					
					tut2.draw(c);
					
				break;
				
				case 3 : 
					
					tut3.setBounds(0, 0, width, height);
					
					tut3.draw(c);
					
				break;
				
				case 4 : 
					
					tut4.setBounds(0, 0, width, height);
					
					tut4.draw(c);
					
				break;
				
				case 5 : 
					
					tut5.setBounds(0, 0, width, height);
					
					tut5.draw(c);
					
				break;
				
				case 6 : 
					
					tut6.setBounds(0, 0, width, height);
					
					tut6.draw(c);
					
				break;
				
				case 7 : 
					
					tut7.setBounds(0, 0, width, height);
					
					tut7.draw(c);
					
				break;
				
				case 8 : 
					
					tut8.setBounds(0, 0, width, height);
					
					tut8.draw(c);
					
				break;
				
				case 9 : 
					
					tut9.setBounds(0, 0, width, height);
					
					tut9.draw(c);
					
				break;
				
				case 10 : 
					
					tut10.setBounds(0, 0, width, height);
					
					tut10.draw(c);
					
				break;
				
				case 11 : 
					
					tut11.setBounds(0, 0, width, height);
					
					tut11.draw(c);
					
				break;
				
				case 12 :
					
					tutorialToggle = false;
					
					gamepaused = false;//MAKE SURE first screen doesn't respond to touch until sequence is finished!
					//attila still moves before the game starts and bill is oddly immune to him on level one
					//attila sound fixed tho
					myHandler.post(myRunnable);
			
			}
			
			postInvalidate();
			
		}
		
	}
	
public boolean onTouchEvent (MotionEvent e) {
	
	int tempoX, tempoY; //used to track touch after gameover	
	
	tempoX = tempoY = 0;

		if (gameover && timeout) {
			
			//Activity dummy = (Activity) getContext();
			
			//dummy.finish();
			
			switch (e.getAction()) {
			
			case MotionEvent.ACTION_UP :
			
				tempoX = (int) e.getX();
				tempoY = (int) e.getY();
				
			}
			
			if (userwon) {
			
				if (tempoX > (width * 0.10) && tempoX < (width * 0.26) && tempoY > ((height * 0.6875) - (width * 0.03)) && tempoY < ((height * 0.6875) + (width * 0.13))) {
							
					GPtemp.onBackPressed();
			
				}
			
				else if (tempoX > (width * 0.42) && tempoX < (width * 0.58) && tempoY > ((height * 0.6875) - (width * 0.03)) && tempoY < ((height * 0.6875) + (width * 0.13))) {
										        	
			        	GPtemp = (GamePlay) this.getContext();
						
			        	Intent tempI = GPtemp.getIntent();
			        	
						GPtemp.myRecreate(tempI);
			      			
				}
			
				else if (tempoX > (width * 0.74) && tempoX < (width * 0.90) && tempoY > ((height * 0.6875) - (width * 0.03)) && tempoY < ((height * 0.6875) + (width * 0.13))) {
								
					Intent tempIntent = GPtemp.getIntent();
				
					tempIntent.removeExtra("level");
				
					tempIntent.putExtra("level", (currentLevel + 1));
				
					tempIntent.removeExtra("maxlevel");
				
					if (maxlevel < 32 && maxlevel == (currentLevel+1)) maxlevel++;
				
					tempIntent.putExtra("maxlevel", maxlevel);
				
					GPtemp.nextLevel(tempIntent);
			
				}
			
			}
			
			else { //if user lost
				
				if (tempoX > (width * 0.26) && tempoX < (width * 0.42) && tempoY > ((height * 0.6875) - (width * 0.03)) && tempoY < ((height * 0.6875) + (width * 0.13))) {
					
					GPtemp.onBackPressed();
			
				}
			
				else if (tempoX > (width * 0.58) && tempoX < (width * 0.74) && tempoY > ((height * 0.6875) - (width * 0.03)) && tempoY < ((height * 0.6875) + (width * 0.13))) {
									
					if (android.os.Build.VERSION.SDK_INT >= 11) { 
			        	
						GPtemp.recreate();
			        	
			        }
			        
			        else {
			        	
			        	Intent tempI = GPtemp.getIntent();
			        	
						GPtemp.myRecreate(tempI);
			        	
			        }
			
				}
				
			}
			
			/*
			 
			 	menubutton.setBounds((int) (width * 0.25), (int) (height * 0.6875), (int) (width * 0.35), (int) ((height * 0.6875) + (width * 0.1)));
				
				menubutton.draw(c);
				
				retrybutton.setBounds((int) (width * 0.45), (int) (height * 0.6875), (int) (width * 0.55), (int) ((height * 0.6875) + (width * 0.1)));

				retrybutton.draw(c);
				
				forwardbutton.setBounds((int) (width * 0.65), (int) (height * 0.6875), (int) (width * 0.75), (int) ((height * 0.6875) + (width * 0.1)));
				
				forwardbutton.draw(c);
			 
			 */
			
			
		}
		
		else {
	
    	switch (e.getAction()) {
    	
    	case MotionEvent.ACTION_DOWN : 
    		
    		if (tutorialToggle) { 
    			
    			tutorialScreenCounter++;
    			
    			postInvalidate();
    			
    		}
    		
    		downX = (int) e.getX();
    		downY = (int) e.getY();	
    		
    		if (e.getX() > (width * 0.9) && e.getY() > (height * 0.9)) {
    			
    			if (!gamepaused) {
    				    				
    				gamepaused = true;
    				
    			}
    			
    			else if (gamepaused) { 
    				
    				gamepaused = false;
    				
    				myHandler.post(myRunnable);
    				
    			}
    			
    		}
    	
    	break;
    	
		case MotionEvent.ACTION_UP : 
	
				upX = (int) e.getX();
				upY = (int) e.getY();
				
				if (upX < downX && upY < downY) {
					
					spiderDirection = UPLEFT;
					
				}
				
				if (upX < downX && upY > downY) {
					
					spiderDirection = DOWNLEFT;
					
				}

				if (upX > downX && upY < downY) {
	
					spiderDirection = UPRIGHT;
	
				}

				if (upX > downX && upY > downY) {
	
					spiderDirection = DOWNRIGHT;
	
				}
				
		break;
	  		    		
		}
    	
    	invalidate();
    	
		} //end else
    	    	
    	return true;
    	
	}	

	void cleanUp () {
		
				mySoundpool.release();
		
				mySoundpool = null;
			
				mediaThread.quit();
		    	
		    	myHandler.removeCallbacksAndMessages(null);
		
	}

}

class Monster {
	
	boolean active = false;
	
	boolean valid = true; //used to determine whether random spot interferes with obstacle
	
	int x, y, width, height;
	
	int monsterMoveIncrement = 3;
	
	int mouseRadius;
	
	final int UPLEFT = 1;
	
	final int DOWNLEFT = 2;
	
	final int UPRIGHT = 3;
	
	final int DOWNRIGHT = 4;
	
	int direction = DOWNRIGHT;
	
	Level[] levelArray = new Level[33];
	
	int currentLevel = 0;
	
	Obstacle[] obstacleArray = new Obstacle[3];
	
	Monster (int w, int h, int l) {
		
		width = w;
		
		height = h;
		
		monsterMoveIncrement = (int) (((double) width / 240) * 3);
				
		//myObstacle = new Obstacle(100, 100, 50, 50);
		
		levelArray[0] = new Level(width, height, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[1] = new Level(width, height, 1, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[2] = new Level(width, height, 2, 10, 1, 110, 160, 20, 160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[3] = new Level(width, height, 2, 10, 2, 75, 100, 25, 150, 145, 100, 25, 150, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[4] = new Level(width, height, 2, 10, 2, 110, 190, 20, 130, 110, 0, 20, 130, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[5] = new Level(width, height, 2, 10, 2, 0, 140, 90, 40, 150, 140, 100, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[6] = new Level(width, height, 2, 10, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 200, 190, 40, 140);
		
		levelArray[7] = new Level(width, height, 3, 10, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 200, 190, 40, 140);
		
		levelArray[8] = new Level(width, height, 2, 10, 3, 140, 0, 100, 40, 0, 140, 100, 40, 140, 230, 100, 40, 0, 0, 0, 0, 140, 270, 100, 50);
		
		levelArray[9] = new Level(width, height, 3, 10, 2, 0, 100, 100, 40, 190, 140, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[10] = new Level(width, height, 2, 5, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 100, 0, 40, 320, 0, 0, 0, 0);
		
		levelArray[11] = new Level(width, height, 2, 10, 2, 110, 190, 20, 130, 110, 0, 20, 130, 0, 0, 0, 0, 0, 0, 0, 0, 130, 0, 110, 320);
		
		levelArray[12] = new Level(width, height, 2, 10, 2, 75, 0, 25, 140, 160, 180, 25, 140, 0, 0, 0, 0, 0, 140, 240, 40, 0, 0, 0, 0);
		
		levelArray[13] = new Level(width, height, 4, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[14] = new Level(width, height, 2, 10, 2, 0, 170, 140, 40, 140, 70, 40, 140, 0, 0, 0, 0, 140, 20, 40, 50, 0, 0, 0, 0);
		
		levelArray[15] = new Level(width, height, 2, 10, 2, 0, 90, 200, 40, 40, 220, 200, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[16] = new Level(width, height, 3, 10, 2, 0, 90, 200, 40, 40, 220, 200, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[17] = new Level(width, height, 2, 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[18] = new Level(width, height, 3, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[19] = new Level(width, height, 4, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[20] = new Level(width, height, 3, 10, 1, 110, 160, 20, 160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[21] = new Level(width, height, 4, 10, 1, 110, 160, 20, 160, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[22] = new Level(width, height, 3, 10, 2, 75, 100, 25, 150, 145, 100, 25, 150, 0, 0, 0, 0, 100, 100, 45, 150, 0, 0, 0, 0);
		
		levelArray[23] = new Level(width, height, 3, 10, 2, 110, 190, 20, 130, 110, 0, 20, 130, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[24] = new Level(width, height, 3, 10, 2, 0, 140, 90, 40, 150, 140, 100, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[25] = new Level(width, height, 3, 10, 3, 140, 0, 100, 40, 0, 140, 100, 40, 140, 230, 100, 40, 0, 0, 0, 0, 140, 270, 100, 50);
		
		levelArray[26] = new Level(width, height, 4, 10, 2, 0, 100, 100, 40, 190, 140, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[27] = new Level(width, height, 3, 5, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 100, 0, 40, 320, 0, 0, 0, 0);
		
		levelArray[28] = new Level(width, height, 3, 10, 2, 110, 190, 20, 130, 110, 0, 20, 130, 0, 0, 0, 0, 110, 130, 20, 60, 130, 0, 110, 320);
		
		levelArray[29] = new Level(width, height, 3, 10, 2, 0, 170, 140, 40, 140, 70, 40, 140, 0, 0, 0, 0, 140, 20, 40, 50, 0, 0, 0, 0);
		
		levelArray[30] = new Level(width, height, 4, 10, 2, 0, 90, 200, 40, 40, 220, 200, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		levelArray[31] = new Level(width, height, 4, 10, 2, 0, 100, 100, 40, 140, 190, 100, 40, 0, 280, 100, 40, 0, 0, 0, 0, 200, 190, 40, 140);
		
		levelArray[32] = new Level(width, height, 5, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		
		currentLevel = l; //equates to level 1 - this will actually be selected by user based on unlocking
		
		mouseRadius = (int) (width * 0.05);
		
		for (int i = 0; i < levelArray[currentLevel].getNumberOfObstacles(); i++) {
			
		obstacleArray[i] = new Obstacle(levelArray[currentLevel].getObstacleX(i), levelArray[currentLevel].getObstacleY(i), levelArray[currentLevel].getObstacleWidth(i), levelArray[currentLevel].getObstacleHeight(i), mouseRadius, width, height);	
		
		} // MOUSE RADIUS ADJUSTED DOWN FROM 0.05 TO AVOIDING KILLING FROM TOO FAR
		
		do {
			
		valid = true;
		
		x = (int) ((Math.random() * (width - (mouseRadius * 2))) + mouseRadius);
		
		y = (int) ((Math.random() * ((height - (width * 0.1)) - (mouseRadius * 2))) + (mouseRadius + (width * 0.1)));
		
		for (int i = 0; i < levelArray[currentLevel].getNumberOfObstacles(); i++) {
			
			if (x > obstacleArray[i].getX() && x < (obstacleArray[i].getX() + obstacleArray[i].getWidth())) {
			
				if (y > obstacleArray[i].getY() && y < (obstacleArray[i].getY() + obstacleArray[i].getHeight())) {
				
					valid = false;
				
				}
			
			}
		
		}
		
		if (x > levelArray[currentLevel].getWaterX() && x < (levelArray[currentLevel].getWaterX() + levelArray[currentLevel].getWaterWidth()) && y > levelArray[currentLevel].getWaterY() && y < (levelArray[currentLevel].getWaterY() + levelArray[currentLevel].getWaterHeight())) valid = false;//checking if in glue trap
		
		} while (valid == false);
		
	}
	
	public boolean move (int spiderX, int spiderY) {
		
		boolean kill = false;
		
		for (int i = 0; i < levelArray[currentLevel].getNumberOfObstacles(); i++) {
		
			if (obstacleArray[i].collisionCheck(x, y, direction) != 0) {
			
				direction = obstacleArray[i].collisionCheck(x, y, direction);
			
			} 
			
		}
		
		if (x > levelArray[currentLevel].getWaterX() && x < (levelArray[currentLevel].getWaterX() + levelArray[currentLevel].getWaterWidth()) && y > levelArray[currentLevel].getWaterY() && y < (levelArray[currentLevel].getWaterY() + levelArray[currentLevel].getWaterHeight())) {//checking if in glue trap
			
			if (direction == UPLEFT) direction = UPRIGHT;
			
			else if (direction == UPRIGHT) direction = UPLEFT;
			
			else if (direction == DOWNRIGHT) direction = DOWNLEFT;
			
			else if (direction == DOWNLEFT) direction = DOWNRIGHT;
			
		} //improve this later!!!!!!!!!!!!!!!!!!!!!!! don't let it approach water from bottom or top
		
		switch (direction) {
		
		case UPLEFT : 
	
			x-=monsterMoveIncrement;

			y-=monsterMoveIncrement;
	
		break;
		
		case UPRIGHT : 
			
			x+=monsterMoveIncrement;

			y-=monsterMoveIncrement;
	
		break;
		
		case DOWNLEFT : 
			
			x-=monsterMoveIncrement;

			y+=monsterMoveIncrement;
	
		break;
		
		case DOWNRIGHT : 
			
			x+=monsterMoveIncrement;

			y+=monsterMoveIncrement;
	
		break;
		
		}
		//lessing the range of kill slightly
		if ((x + (mouseRadius * 0.75)) > (spiderX - mouseRadius) && (x - (mouseRadius * 0.75)) < (spiderX + mouseRadius)) { //same column
			
			if ((y + (mouseRadius * 0.75)) > (spiderY - mouseRadius) && (y - (mouseRadius * 0.75)) < (spiderY + mouseRadius)) { //same row
				
				kill = true;
				
			}
							
		}
	
	if (x < mouseRadius && direction == UPLEFT) {
		
		direction = UPRIGHT;
		
	}
	
	if (x < mouseRadius && direction == DOWNLEFT) {
		
		direction = DOWNRIGHT;
		
	}
	
	if (x > (width - mouseRadius) && direction == UPRIGHT) {
		
		direction = UPLEFT;
		
	}
	
	if (x > (width - mouseRadius) && direction == DOWNRIGHT) {
		
		direction = DOWNLEFT;
		
	}
	
	if (y < (mouseRadius + (width*0.1)) && direction == UPLEFT) {
		
		direction = DOWNLEFT;
		
	}
	
	if (y < (mouseRadius + (width*0.1)) && direction == UPRIGHT) {
		
		direction = DOWNRIGHT;
		
	}
	
	if (y > (height - mouseRadius) && direction == DOWNRIGHT) {
		
		direction = UPRIGHT;
		
	}
	
	if (y > (height - mouseRadius) && direction == DOWNLEFT) {
		
		direction = UPLEFT;
		
	}

	return kill;	//whether killed or not
	
	}
	
	public int getX () {
		
		//x = 50;
		
		return this.x;
		
	}
	
	public int getY () {
		
		//y = 50;
		
		return this.y;
		
	}
	
	public int getDirection () {
		
		return direction;
		
	}
	
}

class Rhino extends Monster {
		
	int dir;
	
	public boolean shadow;
	
	Rhino (int w, int h, int l) {
		
		super(w, h, l);
		
		shadow = false;
		
		monsterMoveIncrement = (int) (((double) width / 240) * 3);
		
		this.x = (int) ((((double) width / 240) * 200) * -1);
		
		this.y = (int) (((double) width / 240) * 150); ///update this to be constructor with y of bill
		
		direction = UPRIGHT;
		
	}
	
	Rhino (int w, int h, int l, int d) {//d 1 = left - d 2 = right
		
		super(w, h, l);
		
		dir = d;
		
		monsterMoveIncrement = (int) (((double) width / 240) * 4);
		
		if (d == 2) this.x = (int) ((((double) width / 240) * 200) * -1);
		
		else if (d == 1)  this.x = (int) ((((double) width / 240) * 520));
		
		this.y = (int) (((double) width / 240) * 150); ///update this to be constructor with y of bill
		
		if (d == 1) direction = UPLEFT;
		
		else if (d == 2) direction = UPRIGHT;
		
		if (d == 0) this.x = this.y = -100;
		
	}
	
	public void getSet (int w, int h, int l, int d) {
		
		dir = d;
		
		monsterMoveIncrement = (int) (((double) width / 240) * 4);
		
		if (d == 2) this.x = (int) ((((double) width / 240) * 200) * -1);
		
		else if (d == 1)  this.x = (int) ((((double) width / 240) * 520));
		
		this.y = (int) (((double) width / 240) * 150); ///update this to be constructor with y of bill
		
		if (d == 1) direction = UPLEFT;
		
		else if (d == 2) direction = UPRIGHT;
		
		if (d == 0) this.x = this.y = -100;
		
	}

	public int getDir () {
		return this.dir;
	}

	public boolean move (int spiderX, int spiderY) {
		
		boolean kill = false;
		
		shadow = false;
		
		for (int i = 0; i < levelArray[currentLevel].getNumberOfObstacles(); i++) {
			
			int tempDir = direction;
			
			if (obstacleArray[i].collisionCheck(x, y, direction) != tempDir) {
			
				shadow = true;
			
			}
			
		}
		
//	!!!!!!!!!!! don't let it approach water from bottom or top
		
		if (dir == 2) {
		
		switch (direction) {
		
		case UPRIGHT : 
			
			this.x+=monsterMoveIncrement;

			this.y-=monsterMoveIncrement;
	
		break;
		
		case DOWNRIGHT : 
			
			this.x+=monsterMoveIncrement;

			this.y+=monsterMoveIncrement;
	
		break;
		
		}
		
		}
		
		else if (dir == 1) {
			
			switch (direction) {
			
			case UPLEFT : 
				
				this.x-=monsterMoveIncrement;

				this.y-=monsterMoveIncrement;
		
			break;
			
			case DOWNLEFT : 
				
				this.x-=monsterMoveIncrement;

				this.y+=monsterMoveIncrement;
		
			break;
			
			}			
			
		}
		
		if ((this.x + (mouseRadius*3)) > (spiderX - mouseRadius) && (this.x - (mouseRadius*3)) < (spiderX + mouseRadius)) { //same column
			
			if ((this.y + mouseRadius) > (spiderY - mouseRadius) && (this.y - mouseRadius) < (spiderY + mouseRadius)) { //same row
				
				kill = true;
				
			}
							
		}
	
	if (dir == 2) {	
		
	if ((spiderY + mouseRadius) < this.y) {
		
		direction = UPRIGHT;
		
	}
	
	else if ((spiderY) > (this.y + mouseRadius)) {
		
		direction = DOWNRIGHT;
		
	}
	
	}
	
	else if (dir == 1) {	
		
		if ((spiderY + mouseRadius) < this.y) {
			
			direction = UPLEFT;
			
		}
		
		else if ((spiderY) > (this.y + mouseRadius)) {
			
			direction = DOWNLEFT;
			
		}
		
		}
	
	return kill;	//whether killed or not
	
	}
	
	public int getX () {
		
		return this.x;
		
	}
	
	public int getY () {
		
		return this.y;
		
	}

	public int getDirection () {
		
		return direction;
		
	}
	
}

class Obstacle {
	
	int x;
	
	int y;
	
	int endX, endY, startX, startY;
	
	int width;
	
	int height;
	
	int screenW, screenH;
	
	int mouseRadius, halfMouseRadius; //used to prevent passing through corners
	
	int direction;
	
	final int UPLEFT = 1;
	
	final int DOWNLEFT = 2;
	
	final int UPRIGHT = 3;
	
	final int DOWNRIGHT = 4;
	
	Obstacle (int x, int y, int width, int height, int r, int screenW, int screenH) {
		
		this.x = x;
		
		this.y = y;
		
		this.width = width;
		
		this.height = height;
		
		this.mouseRadius = r; 
		
		this.screenW = screenW;
		
		this.screenH = screenH;
		
		endX = this.x + this.width;
		
		endY = this.y + this.height;
		
		startX = this.x;
		
		startY = this.y;
		
		this.halfMouseRadius = (int) ((double) this.mouseRadius/2);
		
	}
	
	int getX () {
		
		return this.x;
		
	}
	
	int getY () {
		
		return this.y;
		
	}

	int getWidth () {
	
	return this.width;
	
	}

	int getHeight () {
	
	return this.height;
	
	}
	
	int collisionCheck (int spiderX, int spiderY, int direction) {
		
		if (direction == UPRIGHT) {//in rhino - check if direction is not equal to what it sent to collision check
		
			if ((spiderX + mouseRadius) > startX && spiderX < startX)  {
				
					if (spiderY > startY && (spiderY - halfMouseRadius) < endY) direction = UPLEFT; 
					
			}
			
			else if ((spiderY - mouseRadius) < endY && spiderY > endY) {
				
					if (spiderX > startX && spiderX < endX) direction = DOWNRIGHT;
				
			}
			
		}
		
		if (direction == DOWNRIGHT) {
			
			if ((spiderX + mouseRadius) > startX && (spiderX - halfMouseRadius) < startX)  {
				
					if ((spiderY + halfMouseRadius) > startY && spiderY < endY) direction = DOWNLEFT;
					
			}
		
			else if ((spiderY + mouseRadius) > startY && spiderY < startY) {
				
					if (spiderX > startX && spiderX < endX) direction = UPRIGHT;
				
			}
			
		}
		
		if (direction == UPLEFT) {
			
			if ((spiderX - mouseRadius) < endX && spiderX > endX)  {
				
					if (spiderY > startY && (spiderY - halfMouseRadius) < endY) direction = UPRIGHT; 
					
			}
		
			else if ((spiderY - mouseRadius) < endY && spiderY > endY) {
				
					if (spiderX > startX && spiderX < endX) direction = DOWNLEFT;
				
			}
			
		}
		
		if (direction == DOWNLEFT) {
			
			if ((spiderX - mouseRadius) < endX && (spiderX + halfMouseRadius) > endX)  {
				
					if ((spiderY + halfMouseRadius) > startY && spiderY < endY) direction = DOWNRIGHT; 
					
			}
		
			else if ((spiderY + mouseRadius) > startY && spiderY < startY) {
				
					if (spiderX > startX && spiderX < endX) direction = UPLEFT;
				
			}
			
		}
		
		/*boolean collision = false;
		
		if ((spiderX + mouseRadius) > (x * ((double) screenW / 240)) || (spiderX - mouseRadius) < ((x + width) * ((double) screenW/240))) { //same column
			//maybe change this instead
			if ((spiderY + mouseRadius) > (y * ((double) screenH/320)) || (spiderY - mouseRadius) < (y + height) * ((double) screenH/320)) { //same row
				
				collision = true;
				
			}
						
		}
		
		if (collision) {
			
			switch (direction) {
			
				case UPRIGHT :
					///////////////////////
					if ((spiderX) < (x * ((double) screenW / 240))) direction = UPLEFT; // let these vary with screen height like moveInc too
					///////////////////////
					else direction = DOWNRIGHT;
					
				break;
				
				case DOWNRIGHT :
					
					if ((spiderX) < (x * ((double) screenW / 240))) direction = DOWNLEFT;
					
					else direction = UPRIGHT;
					
				break;
				
				case UPLEFT :
	
					if (spiderX > (x+width-((int)(4 * ( (double) screenW / 240))))) direction = UPRIGHT;
	
					else direction = DOWNLEFT;
	
				break;

				case DOWNLEFT :
	
					if (spiderX > (x+width-((int)(4 * ( (double) screenW / 240))))) direction = DOWNRIGHT;
	
					else direction = UPLEFT;
	
				break;
			
			}
			
		}*/
				
		return direction;
		
	}
	
}

class Level {
	
	int numberOfEnemies;
	
	int applesRequired; //switch to points/apples required to level up
	
	int numberOfObstacles;
	
	int[] obstaclesX;
	
	int[] obstaclesY;
	
	int[] obstaclesWidth;
	
	int[] obstaclesHeight;
	
	int glueX, glueY, glueWidth, glueHeight;
	
	int waterX, waterY, waterWidth, waterHeight;
	
	Level (int w, int h, int n, int a, int n2, double ox1, double oy1, double ow1, double oh1, double ox2, double oy2, double ow2, double oh2, double ox3, double oy3, double ow3, double oh3, double gx,double gy, double gw, double gh, double wx, double wy, double ww, double wh) {
		
		numberOfEnemies = n;
		
		applesRequired = a;
		
		numberOfObstacles = n2;
		
		obstaclesX = new int[3];
		
		obstaclesY = new int[3];
		
		obstaclesWidth = new int[3];
		
		obstaclesHeight = new int[3];
		
		obstaclesX[0] = (int) ((ox1 / 240) * w);
		
		obstaclesY[0] = (int) ((oy1 / 320) * h);
		
		obstaclesWidth[0] = (int) ((ow1 / 240) * w);
		
		obstaclesHeight[0] = (int) ((oh1 / 320) * h);
		
		obstaclesX[1] = (int) ((ox2 / 240) * w);
		
		obstaclesY[1] = (int) ((oy2 / 320) * h);
		
		obstaclesWidth[1] = (int) ((ow2 / 240) * w);
		
		obstaclesHeight[1] = (int) ((oh2 / 320) * h);
		
		obstaclesX[2] = (int) ((ox3 / 240) * w);
		
		obstaclesY[2] = (int) ((oy3 / 320) * h);
		
		obstaclesWidth[2] = (int) ((ow3 / 240) * w);
		
		obstaclesHeight[2] = (int) ((oh3 / 320) * h);
				
		glueX = (int) ((gx / 240) * w);
				
		glueY = (int) ((gy / 320) * h);
		
		glueWidth = (int) ((gw / 240) * w);
		
		glueHeight = (int) ((gh / 320) * h);
				
		waterX = (int) ((wx / 240) * w);
		
		waterY = (int) ((wy / 320) * h);
		
		waterWidth = (int) ((ww / 240) * w);
		
		waterHeight = (int) ((wh / 320) * h);
		
	}
	
	int getNumberOfEnemies() {
		
		return numberOfEnemies;
		
	}
	
	int getApplesRequired() {
		
		return applesRequired;
		
	}
	
	int getNumberOfObstacles() {
		
		return numberOfObstacles;
		
	}
	
	int getObstacleX (int i) {
		
		return obstaclesX[i];
		
	}
	
	int getObstacleY (int i) {
		
		return obstaclesY[i];
		
	}
	
	int getObstacleWidth (int i) {
		
		return obstaclesWidth[i];
		
	}
	
	int getObstacleHeight (int i) {
		
		return obstaclesHeight[i];
		
	}
	
	int getGlueX () {
		
		return glueX;
		
	}
	
	int getGlueY () {
		
		return glueY;
		
	}

	int getGlueWidth () {
	
		return glueWidth;
	
	}

	int getGlueHeight () {
	
		return glueHeight;
	
	}
	
	int getWaterX () {
		
		return waterX;
		
	}
	
	int getWaterY () {
		
		return waterY;
		
	}

	int getWaterWidth () {
	
		return waterWidth;
	
	}

	int getWaterHeight () {
	
		return waterHeight;
	
	}

}
