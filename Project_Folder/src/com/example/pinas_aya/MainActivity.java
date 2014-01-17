package com.example.pinas_aya;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	private GameManager sharedData;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sharedData = GameManager.getInstance();
		sharedData.setContext(getApplicationContext()); // this only needs to be called once
		
		//this.set
		
		// This will load the answer
		parseXml();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	
	// This example shows an Activity, but you would use the same approach if
	// you were subclassing a View.
	@Override
	public boolean onTouchEvent(MotionEvent event){ 
	        
	    int action = MotionEventCompat.getActionMasked(event);
	        
	    switch(action) {
	        case (MotionEvent.ACTION_DOWN) :
	            //Log.d(DEBUG_TAG,"Action was DOWN");
	            return true;
	        case (MotionEvent.ACTION_MOVE) :
	            //Log.d(DEBUG_TAG,"Action was MOVE");
	            return true;
	        case (MotionEvent.ACTION_UP) :
	            //Log.d(DEBUG_TAG,"Action was UP");
	        	
	        	goToMainMenu();
	        	
	            return true;
	        case (MotionEvent.ACTION_CANCEL) :
	            //Log.d(DEBUG_TAG,"Action was CANCEL");
	            return true;
	        case (MotionEvent.ACTION_OUTSIDE) :
	            //Log.d(DEBUG_TAG,"Movement occurred outside bounds " + "of current screen element");
	            return true;      
	        default : 
	            return super.onTouchEvent(event);
	    }      
	}
	
	void goToMainMenu()
	{
		// Jump into Main Menu
		// Menu Entity
		Intent mainMenu = new Intent(this, MainMenuActivity.class);
    	startActivity(mainMenu);
	}
	
	private void parseXml()
	{
		// Load XML for parsing.
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("answers.xml");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        String xml = readTextFile(inputStream);
		
        XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			//factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();

	        xpp.setInput(new StringReader (xml));
	        int eventType = xpp.getEventType();
	     
	        AnswerObject ans = new AnswerObject();
	        
	        while (eventType != XmlPullParser.END_DOCUMENT) {
	         if(eventType == XmlPullParser.START_DOCUMENT) {
	             System.out.println("Start document");
	            
	             
	         } else if(eventType == XmlPullParser.END_DOCUMENT) {
	             System.out.println("End document");
	             
	         } else if(eventType == XmlPullParser.START_TAG) {
	             System.out.println("Start tag "+xpp.getName());
	             ans = null;
	             ans = new AnswerObject();
	             
	             String cat = xpp.getAttributeValue(null, "category");
	             String lvl = xpp.getAttributeValue(null, "level");
	             String stg = xpp.getAttributeValue(null, "stage");
	             String ans1 = xpp.getAttributeValue(null, "text1");
	             String ans2 = xpp.getAttributeValue(null, "text2");
	             String ans3 = xpp.getAttributeValue(null, "text3");
	             String ans4 = xpp.getAttributeValue(null, "text4");
	             
	             if(cat != null)
	             {
	            	 ans.setCategoryNumber(Integer.valueOf(cat));
	             }
	             else 
	             {
	            	 System.out.println("Category is Null");
	             }
	             
	             if(lvl != null)
	             {
	            	 ans.setLevelNumber(Integer.valueOf(lvl));
	             }
	             else
	             {
	            	 System.out.println("Level is Null");
	             }
	             
	             if(stg != null)
	             {
	            	 ans.setStageNumber(Integer.valueOf(stg));
	             }
	             else
	             {
	            	 System.out.println("Stage is Null");
	             }
	             
	             ans.setAnswer1(ans1);
	             ans.setAnswer2(ans2);
	             ans.setAnswer3(ans3);
	             ans.setAnswer4(ans4);
	             
	             sharedData.addAnswer(ans);
	             
	             ans = null;
	             
	         } else if(eventType == XmlPullParser.END_TAG) {
	             System.out.println("End tag "+xpp.getName());
	             
	             
	             
	         } else if(eventType == XmlPullParser.TEXT) {
	             System.out.println("Text "+xpp.getText());
	         }
	         eventType = xpp.next();
	        }
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String readTextFile(InputStream inputStream) {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	    byte buf[] = new byte[1024];
	    int len;
	    try {
	        while ((len = inputStream.read(buf)) != -1) {
	            outputStream.write(buf, 0, len);
	        }
	        outputStream.close();
	        inputStream.close();
	    } catch (IOException e) {

	    }
	    return outputStream.toString();
	}


}
