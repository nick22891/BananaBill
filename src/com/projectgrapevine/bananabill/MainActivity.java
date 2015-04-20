package com.projectgrapevine.bananabill;
 
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import android.app.ActionBar.LayoutParams;
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

//import android.view.Gravity;

//import com.google.ads.AdRequest;
//import com.google.ads.AdSize;
//import com.google.ads.AdView;

public class MainActivity extends Activity {
	
	private FrameLayout myLayout;
	
	private FrameLayout.LayoutParams params;
	
	private MainMenuView myView;
	
	private RotateAnimation buttonRotate, buttonReverseRotate;
	
	private ImageView start, achievements, highscores;
	
	protected AdView adView;
	
	boolean musicStopped = false;
	//private AdView adView;
	
	String name = "startSt";
	
	int width, height;
	
	Intent i; 

	public void onSignInFailed () {
		
	}
	
	public void onSignInSucceeded () {
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_level_select);
		i = new Intent(this, BackgroundMusicService.class);
		
		startService(i);
		
		

		Display display = getWindowManager().getDefaultDisplay();
        
        Point size = new Point();
        
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("a153396ecd48ff3");
        
        //adView.setAdSize(AdSize.BANNER);
        
        //adView.setAdUnitId(AD_UNIT_ID);
        
        if (android.os.Build.VERSION.SDK_INT >= 13) { 
        	
        	display.getSize(size);
        	            
            width = size.x;
            
            height = size.y;
        	
        }
        
        else {
        	
        	width = display.getWidth();
        	
        	height = display.getHeight();
        	
        }
        
		
		start = new ImageView(this);
		
		//animview.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
		
		start.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
		  LayoutParams.WRAP_CONTENT));
		
		params = new FrameLayout.LayoutParams((int)(width * 0.40), (int)(height * ((double)60/534)));
		
		params.setMargins((int)(width * 0.30), (int) (height * ((double)280/534)), (int)(width * 0.70), (int)(height * ((double)340/534)));
		
		//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
		
		params.gravity = Gravity.TOP;
		
		start.setLayoutParams(params);
		
		
		start.setBackgroundResource(R.drawable.startbtn);
		//
		
		achievements = new ImageView(this);
		
		//animview.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
		
		achievements.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
		  LayoutParams.WRAP_CONTENT));
		
		params = new FrameLayout.LayoutParams((int)(width * 0.40), (int)(height * ((double)53/534)));
		
		params.setMargins((int)(width * 0.30), (int) (height * ((double)353/534)), (int)(width * 0.70), (int)(height * ((double)408/534)));
		
		//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
		
		params.gravity = Gravity.TOP;
		
		achievements.setLayoutParams(params);
		
		achievements.setBackgroundResource(R.drawable.achievementsbtn);
		
		//
		
		highscores = new ImageView(this);
		
		//animview.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
		
		highscores.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
		  LayoutParams.WRAP_CONTENT));
		
		params = new FrameLayout.LayoutParams((int)(width * 0.40), (int)(height * ((double)56/534)));
		
		params.setMargins((int)(width * 0.30), (int) (height * ((double)422/534)), (int)(width * 0.70), (int)(height * ((double)478/534)));
		
		//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
		
		params.gravity = Gravity.TOP;
		
		highscores.setLayoutParams(params);
		
		highscores.setBackgroundResource(R.drawable.highscoresbtn);
				
		//adView = new AdView(this, AdSize.BANNER, "a152709973dcd0d");
		
		//LinearLayout.LayoutParams LP = (LinearLayout.LayoutParams) adView.getLayoutParams();
		
		//LP.gravity=Gravity.BOTTOM;
		
		//adView.setLayoutParams(LP);
		
		musicStopped = false;
		
        buttonRotate = new RotateAnimation(-4f, 4f, (float) (0.2 * width), (float) (height * ((double)28/534)));
        
        buttonRotate.setDuration(1000);
        
        buttonRotate.setRepeatMode(RotateAnimation.REVERSE);
        
        buttonRotate.setRepeatCount(1000);
        
        buttonRotate.setFillEnabled(true);
        
        buttonRotate.setFillAfter(true);
        
        //
        
        buttonReverseRotate = new RotateAnimation(4f, -4f, (float) (0.2 * width), (float) (height * ((double)28/534)));
        
        buttonReverseRotate.setDuration(1000);
        
        buttonReverseRotate.setRepeatMode(RotateAnimation.REVERSE);
        
        buttonReverseRotate.setRepeatCount(1000);
        
        buttonReverseRotate.setFillEnabled(true);
        
        buttonReverseRotate.setFillAfter(true);
        
        buttonReverseRotate.setStartOffset(0);
        
		//adView.setX(0f);
		
		//adView.setY((float) (height * 0.8));csd
        
        myView = new MainMenuView (this, i, width, height);
        
        myLayout = new FrameLayout(this);
        
        setContentView(myLayout);
        
        myLayout.addView(myView);
        
        myLayout.addView(start);
        
        myLayout.addView(highscores);
        
        myLayout.addView(achievements);
        
        
        
        
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        
        lp = new FrameLayout.LayoutParams((int)(width), (int)(height * 0.09));
		lp.setMargins((int)(width * 0.0), (int)(height * 0.00), (int)(width * 1), (int)(height * 0.09));
		
		lp.gravity = Gravity.TOP;
        
        adView.setLayoutParams(lp);
        
        
        
        
        
        
        
        
        myLayout.addView(adView);
        
        adView.loadAd(new AdRequest.Builder().build());
        
        adView.setVisibility(View.INVISIBLE);
        
        start.setVisibility(View.GONE);
        
        achievements.setVisibility(View.GONE);
        
        highscores.setVisibility(View.GONE);
        
        //FACEBOOK
        
       
        //adView.loadAd(new AdRequest());
		
	}

	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      
	      name = "activity result runs";
	  }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_select, menu);
		return true;
	}
	
	public String getName() {
		
		return name;
		
	}
	
	public void onBackPressed () {
		
		stopService(i);
		
		super.onBackPressed();
		
	}
	
	public void showButtons () {
		        
        //start.setVisibility(View.VISIBLE);
        
        //achievements.setVisibility(View.VISIBLE);
        
        //highscores.setVisibility(View.VISIBLE);
		
	}
	
	//public void onUserLeaveHint () {
		
		//stopService(i);
		
		//super.onUserLeaveHint();
		
	//}
	
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
	
	@SuppressWarnings("deprecation")
	public void onResume () {
		
		super.onResume();
        
        //name = "StartSt2";
             
		
		
        start.startAnimation(buttonRotate);
        
        highscores.startAnimation(buttonRotate);
        
        achievements.startAnimation(buttonReverseRotate);
		
	}
	
	public void onStop () {
		
		super.onStop();
		
		FlurryAgent.onEndSession(this);
				
		if (!isAppForeground()) {
			
			stopService(i);
			
			musicStopped = true;
			
		}
		
	}
	
	public void onStart () {
		
		super.onStart();
		
		FlurryAgent.onStartSession(this, "4WSGV3PW4YTZPQDD7X8Q");
		
		//stopService(i);
		
		//startService(i);
		
	}
	
	public void onRestart () {
		
		super.onRestart();
		
		if (musicStopped) {
		
		//stopService(i);
		
		startService(i);
		
		musicStopped = false;
			
		}
		
	}
	
	public void startMusicService () {
		
		startService(i);
		
	}
	
	public void stopMusicService () {
		
		stopService(i);
		
	}

}

class MainMenuView extends View {
	
	boolean music = true;
	
	boolean sfx = true;
	
	boolean grapevinesplash = true;//set to false when splash screen sequence ends
	
	boolean bananabillsplash = true;
	
	boolean story1splash = true, story2splash = true, story3splash = true, story4splash = true, story5splash = true;
	
	Paint myPaint;
	
	String name;
	
	Context context;
	
	Timer splashTimer;
	
	Runnable invalidategrape, invalidatebanana, invalidatestory1, invalidatestory2, invalidatestory3, invalidatestory4, invalidatestory5;
	
	Handler splashHandler;

	Intent i;
	
	int width, height;
	
	private Drawable story1, story2, story3, story4, story5, background, play, rules, highscores, banner, musicsymbol, sfxsymbol, nosymbol, credits, copyright, bunch, grapevinesplashimg, bananabillsplashimg, startscreen;
	
	MainMenuView (Context c, Intent intent, int w, int h) {
		
		super(c);
		
		music = true;
		
		i = intent;
		
		context = c;
		
		width = w;
		
		height = h;
		
		name = ((MainActivity) c).getName();
		
		Resources res = this.getResources();
		
		background = res.getDrawable(R.drawable.bg);
		
		//play = res.getDrawable(R.drawable.play);
		
		//rules = res.getDrawable(R.drawable.rules);
		
		//highscores = res.getDrawable(R.drawable.highscores);
		
		//banner = res.getDrawable(R.drawable.banner);
		
		musicsymbol = res.getDrawable(R.drawable.musicsymbol);
		
		sfxsymbol = res.getDrawable(R.drawable.sfxsymbol);
		
		nosymbol = res.getDrawable(R.drawable.nosymbol);
		
		//credits = res.getDrawable(R.drawable.credits);
		
		//copyright = res.getDrawable(R.drawable.copyright);
		
		grapevinesplashimg = res.getDrawable(R.drawable.grapevinesplash);
		
		bananabillsplashimg = res.getDrawable(R.drawable.bananabillsplash);
		
		story1 = res.getDrawable(R.drawable.story1);
		
		story2 = res.getDrawable(R.drawable.story2);
		
		story3 = res.getDrawable(R.drawable.story3);
		
		story4 = res.getDrawable(R.drawable.story4);
		
		story5 = res.getDrawable(R.drawable.story5);
		
		startscreen = res.getDrawable(R.drawable.nicknewmenu);
		
		splashHandler = new Handler();
		
		invalidategrape = new Runnable () { public void run () {
			
			grapevinesplash = false;
			
			grapevinesplashimg = null;
			 
			postInvalidate();
			
		}
		
		};
		
		invalidatestory1 = new Runnable () { public void run () {
			
			story1splash = false;
			
			story1 = null;
			 
			postInvalidate();
			
		}
		
		};
		
		invalidatestory2 = new Runnable () { public void run () {
			
			story2splash = false;
			
			story2 = null;
			 
			postInvalidate();
			
		}
		
		};
		
		invalidatestory3 = new Runnable () { public void run () {
			
			story3splash = false;
			
			story3 = null;
			 
			postInvalidate();
			
		}
		
		};
		
		invalidatestory4 = new Runnable () { public void run () {
			
			story4splash = false;
			
			story4 = null;
			 
			postInvalidate();
			
		}
		
		};
		
		final MainActivity temp = (MainActivity) c;
				
			invalidatestory5 = new Runnable () { public void run () {
			
			story5splash = false;
			
			story5 = null;
			 
			postInvalidate();
			
			temp.showButtons();
			
			temp.adView.setVisibility(View.VISIBLE);
			
		}
		
		};
			invalidatebanana = new Runnable () { public void run () {
			
			bananabillsplash = false;
			
			bananabillsplashimg = null;
			
			postInvalidate();
			
		}

		};

		splashHandler.postDelayed(invalidategrape, 4000);
		
		splashHandler.postDelayed(invalidatebanana, 8000);
		
		splashHandler.postDelayed(invalidatestory1, 11000);
		
		splashHandler.postDelayed(invalidatestory2, 15000);
		
		splashHandler.postDelayed(invalidatestory3, 19000);
		
		splashHandler.postDelayed(invalidatestory4, 23000);
		
		splashHandler.postDelayed(invalidatestory5, 27000);

		myPaint = new Paint();
		
        myPaint.setColor(Color.WHITE);
        myPaint.setTextSize(14f);
        myPaint.setAntiAlias(true);
        myPaint.setFakeBoldText(true);
        myPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextAlign(Paint.Align.LEFT);
		
		
	}
	
	protected void onDraw (Canvas c) {
		
		name = ((MainActivity) context).getName();
		
		background.setBounds (0, 0, width, height);
		
		background.draw(c);
		
		startscreen.setBounds (0, 0, width, height);
		
		startscreen.draw(c);
		
		//c.drawText("Hello,  " + name + "!", 0, 50, myPaint);
		
		//banner.setBounds((int) (width * 0.0), (int) (height * -0.02), (int) (width * 1), (int) (height * 0.41));
		
		//banner.draw(c);
		
		//copyright.setBounds((int) (width * 0.52), (int) (height * 0.30), (int) (width * 0.91), (int) (height * 0.36));
		
		//copyright.draw(c);
		
		//play.setBounds ((int) (width * 0.15), (int) (height * 0.34), (int) (width * 0.85), (int) (height * 0.54));
		
		//play.draw(c);
		
		//rules.setBounds ((int) (width * 0.15), (int) (height * 0.52), (int) (width * 0.85), (int) (height * 0.72));
		
		//rules.draw(c);
		
		//highscores.setBounds ((int) (width * 0.15), (int) (height * 0.70), (int) (width * 0.85), (int) (height * 0.90));
		
		//highscores.draw(c);
		
		//credits.setBounds ((int) (width * 0.35), (int) (height * 0.92), (int) (width * 0.65), (int) (height * 0.99));
		
		//credits.draw(c);
		
		//bunch.setBounds ((int) (width * 0.86), (int) (height * 0.85), (int) (width * 0.99), (int) (height * 0.98));
		
		//bunch.draw(c);
		
		musicsymbol.setBounds ((int) (width * 0.01), (int) (height * 0.88), (int) (width * 0.13), (int) (height * 0.98));
		
		//musicsymbol.draw(c);
		
		if (music == false) {
			
			nosymbol.setBounds ((int) (width * 0.01), (int) (height * 0.88), (int) (width * 0.13), (int) (height * 0.98));
			
			//nosymbol.draw(c);
			
		}
		
		sfxsymbol.setBounds ((int) (width * 0.86), (int) (height * 0.88), (int) (width * 0.99), (int) (height * 0.98));
		
		//sfxsymbol.draw(c);
		
		if (sfx == false) {
			
			nosymbol.setBounds ((int) (width * 0.86), (int) (height * 0.88), (int) (width * 0.99), (int) (height * 0.98));
			
			//nosymbol.draw(c);
			
		}
		
		if (story5splash) {
			
			story5.setBounds (0, 0, width, height);
		
			story5.draw(c);
			
		}
		
		if (story4splash) {
			
			story4.setBounds (0, 0, width, height);
		
			story4.draw(c);
			
		}

		if (story3splash) {
	
			story3.setBounds (0, 0, width, height);

			story3.draw(c);
	
		}

		if (story2splash) {
	
			story2.setBounds (0, 0, width, height);

			story2.draw(c);
	
		}

		if (story1splash) {
	
			story1.setBounds (0, 0, width, height);

			story1.draw(c);
	
		}
		
		if (bananabillsplash) {
			
			bananabillsplashimg.setBounds (0, 0, width, height);
		
			bananabillsplashimg.draw(c);
			
		}
		
		if (grapevinesplash) {
			
			grapevinesplashimg.setBounds (0, 0, width, height);
		
			grapevinesplashimg.draw(c);
			
		}
		
	}
	
	public boolean onTouchEvent (MotionEvent e) {
		
		if (!bananabillsplash && !grapevinesplash && story5splash) {
		
			splashHandler.post(invalidategrape);
		
			splashHandler.post(invalidatebanana);
		
			splashHandler.post(invalidatestory1);
		
			splashHandler.post(invalidatestory2);
		
			splashHandler.post(invalidatestory3);
		
			splashHandler.post(invalidatestory4);
		
			splashHandler.post(invalidatestory5);
		
		}
		
		if (!bananabillsplash && !grapevinesplash && !story5splash) {
		
		switch (e.getAction()) {
		
		case MotionEvent.ACTION_UP :
			
			if (e.getX() > (width * 0.30) && e.getX() < (width * 0.70) && e.getY() < (height * 0.9)) {
				
				if (e.getY() > (height * ((double)488/792)) && e.getY() < (height * ((double)550/792))) {
					
					Intent intent = new Intent(context, LevelSelect.class);
					
					intent.putExtra("sfx", sfx);
					
					intent.putExtra("musicIntent", i);
					
					context.startActivity(intent);
					
				} // put instructions and high scores below
				
				if (e.getY() > (height * ((double)555/792)) && e.getY() < (height * ((double)620/792))) {
					
					Intent intent = new Intent(context, GamePlay.class);
					
					intent.putExtra("arcademode", true);
					
					//intent.putExtra("musicIntent", i);
					
					context.startActivity(intent);
					
				}
				
					if (e.getY() > (height * ((double)620/792)) && e.getY() < (height * ((double)690/792))) {
					
					Intent intent = new Intent(context, BillSocial.class);
					
					intent.putExtra("tab", 1);
					
					intent.putExtra("musicIntent", i);
					
					context.startActivity(intent);
					
				} 
				
			}
			
			else if (e.getY() > (height * 0.90) && e.getY() < (height * 1)) {
					
				/*if (e.getX() > (width * 0.35) && e.getX() < (width * 0.65)) {
				
					Intent intent = new Intent(context, Credits.class);
					
					intent.putExtra("musicIntent", i);
					
					context.startActivity(intent);
					
				}*/
				
				if (e.getX() < (width * 0.15)) { // if in same columns as music toggle
					
					if (music == true) {
						
						MainActivity dummy = (MainActivity) getContext();
						
						dummy.stopMusicService();
						
						music = false;
						
						invalidate();
						
					}
					
					else if (music == false) {
						
						MainActivity dummy = (MainActivity) getContext();
						
						dummy.startMusicService();
						
						music = true;
						
						invalidate();
						
					}
					
				}
				
				else if (e.getX() > (width * 0.86) && e.getX() < (width * 0.99)) { 
					
					if (sfx == true) {
						
						//MainActivity dummy = (MainActivity) getContext();
						
						//dummy.stopMusicService();
						
						sfx = false;
						
						invalidate();
						
					}
					
					else if (sfx == false) {
						
						//MainActivity dummy = (MainActivity) getContext();
						
						//dummy.startMusicService();
						
						sfx = true;
						
						invalidate();
						
					}
					
				}
					
			} 
			
			
				
		}
		
	}//end of check for if splash screen sequence is finished
			
		
		return true;
		
	}

}



