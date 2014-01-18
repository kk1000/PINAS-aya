package com.example.pinas_aya;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class MainMenuActivity extends Activity {

	private GameManager sharedData;
	
	private CheckBox cbox_sfx;
	private CheckBox cbox_bgm;
	
	private Button btnPlay, btn_help, btn_about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		sharedData = GameManager.getInstance();
		
		sharedData.initializeAudio(this);
		
		
		cbox_sfx = (CheckBox)findViewById(R.id.cbox_sfx);
		cbox_bgm = (CheckBox)findViewById(R.id.cbox_bgm);
		btnPlay = (Button)findViewById(R.id.btn_play);
		btn_help = (Button)findViewById(R.id.btn_help);
		btn_about = (Button)findViewById(R.id.btn_about);
		
		cbox_sfx.setChecked(sharedData.isSFXEnabled());
		cbox_bgm.setChecked(sharedData.isBGMEnabled());
		
		cbox_sfx.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				sharedData.setSFXEnabled(cbox_sfx.isChecked());
			}});
		
		cbox_bgm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sharedData.setBGMEnabled(cbox_bgm.isChecked());
				sharedData.playMainBGM();
			}
		});
		
        btnPlay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				sharedData.playTick();
				
				
				
				
				goToCategories();
			}});
        
        btn_help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showDialogWithMessage("Help Details here");
			}
		});
        
        btn_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialogWithMessage("Please pay the other half to finalize");
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		
		
        
        
		return true;
	}
	
	@Override
	public void onBackPressed() {
	    // do nothing.
		// This will disable the back button on the device so the user cannot return to the Start Page
	}
	
	@Override
	protected void onPause() {
		sharedData.playMainBGM();
		super.onPause();
	}
	
	
	@Override
	protected void onResume() {
		sharedData.playMainBGM();
		super.onResume();
	}
	
	private void showDialogWithMessage(String message)
	{
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(message)
		       .setTitle(getResources().getString(R.string.app_name)).setNeutralButton("Dismiss", new DialogInterface.OnClickListener()
		       {
		    	   @Override
		    	   public void onClick(DialogInterface dialog, int which) { dialog.dismiss();}
		       });
		
		
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	void goToCategories()
	{
		Intent categoriesMenu = new Intent(this, CategoryMenuActivity.class);
		startActivity(categoriesMenu);
	}

}
