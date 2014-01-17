package com.example.pinas_aya;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CategoryMenuActivity extends Activity {
 
	ArrayList<Button> btn_array_list;
	
	Button btn_cat1, btn_cat2, btn_cat3, btn_cat4, btn_cat5;
	
	private GameManager sharedData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_menu);
		
		sharedData = GameManager.getInstance();
		
		
		btn_cat1 = (Button)findViewById(R.id.btn_cat1);
		btn_cat2 = (Button)findViewById(R.id.btn_cat2);
		btn_cat3 = (Button)findViewById(R.id.btn_cat3);
		btn_cat4 = (Button)findViewById(R.id.btn_cat4);
		btn_cat5 = (Button)findViewById(R.id.btn_cat5);
		
		btn_array_list = new ArrayList<Button>();
		btn_array_list.add(btn_cat1);
		btn_array_list.add(btn_cat2);
		btn_array_list.add(btn_cat3);
		btn_array_list.add(btn_cat4);
		btn_array_list.add(btn_cat5);
		
		for(final Button btn : btn_array_list)
		{
			
			btn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0) {
					
					if(btn == btn_cat1)
					{
						sharedData.setCategory(1);
					}
					else if(btn == btn_cat2)
					{
						sharedData.setCategory(2);
					}
					else if(btn == btn_cat3)
					{
						sharedData.setCategory(3);
					}
					else if(btn == btn_cat4)
					{
						sharedData.setCategory(4);
					}
					else if(btn == btn_cat5)
					{
						sharedData.setCategory(5);
					}
					
					goToLevelSelection();
					
				}
				
			});
			
			
		}
		
	}

	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category_menu, menu);
		return true;
	}
	
	void goToLevelSelection()
	{
		Intent lvlSelectMenu = new Intent(this, LevelSelectionActivity.class);
		startActivity(lvlSelectMenu);
	}

}
