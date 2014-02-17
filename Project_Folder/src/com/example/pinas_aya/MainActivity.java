package com.example.pinas_aya;


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
		
		sharedData = GameManager.getInstance(); // Getting the only instance of our GAME LOGIC
		sharedData.setContext(getApplicationContext()); // this only needs to be called once
		
		sharedData.initializeAudio(this);
		
		//sharedData.playMainBGM();
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
	
	
	@Override
	protected void onPause() {
		sharedData.playMainBGM(); // Background Music 
		super.onPause();
	}
	
	
	@Override
	protected void onResume() {
		sharedData.playMainBGM();
		super.onResume();
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
	
	// BEWARE: THIS IS WHAT YOU PAY ME FOR
	private void parseXml()
	{
		// Load XML for parsing.
        AssetManager assetManager = getAssets(); // Reference to our Assets Folder
        InputStream inputStream = null; // Stream where we want the data to go through
        try {
            inputStream = assetManager.open("answers.xml"); // From the assets folder we open the stream of data from the xml file
            // NOTE: answer.xml contains all the answers for word related gameplay
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        String xml = sharedData.readTextFile(inputStream); // Using our XML serializer we convert the InputStrem to a string
		
        XmlPullParserFactory factory; // An xml utility class
		try {
			factory = XmlPullParserFactory.newInstance(); // Creating new instance for our utility
			//factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser(); // The Parser Class from our utility instance

	        xpp.setInput(new StringReader (xml)); // then new we read the xml file
	        int eventType = xpp.getEventType();
	     
	        AnswerObject ans = new AnswerObject(); // Creating an instance of a Model Class to hold an answer
	        
	        // Here is how we read the XML TAGS (WE LOOP IT!!!)
	        while (eventType != XmlPullParser.END_DOCUMENT) {
	         if(eventType == XmlPullParser.START_DOCUMENT) {
	             System.out.println("Start document");
	            
	             
	         } else if(eventType == XmlPullParser.END_DOCUMENT) {
	             System.out.println("End document");
	             
	         } else if(eventType == XmlPullParser.START_TAG) {
	             System.out.println("Start tag "+xpp.getName());
	             ans = null; // clearing the model class just to make sure no data is left behind the previous iteration
	             ans = new AnswerObject(); // then re-instanciated to allocate memory
	             
	             // PAY CLOSE ATTENTION AS WE NOW TAKE VALUES FROM THE XML TAGS
	             String cat = xpp.getAttributeValue(null, "category");
	             String lvl = xpp.getAttributeValue(null, "level");
	             String stg = xpp.getAttributeValue(null, "stage");
	             String ans1 = xpp.getAttributeValue(null, "text1");
	             String ans2 = xpp.getAttributeValue(null, "text2");
	             String ans3 = xpp.getAttributeValue(null, "text3");
	             String ans4 = xpp.getAttributeValue(null, "text4");
	             
	             if(cat != null)
	             {
	            	 ans.setCategoryNumber(Integer.valueOf(cat)); // on the AnswerObject which is a model class
	            	 // we set their category (FUCK! READ THE AnswerObject.java)
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
	             
	             // Then we set the answers here
	             ans.setAnswer1(ans1);
	             ans.setAnswer2(ans2);
	             ans.setAnswer3(ans3);
	             ans.setAnswer4(ans4);
	             
	             // Then we store them to our GameManager or GAME LOGIC
	             sharedData.addAnswer(ans);
	             
	             ans = null; // then deallocate the answer instance from the memory
	             
	         } else if(eventType == XmlPullParser.END_TAG) {
	             System.out.println("End tag "+xpp.getName());
	             
	             
	             
	         } else if(eventType == XmlPullParser.TEXT) {
	             System.out.println("Text "+xpp.getText());
	         }
	         eventType = xpp.next();
	        }
		} catch (XmlPullParserException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}


}
