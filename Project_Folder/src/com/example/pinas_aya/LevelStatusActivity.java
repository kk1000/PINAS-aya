package com.example.pinas_aya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LevelStatusActivity extends Activity {

	TextView txt_lvlStatusTitle;
	TextView txt_lvlStatusLevelNumber;
	ImageView[] img_lvlStages = new ImageView[15];
	
	
	private GameManager sharedData; // IMPORTANT
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_status);
		
		sharedData = GameManager.getInstance();
		
		sharedData.initializeAudio(this);
		
		txt_lvlStatusTitle = (TextView)findViewById(R.id.txt_lvlStatusTitle);
		txt_lvlStatusTitle.setText(sharedData.getCategoryName());
		
		txt_lvlStatusLevelNumber = (TextView)findViewById(R.id.txt_lvlStatusLevelNumber);
		txt_lvlStatusLevelNumber.setText("Level "+ sharedData.getLevel());
		
		img_lvlStages[0] = (ImageView)findViewById(R.id.img_lvlStage1);
		img_lvlStages[1] = (ImageView)findViewById(R.id.img_lvlStage2);
		img_lvlStages[2] = (ImageView)findViewById(R.id.img_lvlStage3);
		img_lvlStages[3] = (ImageView)findViewById(R.id.img_lvlStage4);
		img_lvlStages[4] = (ImageView)findViewById(R.id.img_lvlStage5);
		img_lvlStages[5] = (ImageView)findViewById(R.id.img_lvlStage6);
		img_lvlStages[6] = (ImageView)findViewById(R.id.img_lvlStage7);
		img_lvlStages[7] = (ImageView)findViewById(R.id.img_lvlStage8);
		img_lvlStages[8] = (ImageView)findViewById(R.id.img_lvlStage9);
		img_lvlStages[9] = (ImageView)findViewById(R.id.img_lvlStage10);
		img_lvlStages[10] = (ImageView)findViewById(R.id.img_lvlStage11);
		img_lvlStages[11] = (ImageView)findViewById(R.id.img_lvlStage12);
		img_lvlStages[12] = (ImageView)findViewById(R.id.img_lvlStage13);
		img_lvlStages[13] = (ImageView)findViewById(R.id.img_lvlStage14);
		img_lvlStages[14] = (ImageView)findViewById(R.id.img_lvlStage15);
		
		for(final ImageView img : img_lvlStages)
		{
			img.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
					
					
					// There's no other way though
					if(img == img_lvlStages[0])
					{
						sharedData.playTick();
						setStage(1);
					}
					else if(img == img_lvlStages[1] /*&& !isStageLocked(2) */)
					{
						setStage(2);
					}
					else if(img == img_lvlStages[2] /* && !isStageLocked(3) */)
					{
						setStage(3);
					}
					else if(img == img_lvlStages[3] /* && !isStageLocked(4) */)
					{
						setStage(4);
					}
					else if(img == img_lvlStages[4] /* && !isStageLocked(5) */)
					{
						setStage(5);
					}
					else if(img == img_lvlStages[5] /* && !isStageLocked(6) */)
					{
						setStage(6);
					}
					else if(img == img_lvlStages[6] /* && !isStageLocked(7) */)
					{
						setStage(7);
					}
					else if(img == img_lvlStages[7] /* && !isStageLocked(8) */ )
					{
						setStage(8);
					}
					else if(img == img_lvlStages[8] /* && !isStageLocked(9) */ )
					{
						setStage(9);
					}
					else if(img == img_lvlStages[9] /* && !isStageLocked(10) */)
					{
						setStage(10);
					}
					else if(img == img_lvlStages[10] /* && !isStageLocked(11) */)
					{
						setStage(11);
					}
					else if(img == img_lvlStages[11] /* && !isStageLocked(12) */)
					{
						setStage(12);
					}
					else if(img == img_lvlStages[12] /* && !isStageLocked(13) */)
					{
						setStage(13);
					}
					else if(img == img_lvlStages[13] /* && !isStageLocked(14) */)
					{
						setStage(14);
					}
					else if(img == img_lvlStages[14] /* && !isStageLocked(15) */)
					{
						setStage(15);
					}
					
				}});
			
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_status, menu);
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
		setTileImages();
		super.onResume();
	}
	
	void goToLevelImage()
	{
		Intent lvlImageScreen = null;
		// determine if word or puzzle mechanic here
		if(sharedData.getCategory() == 4 || sharedData.getCategory() == 5)
		{
			lvlImageScreen = new Intent(this, PuzzleImageActivity.class);
		}
		else
		{
			lvlImageScreen = new Intent(this, LevelImageActivity.class);
		}
		
		startActivity(lvlImageScreen);
	}
	
	private void setStage(int stage)
	{
		sharedData.setStage(stage);
		goToLevelImage();
	}
	
	private boolean isStageLocked(final int stage)
	{
		if(sharedData.isStageLocked(sharedData.getCategory(), sharedData.getLevel(), stage))
		{
			// shows a toast alert
			Toast.makeText(getApplicationContext(), "Stage is still Locked", 
					Toast.LENGTH_SHORT).show();
			sharedData.playWoosh();			
			return true;
		}
		
		sharedData.playTick();	
		return false;
	}

	private void setTileImages()
	{
		String imgString  = "img_";
		int resourceId;
		for(int i = 1; i <= 15; i++)
		{
			imgString += sharedData.getCategory()+"_"+sharedData.getLevel()+"_"+i;
			
			resourceId = getResources().getIdentifier (imgString, "drawable", getPackageName().toString());
			img_lvlStages[i-1].setImageResource(resourceId);
			
			/*
			if(!sharedData.isStageLocked(sharedData.getCategory(), sharedData.getLevel(), i) || i == 1)
			{
				imgString += sharedData.getCategory()+"_"+sharedData.getLevel()+"_"+i;
				
				resourceId = getResources().getIdentifier (imgString, "drawable", getPackageName().toString());
				img_lvlStages[i-1].setImageResource(resourceId);
			}
			else
			{
				img_lvlStages[i-1].setImageResource(R.drawable.ic_qmark_clear);
			}
			
			*/
			
			imgString = "img_";
			
		}
	}
	
}
