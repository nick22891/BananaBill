package com.projectgrapevine.bananabill;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import com.flurry.android.FlurryAgent;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class LevelSelect extends Activity {
	
	private FrameLayout myLayout;
	
	private ScrollView hostScrollView;
	//private LinearLayout lvlLayout;
	
	private LevelSelectView lView;
	
	public int width, height, maxlevel;
	
	MediaPlayer myClick;
	
	String data;
	
	Intent i; //for media player
	
	boolean sfx = true;
	
	boolean musicStopped = false;
	
	File f = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill/lvl.txt");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    			
        super.onCreate(savedInstanceState);
    	//Bitmap myBitmap = new Bitmap
    	musicStopped = false;
    	
    	Intent intent = getIntent();
    	
    	sfx = intent.getBooleanExtra("sfx", true);
    	
    	i = (Intent)intent.getParcelableExtra("musicIntent");
    	
		myClick = MediaPlayer.create(this, R.raw.click);
    	        
        if (sfx) myClick.start();
        
        myLayout = new FrameLayout(this);
        
        Display display = getWindowManager().getDefaultDisplay();
        
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
        
        try {
            
            BufferedReader buf = new BufferedReader(new FileReader(f));

            //message = "Grrr.";
            
            data = buf.readLine();
            
            maxlevel = Integer.parseInt(data);
            
            buf.close();
            
            }
            
            catch(Exception e) {
            	
            	e.printStackTrace();
            	
            	maxlevel = 1;
            	
            }
              
        lView = new LevelSelectView(this, width, height, maxlevel);
        
        lView.setLayoutParams(new FrameLayout.LayoutParams(width, 
                (int)(height*1.55)));
        
        hostScrollView = new ScrollView(this);
		
		hostScrollView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 
				FrameLayout.LayoutParams.FILL_PARENT));
		
		if (android.os.Build.VERSION.SDK_INT >= 11) hostScrollView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        
		hostScrollView.setFillViewport(true);
        
       
        
        myLayout.addView(lView);
        
        hostScrollView.addView(myLayout);
        
        setContentView(hostScrollView);
        
                               
    }
    
    //public void startGame () {
    	
    //	myLayout.addView(mView);
    	
   // }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.level_select, menu);
        return true;
    }
    
    public int getScrollY(){
    	
    	//return hostScrollView.getScrollY();    	
    	
    	return 0;
    	
    }
    
   protected void onResume () {
	   
	   super.onResume();
	   
	   myLayout.removeView(lView);
	   
	   lView = null;
	   
	   this.onCreate(null);
	   
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
		
		FlurryAgent.onEndSession(this);
				
		if (!isAppForeground()) 
			
			{
			
			stopService(i);
			
			musicStopped = true;
			
			}
		
	}
	
	public void onStart () {
		
		super.onStart();
		
		FlurryAgent.onStartSession(this, "4WSGV3PW4YTZPQDD7X8Q");
		
	}
	
    public void onRestart() {
    	
    	super.onRestart();
    	
		if (musicStopped) {
			
			startService(i);
		
			musicStopped = false;
			
		}
    	
    }
	
}

class LevelSelectView extends View {
	
	private Drawable locked, levelselectbackground, banana, panel, tinystar, tinystarempty, levelselectbanner, bgfooter;
	
	private Path myPath;
	
	Typeface pressStart;
	
	Paint myPaint;
	
	int width, height, maxLevel, banananumbergreen, billyellow;
	
	int[] highscores = new int[32];
	
	Context context;
	
	LevelSelect dummy;
	
	//private MyView gameView;
	
	LevelSelectView (Context c, int w, int h, int maxLevel) {
		
		super(c);
		
		context = c;
		
		dummy = (LevelSelect) context;
		
		this.maxLevel = maxLevel;
		
		Resources res = this.getResources();

		this.width = w;
		
		this.height = h;
		
		if (android.os.Build.VERSION.SDK_INT >= 11) setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		
		locked = res.getDrawable(R.drawable.locked);
		
		banananumbergreen = res.getColor(R.color.banananumbergreen);
		
		billyellow = res.getColor(R.color.newyellow);
		
		banana = res.getDrawable(R.drawable.apple);
		
		tinystar = res.getDrawable(R.drawable.tinystar);
		
		tinystarempty = res.getDrawable(R.drawable.tinystarempty);
		
		panel = res.getDrawable(R.drawable.panel);
		
		levelselectbanner = res.getDrawable(R.drawable.levelselectbg);
		
		bgfooter = res.getDrawable(R.drawable.bgfooter);
		
		//selectalevel = res.getDrawable(R.drawable.selectalevel);
		
		levelselectbackground = res.getDrawable(R.drawable.wallpaper);
		
		pressStart = Typeface.createFromAsset(c.getAssets(), "PrStart.ttf");
				
		myPaint = new Paint();
		
		for (int i = 0; i < 32; i++ ) {
			
			highscores[i] = 0;
			
		}
		
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
		
		float textSize = 14f;
		
		textSize = (float) (textSize * ((double) width / 240));
		
		myPaint.setTypeface(pressStart);
        myPaint.setColor(Color.rgb(255,255,255));
        myPaint.setTextSize(textSize);
        myPaint.setAntiAlias(true);
        myPaint.setFakeBoldText(true);
        myPaint.setShadowLayer(10f, 0, 0, Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextAlign(Paint.Align.LEFT);
		
        RectF myOval = new RectF((int) ((-1) * (width * 0.2)), 0, (int) (width * 1.2), width);
        
        myPath = new Path();
        
        myPath.addArc(myOval, -106, 36);
        
		levelselectbackground.setBounds(0, 0, width, (int) (height * 1));
		
		levelselectbackground.draw(c);
		
		levelselectbackground.setBounds(0, height, width, (int) (height * 2));
		
		levelselectbackground.draw(c);
				
		bgfooter.setBounds((int) (width * 0.1), (int) (height * 1.53), (int) (width * 0.9), (int) (height * 1.55));
		
		bgfooter.draw(c);
		
		//woodbackground.setBounds((int) (width * 0.02), (int) (height * 0.02), (int) (width * 0.98), (int) (height * 0.98));
		
		//woodbackground.draw(c);
		
		levelselectbanner.setBounds((int) (width * 0.2), 0, (int) (width * 0.8), (int) (height * 0.12));
		
		levelselectbanner.draw(c);
				
		myPaint.clearShadowLayer();
				        
        c.drawTextOnPath("LEVELS", myPath, (int) (width * 0.025), (int) (height * 0.055), myPaint);
		
		myPaint.setUnderlineText(true);
		
		//c.drawText("SELECT A LEVEL", (float) (width * 0.1), (float) (height * 0.1), myPaint);
		
		myPaint.setUnderlineText(false);
		
		//selectalevel.setBounds (0, 0, width, (int) (height * 0.14));
		
		//selectalevel.draw(c);
		
		int startX, endX, startY, endY, panelStartX, panelEndX, panelStartY, panelEndY;
		
		myPaint.setTextSize((float) ((0.0303) * width));
		
		myPaint.setColor(Color.BLACK);
		
		for (int i = 0;i<32;i++) {
						
			startX = (int)((0.07 * width) + ((i%4) * (width * 0.24)));
			
			startY = (int) (((i / 4) * (height * 0.175)) + (height * 0.135));
			
			endX = (int) (startX + (width * 0.15));
			
			endY =  (int) (startY + (height * 0.09));
			
			panelStartX = (int) (startX - (width * 0.03));
			
			panelEndX = (int) (endX + (width * 0.03));
			
			panelStartY = (int) (startY - (height * 0.01));
			
			panelEndY = (int) (endY + (height * 0.04));
			
			panel.setBounds(panelStartX, panelStartY, panelEndX, panelEndY);
			
			panel.draw(c);
			
			if (maxLevel > i) {
			
				banana.setBounds(startX, startY, endX, endY);
			
				banana.draw(c);
			
				if (i < 9) c.drawText(Integer.toString(i+1), (float) (startX + (width * 0.07)), (float) (startY + (height * 0.076)), myPaint);
			
				else c.drawText(Integer.toString(i+1), (float) (startX + (width * 0.056)), (float) (startY + (height * 0.076)), myPaint);
				
			}
			
			else {
				
				locked.setBounds(startX, startY, endX, endY);
				
				locked.draw(c);
				
			}
			
			for (int j = 0;j<3;j++) {
				
				tinystar.setBounds((int) (startX + (j * width * 0.05)), (int) (endY + (height * 0.004)), (int) ((startX + (j * width * 0.05)) + (width * 0.05)), (int) (endY + (height * 0.03)));
				
				tinystarempty.setBounds((int) (startX + (j * width * 0.05)), (int) (endY + (height * 0.004)), (int) ((startX + (j * width * 0.05)) + (width * 0.05)), (int) (endY + (height * 0.03)));
				
				try {
				
				if (highscores[i] > 0) {
				
					switch (j) {
					
						case 0 : if (highscores[i] > 0) tinystar.draw(c);
						
								 else tinystarempty.draw(c);
						
						break;
						
						case 1 : if (highscores[i] > 33) tinystar.draw(c);
						
								 else tinystarempty.draw(c);
						
						break;
						
						case 2 : if (highscores[i] > 66) tinystar.draw(c);
						
								 else tinystarempty.draw(c);
						
						break;
					
					}
					
					//tinystar.draw(c);
				
				}
				
				}
				
				catch (Exception e) {
					
					
				}
				
			}
			
		}
		
	}

	public boolean onTouchEvent (MotionEvent e) {
		
		int lvl = 20;
		
    	switch (e.getAction()) {

			case MotionEvent.ACTION_UP : 
	
				//setVisibility(View.GONE);
				
				if ((e.getY() + dummy.getScrollY()) > (height * 0.16) && (e.getY() + dummy.getScrollY()) < (int) (height * 0.27)) { //first row
				
					if (e.getX() > (width * 0.09) && e.getX() < (width * 0.25)) lvl = 0;
					
					else if (e.getX() > (width * 0.31) && e.getX() < (width * 0.47)) lvl = 1;
					
					else if (e.getX() > (width * 0.53) && e.getX() < (width * 0.69)) lvl = 2;
					
					else if (e.getX() > (width * 0.75) && e.getX() < (width * 0.91)) lvl = 3;
								
				}
				
				if ((e.getY() + dummy.getScrollY()) > (int) (height * 0.33) && (e.getY() + dummy.getScrollY()) < (height * 0.44)) { //second row
					
					if (e.getX() > (width * 0.09) && e.getX() < (width * 0.25)) lvl = 4;
					
					else if (e.getX() > (width * 0.31) && e.getX() < (width * 0.47)) lvl = 5;
					
					else if (e.getX() > (width * 0.53) && e.getX() < (width * 0.69)) lvl = 6;
					
					else if (e.getX() > (width * 0.75) && e.getX() < (width * 0.91)) lvl = 7;
								
				}
				
				if ((e.getY() + dummy.getScrollY()) > (int) (height * 0.50) && (e.getY() + dummy.getScrollY()) < (height * 0.61)) { //third row
					
					if (e.getX() > (width * 0.09) && e.getX() < (width * 0.25)) lvl = 8;
					
					else if (e.getX() > (width * 0.31) && e.getX() < (width * 0.47)) lvl = 9;
					
					else if (e.getX() > (width * 0.53) && e.getX() < (width * 0.69)) lvl = 10;
					
					else if (e.getX() > (width * 0.75) && e.getX() < (width * 0.91)) lvl = 11;
								
				}
				
				if ((e.getY() + dummy.getScrollY()) > (int) (height * 0.67) && (e.getY() + dummy.getScrollY()) < (height * 0.78)) { //fourth row
					
					if (e.getX() > (width * 0.09) && e.getX() < (width * 0.25)) lvl = 12;
					
					else if (e.getX() > (width * 0.31) && e.getX() < (width * 0.47)) lvl = 13;
					
					else if (e.getX() > (width * 0.53) && e.getX() < (width * 0.69)) lvl = 14;
					
					else if (e.getX() > (width * 0.75) && e.getX() < (width * 0.91)) lvl = 15;

				}
				
				if ((e.getY() + dummy.getScrollY()) > (int) (height * 0.84) && (e.getY() + dummy.getScrollY()) < (height * 0.95)) { //fifth row
					
					if (e.getX() > (width * 0.09) && e.getX() < (width * 0.25)) lvl = 16;
					
					else if (e.getX() > (width * 0.31) && e.getX() < (width * 0.47)) lvl = 17;
					
					else if (e.getX() > (width * 0.53) && e.getX() < (width * 0.69)) lvl = 18;
					
					else if (e.getX() > (width * 0.75) && e.getX() < (width * 0.91)) lvl = 19;

				}
				
				if ((e.getY() + dummy.getScrollY()) > (int) (height * 1.01) && (e.getY() + dummy.getScrollY()) < (height * 1.12)) { //sixth row
					
					if (e.getX() > (width * 0.09) && e.getX() < (width * 0.25)) lvl = 20;
					
					else if (e.getX() > (width * 0.31) && e.getX() < (width * 0.47)) lvl = 21;
					
					else if (e.getX() > (width * 0.53) && e.getX() < (width * 0.69)) lvl = 22;
					
					else if (e.getX() > (width * 0.75) && e.getX() < (width * 0.91)) lvl = 23;

				}

			if ((e.getY() + dummy.getScrollY()) > (int) (height * 1.18) && (e.getY() + dummy.getScrollY()) < (height * 1.29)) { //seventh row
	
				if (e.getX() > (width * 0.09) && e.getX() < (width * 0.25)) lvl = 24;
	
				else if (e.getX() > (width * 0.31) && e.getX() < (width * 0.47)) lvl = 25;
	
				else if (e.getX() > (width * 0.53) && e.getX() < (width * 0.69)) lvl = 26;
	
				else if (e.getX() > (width * 0.75) && e.getX() < (width * 0.91)) lvl = 27;

			}

				if ((e.getY() + dummy.getScrollY()) > (int) (height * 1.35) && (e.getY() + dummy.getScrollY()) < (height * 1.46)) {//eighth row
	
					if (e.getX() > (width * 0.09) && e.getX() < (width * 0.25)) lvl = 28;
	
					else if (e.getX() > (width * 0.31) && e.getX() < (width * 0.47)) lvl = 29;
	
					else if (e.getX() > (width * 0.53) && e.getX() < (width * 0.69)) lvl = 30;
	
					else if (e.getX() > (width * 0.75) && e.getX() < (width * 0.91)) lvl = 31;

				}
				
				//gameView.setVisibility(View.VISIBLE);
				Intent intent = new Intent(context, GamePlay.class);
										
				intent.putExtra("level", lvl);
				
				intent.putExtra("maxlevel", maxLevel);
				
				LevelSelect dummy = (LevelSelect) getContext();
				
				intent.putExtra("sfx", dummy.sfx);
				
				intent.putExtra("musicIntent", dummy.i);
				
				if (lvl < maxLevel) context.startActivity(intent);
				//
			break;
	  		    		
		}
    	
    	//invalidate();
    	    	
    	return true;
    	
	}
		
}

