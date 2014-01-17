package com.example.pinas_aya;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		final Button btnPlay = (Button)findViewById(R.id.btn_play);
		
        btnPlay.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				goToCategories();
			}});
		
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
	
	void goToCategories()
	{
		Intent categoriesMenu = new Intent(this, CategoryMenuActivity.class);
		startActivity(categoriesMenu);
	}

}
