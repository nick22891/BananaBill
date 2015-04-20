package com.projectgrapevine.bananabill;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.ProfilePictureView;

public class BillSocial extends Activity {
	
	private UiLifecycleHelper uiHelper;
	
	boolean friendsloaded;
	
	String accessTokenString;
	
	public ScrollView hostScrollView;
	
	private ImageView myTabs;
	
	private FrameLayout masterLayout;
	
	private FrameLayout BSocialLayout;
	
	private BillSocialView BSocialView;
	
	private ProfilePictureView myProfilePic;
	
	private ProfilePictureView [] friendsPics;
	
	public Session.StatusCallback myCallback;
	
	public Request.Callback friendsCallback;
	
	private BitmapDrawable pic;
	
	private AnimationDrawable loadinganim;
	
	private ImageView animview;
	
	Request getFriendsWithApp;
	
	public String [] friends;
	
	public String [] fnames;
	
	public String [] lnames;
	
	public String userid, userfname, userlname;
	
	public int [] levelsArray;
	
	public String [] levelsArrayString;
	
	boolean fbloaded = false;
	
	int maxlevel, maxscore;
	
	String data;
	
	String tempCSV;
	
	String responseString;
	
	File f = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill/lvl.txt");
	
	File f2 = new File(Environment.getExternalStorageDirectory(), "Android/data/com.projectgrapevine.bananabill/arcadehighscore.txt");
	
	String name, id, id2;
	
	Bitmap dummy;
	
	Context c;
	
	int width, height;
	
	HandlerThread internetThread;
	
	Handler internetHandler;
	
	Runnable inetquery;
	
	Intent intent;
	
	int tab = 1;
	
	@SuppressWarnings("deprecation")
	protected void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
        uiHelper = new UiLifecycleHelper(this, myCallback);
		
        uiHelper.onCreate(savedInstanceState);		
        
		intent = getIntent();
        
        tab = intent.getIntExtra("tab", 1);
		
		//Settings.setPlatformCompatibilityEnabled(true);
		
		internetThread = new HandlerThread("InternetThread");
		
		internetThread.start();
		
		Looper looper = internetThread.getLooper();
		
		internetHandler = new Handler(looper);
		
		c = this;
		
		levelsArray = new int[20];
		
		levelsArrayString = new String[20];
		
		tempCSV = "placeholder";
		
		friendsloaded = false;
		
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
		
		animview = new ImageView(this);
		
		animview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
				
				FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams((int)(width * 0.33), (int)(width * 0.33));
				params2.setMargins((int)(width * 0.33), (int)((double)(height - (width * 0.33))/2), (int)(width * 0.66), (int)(((double)(height - (width * 0.33))/2) + (width * 0.33)));
				
				//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
				
				params2.gravity = Gravity.TOP;
				
				animview.setLayoutParams(params2);

		    	//starAnimation.setBounds((int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14));

		Resources res = this.getResources();
		

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
		
        	try {
            
            BufferedReader buf = new BufferedReader(new FileReader(f2));

            //message = "Grrr.";
            
            data = buf.readLine();
            
            maxscore = Integer.parseInt(data);
            
            buf.close();
            
            }
            
            catch(Exception e) {
            	
            	e.printStackTrace();
            	
            	maxscore = 0;
            	
            }
		
        
 		dummy = BitmapFactory.decodeResource(c.getResources(), R.drawable.bananabillicon2);
		
		name = id = "*";
		
		hostScrollView = new ScrollView(this);
		
		hostScrollView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 
				FrameLayout.LayoutParams.FILL_PARENT));
		
		BSocialLayout = new FrameLayout(this);
		
		masterLayout = new FrameLayout(this);
		
		if (android.os.Build.VERSION.SDK_INT >= 11) hostScrollView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		
		BSocialLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
				FrameLayout.LayoutParams.WRAP_CONTENT)); 
		
		masterLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
				FrameLayout.LayoutParams.WRAP_CONTENT)); 
		
		hostScrollView.setFillViewport(true);
		
		hostScrollView.setWillNotDraw(false);
		
		if (tab == 1) BSocialView = new BillSocialView(this, width, height, maxlevel);
		
		else if (tab == 2) BSocialView = new BillSocialView(this, width, height, maxscore);
		
		BSocialView.setLayoutParams(new FrameLayout.LayoutParams(width, 
                (int)(height*5.5)));
		
		myProfilePic = new ProfilePictureView(this);
		
		myProfilePic.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
				
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(25 * ((int)((double)width/240)), 25 * ((int)((double)width/240)));
				
				params.setMargins((int)(21 * (((double)width/240))), (int) (55 * (((double)height/320))), (int) (50 * (((double)width/240))), (int)(55 * (((double)height/320)) +  (int) (60 * (((double)width/240)))));
				//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
				
				params.gravity = Gravity.TOP;
				
				myProfilePic.setLayoutParams(params);
				
				myProfilePic.setDefaultProfilePicture(dummy);
				
				myProfilePic.setPresetSize(ProfilePictureView.SMALL);
				//
				friendsPics = new ProfilePictureView[20];
			
				hostScrollView.addView(BSocialLayout);
				//
				BSocialLayout.addView(BSocialView);
				
				masterLayout.addView(hostScrollView);
				
				fnames = new String[20];
				
				lnames = new String[20];
				
				for (int j = 0;j < 20;j++) {
				
				fnames[j] = "";
				
				lnames[j] = "";
					
				levelsArrayString[j] = "";
				
				levelsArray[j] = 0;
				
				friendsPics[j] = new ProfilePictureView(this);
				
				/*myProfilePic.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT));
						
						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(25 * ((int)((double)width/240)), 25 * ((int)((double)width/240)));
						
						params.setMargins((int)(21 * (((double)width/240))), (int) (55 * (((double)height/320))), (int) (50 * (((double)width/240))), (int)(55 * (((double)height/320)) +  (int) (60 * (((double)width/240)))));
						//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
						
						params.gravity = Gravity.TOP;*/
				
				friendsPics[j].setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT));
						
						FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(25 * ((int)((double)width/240)), 25 * ((int)((double)width/240)));
						
						params3.gravity = Gravity.TOP;
						// csac
						params3.setMargins((int) (21 * (((double)width/240))), (int) (55 * (((double)height/320))) + (int)(((30 * (((double)width/240)))* j)), (int)(50 * (((double)width/240))), (int)(175 * (((double)height/320)) +  (int)(60 * (((double)width/240)))* j));
						
						//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
						
						friendsPics[j].setLayoutParams(params3);
						
						friendsPics[j].setDefaultProfilePicture(dummy);
						
						friendsPics[j].setPresetSize(ProfilePictureView.SMALL);
						
						friendsPics[j].setVisibility(View.INVISIBLE);
						
						//
						
						BSocialLayout.addView(friendsPics[j]);
				
				}
		
		BSocialLayout.addView(myProfilePic);
		
		BSocialLayout.addView(animview);
		
		myTabs = new ImageView(this);
		
		myTabs.setLayoutParams(new Gallery.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				  FrameLayout.LayoutParams.WRAP_CONTENT));
				
				FrameLayout.LayoutParams params5 = new FrameLayout.LayoutParams((int)(width * 1), (int)(height * 0.15));
				params5.setMargins((int)(width * 0), (int)(height * 0.85), (int)(width * 1), (int)(height * 1));
				
				params5.gravity = Gravity.TOP;
				
				//(int)(width * 0.20), (int)(height * 0.01), (int)(width * 0.80), (int)(height * 0.14)
				
				myTabs.setLayoutParams(params5);
		
				myTabs.setBackgroundResource(R.drawable.tabs);
				
				masterLayout.addView(myTabs);
		
		setContentView(masterLayout);
		
		myProfilePic.setVisibility(View.INVISIBLE);
		
		
		//GET FRIENDS
		
		

		friendsCallback = new Request.Callback () {
			
			@Override
			public void onCompleted (Response r) {
				
				FacebookRequestError myErr = r.getError();
				
				//Log.d("fberr", myErr.getErrorMessage());
				
				//Log.d("fberr2", r.toString());
				
				GraphObject g = r.getGraphObject();
				
				//Log.d("FBERROR", r.getError().getErrorMessage());
				
				JSONObject jso = g.getInnerJSONObject();
				
				Log.d("point", "point");
								 
				JSONArray arr = new JSONArray();
				
				JSONObject obj = new JSONObject();
				
				String id = "";
						
				try {arr = jso.getJSONArray("data");} catch (Exception e) {Log.d("err1", "err1");}
				
				int friendcount = 0;
				
				for (int i = 0;i<arr.length();i++) {
				
				try {obj = arr.getJSONObject(i);} catch (Exception e) {Log.d("err2", "err2");}
				
				try {id = obj.getString("id");} catch (Exception e) {Log.d("err3", "err3");}
				
				Log.d("Here's the id", id);
				
				try {
					
					if (obj.getBoolean("installed")) {
				
					//start creating long string csv for subsequent splitting
					
					if (friendcount == 0) tempCSV = id;
					
					else tempCSV = tempCSV + "," + id;

					Log.d("tempcsv", tempCSV);
					
					try {fnames[friendcount] = obj.getString("first_name");} catch (Exception e) {Log.d("err5", "err5");}
					
				try {lnames[friendcount] = obj.getString("last_name");} catch (Exception e) {Log.d("err6", "err6");}
					      					
					
					friendcount++;
					
					}
				
				}catch (Exception e) {Log.d("err4", "err4");}
				
				
				}
				
				tempCSV = tempCSV + "," + userid;
				
				friendcount++;
								
				BSocialView.friendcount = friendcount;
				
				BSocialView.csvString = tempCSV;
				
				
				friends = tempCSV.split(",");
				
				fnames[friends.length - 1] = userfname;
				
				lnames[friends.length - 1] = userlname;
				
				levelsArray[friends.length - 1] = maxlevel;
				
				//then add my stuff to the index of the other arrays that corresponds to the size of friends array
				
				Log.d("friends", tempCSV);
				
				final String idForRunnable = new String(id2);
				
				//send maxlevel and csv string to server
				//expect csv string in response containing the maxlevels of all submitted
				inetquery = new Runnable () {
					 
					public void run () {
						
						HttpClient httpclient = new DefaultHttpClient();
				
						HttpGet query = new HttpGet();
						
						if (tab == 1) query = new HttpGet("http://www.nickjwill.com/socialserver/query.php?maxlevel=" + Integer.toString(maxlevel) + "&user_id=" + idForRunnable + "&csvstring=" + tempCSV);
				//asdcsac
						else if (tab == 2) query = new HttpGet("http://www.nickjwill.com/socialserver/query2.php?maxscore=" + Integer.toString(maxscore) + "&user_id=" + idForRunnable + "&csvstring=" + tempCSV);
						
						try {HttpResponse response = httpclient.execute(query);responseString = EntityUtils.toString(response.getEntity());}catch (Exception e) {Log.e("MYAPP1", "exception", e);}
				//
						try {
						
				//String responseStr = EntityUtils.toString(response.getEntity());
						levelsArrayString = responseString.split(",");
				//REMEMBER TO GO BACK AND INITIALIZE THE TWO ARRAYS
						for (int k=0;k<friends.length;k++){
					
							Log.e("Int : ", levelsArrayString[k]);
							
							levelsArrayString[k] = levelsArrayString[k].replaceAll("\\D+","");
							
							levelsArray[k] = Integer.parseInt(levelsArrayString[k]);
					
						}
						
						int swapcounter = 1;

						int buf = 0;
						
						String buf2, buf3, buf4 = "";

						while (swapcounter != 0) {
						
							swapcounter = 0;
						
							for (int i=(levelsArray.length - 1);i > 0;i--) {
						
								if (levelsArray[i] > levelsArray[i-1]) {
							
									swapcounter++;
							
									buf = levelsArray[i-1];
								
									levelsArray[i-1] = levelsArray[i];
								
									levelsArray[i] = buf;
									
									buf2 = fnames[i-1];
								
									fnames[i-1] = fnames[i];
								
									fnames[i] = buf2;
									
									buf3 = lnames[i-1];
								
									lnames[i-1] = lnames[i];
								
									lnames[i] = buf3;
									
									buf4 = friends[i-1];
								
									friends[i-1] = friends[i];
								
									friends[i] = buf4;
							
								}
						
						
							}
						
						}
						
						fbloaded = true;						
						
      				    animview.setVisibility(View.INVISIBLE);
				
      				  
      				    BSocialView.setBackgroundColor(0x000000);
      				    
						BSocialView.postInvalidate();
						
						}
						
						catch (Exception e) {
							
							
							
						}
						
					}
					
				};
				
				internetHandler.post(inetquery);
				
				BSocialView.setBackgroundColor(0x000000);
				
				BSocialView.postInvalidate();
				
			
			}
			
		};
		
		//END OF GET FRIENDS
				
		myCallback = new Session.StatusCallback() {
	        	
	        	//called when session changes state
	        	@SuppressWarnings("deprecation")
				public void call(Session session, SessionState state, Exception exception) {
	        		
	        		Log.e("mycallback", "runs!");
	        		
	        		if (session.isOpened()) {
	        			
	        			Log.e("Session:", "is opened");
	        			
	        			if (accessTokenString != null) Log.d("access_token", accessTokenString);
	        			
	        			else Log.d("access_token", "null");
	        			
	        			Bundle params = new Bundle();
	        			
	        			params.putString("fields", "installed,first_name,last_name");

	        	        //params.putString("access_token", accessTokenString);
	        	        
  	        			getFriendsWithApp = new Request(session, "me/friends", params, HttpMethod.GET, friendsCallback);
  	        				        			
	        			// make request to the /me API
	        			if (!fbloaded) Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	        			  // callback after Graph API response with user object
	        			  @Override	        			  public void onCompleted(GraphUser user, Response response) {

	        				 
	        				  try {
	        					  
	        				userid = user.getId();
	        				
	        				userfname = user.getFirstName();
	        				
	        				userlname = user.getLastName();
	        				   
	        				  name = user.getFirstName() + " " + user.getLastName();

	        				  if (!friendsloaded) getFriendsWithApp.executeAsync();
	        				  
	        				  id = user.getId();
	        				  
	        				  id2 = new String(id);
	        				  
	        				  myProfilePic.setProfileId(id);
	        				 
	        				  
	        				  //myProfilePic.setVisibility(View.VISIBLE);
	        				  	        				  
	        				  //fbloaded = true;
	        				  
	        				  BSocialView.postInvalidate();
	        				  
	        				  }
	        				  
	        				  catch (Exception e) {
	        					  
	        					  //networkerror = true;
	        					  
	        				  };
	        				  
	        			  }
	        			  
	        			});
	        			
	        		}
	        		
	        	}
	        	
	        	
	        };
	        
	        Session session = new Session(this);
	        
	        Session.setActiveSession(session);
	        
	        Session.OpenRequest request = new Session.OpenRequest(this);
	        
	        request.setPermissions(Arrays.asList("public_profile","user_friends"));
	        
	        request.setCallback(myCallback);
	        
	        session.openForRead(request);
	        
	        //Session session = Session.openActiveSession(this, true, myCallback);
	        /*
	        Session.NewPermissionsRequest newPermissionsRequest = new Session
	        	      .NewPermissionsRequest(this, Arrays.asList("user_friends"));
	        	    session.requestNewReadPermissions(newPermissionsRequest);*/

	        //Session.OpenRequest request = new Session.OpenRequest(this);
	        
	        //request.setPermissions("user_friends"); 
	        
	        // get active session
	        /*Session mFacebookSession = Session.getActiveSession();
	        if (mFacebookSession == null || mFacebookSession.isClosed()) 
	        {
	            mFacebookSession = new Session(this);
	        
	        
	        mFacebookSession.addCallback(myCallback);
	        
	        }*/
	        
	        //mFacebookSession.openForRead(request);
	        
	        //accessTokenString = mFacebookSession.getAccessToken();
	    
	        
		
		
	}
	
	public void showProfilePictures () {
		

		if (!friendsloaded) {
		
			
			
			
			
		//friendsPics = new ProfilePictureView[friends.length];
		
		for (int j = 0; j < friends.length;j++) {
			
			Log.d("Runs!", "Runs!");
			
			Log.d("ID", friends[j]);
			
			Log.d("FriendL", Integer.toString(friends.length));
					
					Log.d("About to set : ", friends[j]);
					
					//setContentView(BSocialLayout);
					
					friendsPics[j].setProfileId(friends[j]);//maybe introduce a delay before trying to set this?
					
					friendsPics[j].setVisibility(View.VISIBLE);
					
					friendsloaded = true;
											
		}
		
		}//end if
		
		
	}

	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	      
	      name = "activity result runs";
	      
	      Log.e("name", name);
	      
	      //start refreshing view
	      
	      internetHandler.post(new Runnable () {public void run () {hostScrollView.postInvalidate();BSocialView.postInvalidate();internetHandler.postDelayed(this,  100);}});
	      
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	      
	      accessTokenString = Session.getActiveSession().getAccessToken();
	      	      
	  }
	
	public void onResume () {
		
		super.onResume();
		
		uiHelper.onResume();
		
		animview.setBackgroundResource(R.anim.loadinganim);
    	
    	loadinganim = (AnimationDrawable) animview.getBackground();

    	loadinganim.start();
    	
		
	}
	
	public void onStart () {
		
		super.onStart();
		
		//uiHelper.onStart();
		
	}
	
	public void onDestroy () {
		
		super.onDestroy();
		
		uiHelper.onDestroy();
		
	}
	
	public void onPause () {
		
		super.onPause();
		
		uiHelper.onPause();
		
	}
	
	public void reloadWithLevels () {
    	
		finish();
    	
		Intent intent = new Intent(this, BillSocial.class);
		
		intent.putExtra("tab", 1);
    	
    	startActivity(intent);
    	
    }
	
	public void reloadWithScores () {

    	finish();
    	
		Intent intent = new Intent(this, BillSocial.class);
		
		intent.putExtra("tab", 2);
    	
    	startActivity(intent);
    	
    }
	
public void reloadWithShare () {
    	
		/*Intent intent = new Intent(this, BillSocial.class);
		
		intent.putExtra("tab", 3);
    	    	
    	finish();
    	
    	startActivity(intent);*/
	    
    	FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
    	.setApplicationName("Banana Bill")
    	.setLink("https://play.google.com/store/apps/details?id=com.projectgrapevine.bananabill&hl=en")
    	.setDescription("Help Banana Bill escape Attila the evil rhino and retrieve the Quantum Banana!")
    	.setPicture("http://www.nickjwill.com/bbbutton.png")
        .build();
    	uiHelper.trackPendingDialogCall(shareDialog.present());
	
    	
    }
	
	/*public void reloadWithLevels(){
		
		Log.d("levels", "levels");
		
	}
	
	public void reloadWithScores(){
		
		Log.d("scores", "scores");
		
	}
	
	public void reloadWithShare(){
		
		Log.d("share", "share");
		
	}*/
	
	public boolean dispatchTouchEvent(MotionEvent event) {
		   int eventaction=event.getAction();

		    switch(eventaction) {
		      case MotionEvent.ACTION_UP:
		    	  
		    	  if (event.getY() > ((double)height * 0.85)) {
		    		  
		    		  if (event.getX() > (width * 0) && event.getX() < ((double) width * 0.33)) reloadWithLevels();
		    		  
		    		  else if (event.getX() > ((double)width * 0.33) && event.getX() < ((double) width * 0.66)) reloadWithScores();
		    		  
		    		  else if (event.getX() > ((double)width * 0.66) && event.getX() < ((double) width)) reloadWithShare();
		    		  
		    	  }
		          //reg.setText("hey");
		          break;
		      default:
		          break;
		    }

		    return super.dispatchTouchEvent(event);
		}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	
}

class BillSocialView extends View {
	
	private Drawable background, userpic, blueheader;
	
	Typeface pressStart;
	
	String name, id;
	
	Paint myPaint;
	
	Path myPath;
	
	String csvString;
	
	Context context;
	
	float textsize;
	
	int width, height, maxlevel, friendcount;
	
	BillSocialView (Context c, int w, int h, int m) {
		
		super(c);
		
		this.setWillNotDraw(false);
		
		this.width = w;
		
		this.height = h;
		
		this.maxlevel = m;
		
		this.context = c;
		
		friendcount = 0;
		
		csvString = "";
		
		Resources res = this.getResources();
		
		pressStart = Typeface.createFromAsset(c.getAssets(), "PrStart.ttf");
		
		background = res.getDrawable(R.drawable.bg);
		
		blueheader = res.getDrawable(R.drawable.blueheader);
		
		myPaint = new Paint();
		
		textsize = (float) (8 * ((double)width / 320));
		
		myPaint.setTypeface(pressStart);
        myPaint.setColor(Color.WHITE);
        myPaint.setTextSize(textsize);
        myPaint.setAntiAlias(true);
        myPaint.setFakeBoldText(true);
        myPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextAlign(Paint.Align.LEFT);
        
        //RectF myOval = new RectF((int) ((-1) * (width * 0.2)), 0, (int) (width * 1.2), width);
        
       // myPath = new Path();
        
        //myPath.addArc(myOval, -106, 36);
        
        
        this.setBackgroundColor(0x000000);
        
		name = ((BillSocial) context).name;
		
		id = ((BillSocial) context).id;
		
		if (android.os.Build.VERSION.SDK_INT >= 11) setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        
	}
	
	protected void onDraw (Canvas c) {
		
		myPaint.setColor(Color.BLACK);
		
		c.drawRect(0, 0, width, (float) ((double)height * 5.5), myPaint);
		
		myPaint.setColor(Color.WHITE);
		
		name = ((BillSocial) context).name;
		
		id = ((BillSocial) context).id;
		
		//blueheader.setBounds((int) (width * 0.2), 0, (int) (width * 0.8), (int) (height * 0.12));
		
		//blueheader.draw(c);
		
		
		//background.setBounds (0, 0, 240, 320);
		
		//background.draw(c);
		
		myPaint.clearShadowLayer();
        
		textsize = (float) (12 * ((double) width / 240));
		
		myPaint.setTextSize(textsize);
		
        //c.drawTextOnPath("SOCIAL", myPath, (int) (width * 0.025), (int) (height * 0.055), myPaint);
		
		c.drawText("BILL SOCIAL", (float) (58 * ((double)width/240)), (float) (height * 0.045), myPaint);
		
		textsize = (float) (7 * ((double) width / 240));
		
		myPaint.setTextSize(textsize);
		
		c.drawText("LEADERBOARD", (float) (80 * ((double)width/240)), (float) (height * 0.075), myPaint);
		
        myPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
		//((BillSocial) context).pic.setBounds(50, 150, 100, 200);
        textsize = (float) (9 * ((double) width / 320));
		//((BillSocial) context).pic.draw(c);
        myPaint.setTextSize(textsize);
        
        c.drawText("RANK", (float) (10 * ((double)width/240)), (float)(50 * ((double)height / 320)), myPaint);
        
        c.drawText("NAME", (float) (50 * ((double)width/240)), (float)(50 * ((double)height / 320)), myPaint);
        
        if (((BillSocial) context).tab == 1) c.drawText("LEVEL", (float) (200 * ((double)width/240)), (float)(50 * ((double)height / 320)), myPaint);
        
        else c.drawText("SCORE", (float) (200 * ((double)width/240)), (float)(50 * ((double)height / 320)), myPaint);
        
        boolean fbloaded = ((BillSocial) context).fbloaded;
        
        if (fbloaded) {
        	
        	((BillSocial) context).showProfilePictures();
        
        textsize = (float) (11 * ((double)width / 320));
    		//((BillSocial) context).pic.draw(c);
        myPaint.setTextSize(textsize);
        /*
        c.drawText("1", (float) (7 * ((double) width / 240)), (float)(65 * ((double)height / 320)), myPaint);
        
		c.drawText(name.toUpperCase(), (float) (50 * ((double) width / 240)), (float)(65 * ((double)height / 320)), myPaint);
		
		c.drawText("" + Integer.toString(maxlevel), (float) (200 * ((double) width/240)), (float)(65 * ((double)height / 320)), myPaint);
		*/
		//c.drawText(Integer.toString(friendcount), 150, 150, myPaint);
		
		//c.drawText(csvString, 20, 180, myPaint);
        String name;
        
		for (int i = 0;i < ((BillSocial) context).friends.length;i++) {
			
			if (!((BillSocial) context).fnames[i].equals("")) {
			
				c.drawText("" + (i+1) + "", (float) (7 * ((double) width / 240)),  (float) ((65 * ((double)height / 320)) + (i * (30 * ((double)width/240)))), myPaint);
				
				name = ((BillSocial) context).fnames[i].toUpperCase() + " " + ((BillSocial) context).lnames[i].toUpperCase();
				
				c.drawText(name.substring(0, Math.min(name.length(), 17)), (float) (50 * ((double) width / 240)),  (float) ((65 * ((double)height / 320)) + (i * (30 * ((double)width/240)))), myPaint);
			
				if (((BillSocial) context).levelsArray[i] == 0) c.drawText("", (float) (200 * ((double) width / 240)),  (float) ((65 * ((double)height / 320)) + (i * (30 * ((double)width/240)))), myPaint);
			
				else c.drawText("" + Integer.toString(((BillSocial) context).levelsArray[i]), (float) (200 * ((double) width / 240)),  (float) ((65 * ((double)height / 320)) + (i * (30 * ((double)width/240)))), myPaint);
				
			}
			
		}
		
		
		
		
        }
		
		//c.drawText(id,  50,  100, myPaint);
        
        invalidate();
        ((BillSocial)context).hostScrollView.postInvalidate();
		
	}
	
}
