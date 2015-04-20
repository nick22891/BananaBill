package com.projectgrapevine.bananabill;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class HighScores extends Activity {
	
	int width, height;
	
	MediaPlayer myClick;
	
	Intent i;
	
	boolean musicStopped = false;
	
	private HighScoresView myView;
	
	private ScrollView hostScrollView;

	private FrameLayout myLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_high_scores);
		
		Display display = getWindowManager().getDefaultDisplay();
		
		Intent intent = getIntent();
    	
    	//sfx = intent.getBooleanExtra("sfx", true);
    	
    	i = (Intent)intent.getParcelableExtra("musicIntent");
		
		myClick = MediaPlayer.create(this, R.raw.click);
        
		musicStopped = false;
		
        Point size = new Point();
        
        if (android.os.Build.VERSION.SDK_INT >= 13) { 
        	
        	display.getSize(size);
        	            
            width = size.x;
            
            height = size.y;
        	
        }
        
        else {
        	
        	width = display.getWidth();
        	
        	height = display.getHeight();
        	
        }
        
        myView = new HighScoresView (this, width, height);
        
        myView.setLayoutParams(new FrameLayout.LayoutParams(width, 
                (int)(height*3.35)));
        
        hostScrollView = new ScrollView(this);
        
        myLayout = new FrameLayout(this);
        
        hostScrollView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 
				FrameLayout.LayoutParams.FILL_PARENT));
		
		if (android.os.Build.VERSION.SDK_INT >= 11) hostScrollView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        
		hostScrollView.setFillViewport(true);
        
		myLayout.addView(myView);
        
        hostScrollView.addView(myLayout);
        
        setContentView(hostScrollView);
        
        myClick.start();
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.high_scores, menu);
		return true;
	}

	public void onBackPressed () {
		
		myClick.release();
		
		super.onBackPressed();
		
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
					
			if (!isAppForeground()) 
				
				{
				
				stopService(i);
				
				musicStopped = true;
				
				}
			
		}
		
		public void onStart () {
			
			super.onStart();
			
		}
		
	    public void onRestart() {
	    	
	    	super.onRestart();
	    	
			if (musicStopped) {
				
				startService(i);
			
				musicStopped = false;
				
			}
	    	
	    }

	
}

class HighScoresView extends View {
	
	Context context;
	
	Path myPath;
	
	int width, height;
	
	int[] highscores = new int[32];
	
	private Drawable background, highscoresbanner, leaderboard;
	
	private Paint myPaint;
	
	int billyellow;
	
	HighScoresView (Context c, int w, int h) {
		
		super(c);
		
		context = c;
		
		width = w;
		
		height = h;
		
		if (android.os.Build.VERSION.SDK_INT >= 11) setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        		
		for (int i = 0; i < 32; i++ ) {
			
			highscores[i] = 0;
			
		}
		
		Typeface pressStart = Typeface.createFromAsset(c.getAssets(), "PrStart.ttf");
		
		myPaint = new Paint();
		myPaint.setTypeface(pressStart);
        myPaint.setColor(Color.WHITE);
        myPaint.setTextSize(16f);
        myPaint.setAntiAlias(true);
        myPaint.setFakeBoldText(true);
        myPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextAlign(Paint.Align.LEFT);
		
		Resources res = this.getResources();
		
		background = res.getDrawable(R.drawable.bg);
		
		highscoresbanner = res.getDrawable(R.drawable.levelselectbg);
		
		billyellow = res.getColor(R.color.newyellow);
		
		leaderboard = res.getDrawable(R.drawable.greenleaderboard);
		
		File hscores = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill/highscores.txt");
		
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
		
			
	}
	
	protected void onDraw (Canvas c) {
		
		background.setBounds (0, 0, width, (int)(height * 3.35));
		
		background.draw(c);
		
		//bgfooter.setBounds((int) (width * 0.1), (int) (height * 0.98), (int) (width * 0.9), height);
		
		//bgfooter.draw(c);
		
		//panel.setBounds ((int) (width * -0.01), (int) (height * 0.09), (int) (width * 1.01), (int) (height * 1.01));
		
		//panel.draw(c);
		
		highscoresbanner.setBounds((int) (width * 0.025), 0, (int) (width * 0.975), (int) (height * 0.12));
		
		highscoresbanner.draw(c);
		
		leaderboard.setBounds((int) (width * 0.25), (int) (height * 3.18), (int) (width * 0.75), (int) (height * 3.34));
		
		leaderboard.draw(c);
		
		//woodbackground.setBounds((int) (width * 0.02), (int) (height * 0.02), (int) (width * 0.98), (int) (height * 0.98));
		
		//woodbackground.draw(c);
		
		/*
		 
		bunch.setBounds ((int) (width * 0.01), (int) (height * 0.85), (int) (width * 0.13), (int) (height * 0.98));
		
		bunch.draw(c);
		
		bunch.setBounds ((int) (width * 0.43), (int) (height * 0.85), (int) (width * 0.57), (int) (height * 0.98));
		
		bunch.draw(c);
		
		bunch.setBounds ((int) (width * 0.86), (int) (height * 0.85), (int) (width * 0.99), (int) (height * 0.98));
		
		bunch.draw(c);
		
		*/
			
		myPaint.setColor(Color.WHITE);
		
		//c.drawText ("HIGH SCORES", (float) (width * 0.3) , (float) (height * 0.1), myPaint);
		
		float textSize = 14f;
		
		textSize = (float) (textSize * ((double) width / 240));
		
		myPaint.setTextSize(textSize);
		
		for (int i = 0; i < 32; i++) {
		
			c.drawText("LEVEL " + (i+1) + " : " + highscores[i], (float) (width * 0.15), (float) ((height * 0.18) + ((i) * ( height * 0.095 ))), myPaint);
		
		}
		
		/*for (int i = 10; i < 20; i++) {
			
			c.drawText("LEVEL " + (i+1) + " : " + highscores[i], (float) (width * 0.52), (float) ((height * 0.16) + ((i - 10) * ( height * 0.07 ))), myPaint);
		
		}*/
		
		textSize = 14f;
		
		textSize = (float) (textSize * ((double) width / 240));
		
		myPaint.setTextSize(textSize);
		
		//myPaint.setUnderlineText(true);
		
		//c.drawText(" HIGH SCORES ", (float) (width * 0.12), (float) (height * 0.1), myPaint);
		
		myPaint.setUnderlineText(false);
		
		myPaint.clearShadowLayer();
		
        RectF myOval = new RectF((int) ((-1) * (width * 0.4)), 0, (int) (width * 1.4), width);
        
        myPath = new Path();
        
        myPath.addArc(myOval, -130, 72);
		
        c.drawTextOnPath("HIGH SCORES", myPath, (int) (width * 0.27), (int) (height * 0.055), myPaint);
        
	}

}

