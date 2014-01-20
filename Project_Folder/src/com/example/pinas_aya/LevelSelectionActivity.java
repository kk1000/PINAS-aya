package com.example.pinas_aya;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LevelSelectionActivity extends Activity {

	// Global Access
	TextView txt_lvlSelectTitle;
	// Level Buttons
	Button btn_lvl1, btn_lvl2, btn_lvl3;
	// Button State (if level is locked or unlocked)
	TextView btn_lvlState1, btn_lvlState2, btn_lvlState3;
	// Button Status (Percentage Done in a level)
	TextView btn_lvlStatus1, btn_lvlStatus2, btn_lvlStatus3;
	
	ArrayList<Button> btn_lvls;
	ArrayList<TextView> btn_lvlStates, btn_lvlStatuses;
	
	int numberOfCompletedStageForLevel1;
	int numberOfCompletedStageForLevel2;
	int numberOfCompletedStageForLevel3;
	
	
	private GameManager sharedData; // IMPORTANT
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_selection);
		
		sharedData = GameManager.getInstance();
		
		sharedData.initializeAudio(this);
		
		
		// The Category Name
		txt_lvlSelectTitle = (TextView)findViewById(R.id.txt_lvlSelectTitle);
		
		txt_lvlSelectTitle.setText(sharedData.getCategoryName());
		
		// Level Buttons
		btn_lvls = new ArrayList<Button>();
		// find XML ids
		btn_lvl1 = (Button)findViewById(R.id.btn_lvl1);
		btn_lvl2 = (Button)findViewById(R.id.btn_lvl2);
		btn_lvl3 = (Button)findViewById(R.id.btn_lvl3);
		// add to array
		btn_lvls.add(btn_lvl1);
		btn_lvls.add(btn_lvl2);
		btn_lvls.add(btn_lvl3);
		
		// Button State (if level is locked or unlocked)
		btn_lvlStates = new ArrayList<TextView>();
		// find XML ids
		btn_lvlState1 = (TextView)findViewById(R.id.btn_lvlState1);
		btn_lvlState2 = (TextView)findViewById(R.id.btn_lvlState2);
		btn_lvlState3 = (TextView)findViewById(R.id.btn_lvlState3);
		// add to array
		btn_lvlStates.add(btn_lvlState1);
		btn_lvlStates.add(btn_lvlState2);
		btn_lvlStates.add(btn_lvlState3);
		
		// Button Status (Percentage Done in a level)
		btn_lvlStatuses = new ArrayList<TextView>();
		// find XML ids
		btn_lvlStatus1 = (TextView)findViewById(R.id.btn_lvlStatus1);
		btn_lvlStatus2 = (TextView)findViewById(R.id.btn_lvlStatus2);
		btn_lvlStatus3 = (TextView)findViewById(R.id.btn_lvlStatus3);
		// add to array
		btn_lvlStatuses.add(btn_lvlStatus1);
		btn_lvlStatuses.add(btn_lvlStatus2);
		btn_lvlStatuses.add(btn_lvlStatus3);
		
		for(final Button btn : btn_lvls)
		{
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
					
					
					if(btn == btn_lvl1)
					{
						sharedData.playTick();
						sharedData.setLevel(1);
						goToLevelStatus();
					}
					
					if(btn == btn_lvl2)
					{
						
						if(sharedData.isLevelLocked(sharedData.getCategory(), 2))
						{
							// shows a toast alert
							Toast.makeText(getApplicationContext(), "Level is still Locked",
									   Toast.LENGTH_SHORT).show();
							sharedData.playWoosh();
						}
						else
						{
							sharedData.playTick();
							sharedData.setLevel(2);
							goToLevelStatus();
						}
					}
					
					if(btn == btn_lvl3)
					{
						if(sharedData.isLevelLocked(sharedData.getCategory(), 3))
						{
							// shows a toast alert
							Toast.makeText(getApplicationContext(), "Level is still Locked",
									   Toast.LENGTH_SHORT).show();
							sharedData.playWoosh();
						}
						else
						{
							sharedData.playTick();
							sharedData.setLevel(3);
							goToLevelStatus();
						}
					}
					
				}});
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_selection, menu);
		return true;
	}
	@Override
	protected void onPause() {
		sharedData.playMainBGM();
		super.onPause();
	}
	
	
	@Override
	protected void onResume() {
		sharedData.playMainBGM();
		setupProgress();
		super.onResume();
	}
	
	void goToLevelStatus()
	{
		Intent lvlStatusScreen = new Intent(this, LevelStatusActivity.class);
		startActivity(lvlStatusScreen);
	}
	
	void setupProgress()
	{
		numberOfCompletedStageForLevel1 = 0;
		numberOfCompletedStageForLevel2 = 0;
		numberOfCompletedStageForLevel3 = 0;
		
		SharedPreferences prefs = this.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
		
		for(int i = 1; i <= sharedData.getTotalNumberOfStagePerLevel(); i++)
		{
			if(prefs.getBoolean(""+sharedData.getCategory()+"-"+1+"-"+i, false))
			{
				numberOfCompletedStageForLevel1++;
			}
			
			if(prefs.getBoolean(""+sharedData.getCategory()+"-"+2+"-"+i, false))
			{
				numberOfCompletedStageForLevel2++;
			}
			
			if(prefs.getBoolean(""+sharedData.getCategory()+"-"+3+"-"+i, false))
			{
				numberOfCompletedStageForLevel3++;
			}
			
		}
		
		btn_lvlStatus1.setText(""+numberOfCompletedStageForLevel1+"/"+sharedData.getTotalNumberOfStagePerLevel());
		btn_lvlStatus2.setText(""+numberOfCompletedStageForLevel2+"/"+sharedData.getTotalNumberOfStagePerLevel());
		btn_lvlStatus3.setText(""+numberOfCompletedStageForLevel3+"/"+sharedData.getTotalNumberOfStagePerLevel());
		
		
		if(sharedData.isLevelLocked(sharedData.getCategory(), 2))
		{
			btn_lvlState2.setText("Locked");
		}
		else
		{
			btn_lvlState2.setText("Tap to choose");
		}
		
		if(sharedData.isLevelLocked(sharedData.getCategory(), 3))
		{
			btn_lvlState3.setText("Locked");
		}
		else
		{
			btn_lvlState3.setText("Tap to choose");
		}
		
		
		
		
	}

}
