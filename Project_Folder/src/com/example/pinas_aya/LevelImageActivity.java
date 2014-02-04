package com.example.pinas_aya;

import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class LevelImageActivity extends Activity {

	private TextView lbl_lvlImageCategory;
	private EditText txt_answer;
	
	private ImageView img_lvlImage;
	
	private Button btn_viewDetails;
	private ImageButton btn_submitImage;
	
	private GameManager sharedData;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_image);
		
		sharedData = GameManager.getInstance();
		
		sharedData.initializeAudio(this);
		
		showDialogWithMessage("How to play?\nGuess the word!");
		
		lbl_lvlImageCategory = (TextView)findViewById(R.id.lbl_lvlImageCategory);
		lbl_lvlImageCategory.setText(sharedData.getCategoryName());
		
		// The Image
		img_lvlImage = (ImageView)findViewById(R.id.img_lvlImage);
		
		String imgString  = "img_"+sharedData.getCategory()+"_"+sharedData.getLevel()+"_"+sharedData.getStage();
		
		System.out.println("THE IMAGE: " + imgString);
		
		int resourceId = getResources().getIdentifier (imgString, "drawable", getPackageName().toString());
		img_lvlImage.setImageResource(resourceId);
		
		// The Answer Tada!
		txt_answer = (EditText)findViewById(R.id.txt_answer);
		// The Buttons (Ok OA na)
		btn_viewDetails = (Button)findViewById(R.id.btn_viewDetails);
		btn_submitImage = (ImageButton)findViewById(R.id.btn_submitImage);
		
		btn_viewDetails.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showDialogWithMessage("Details here");
			}
		});
		
		btn_submitImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				String input = txt_answer.getText().toString();
				
				if(input.length() > 0 && validateAnswer(input))
				{
					//System.out.println(""+sharedData.getAnswers());
					sharedData.playTada();
					showDialogWithMessage("Stage Complete!");
					
				}
				else
				{
					sharedData.playKidLaugh();
				}
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_image, menu);
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
		super.onResume();
	}
	
	private void showDialogWithMessage(String message)
	{
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		final String theMessage = message;
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(message)
		       .setTitle(getResources().getString(R.string.app_name)).setNeutralButton("Dismiss", new DialogInterface.OnClickListener()
		       {
		    	   @Override
		    	   public void onClick(DialogInterface dialog, int which) 
		    	   { 
		    		   dialog.dismiss();
		    		   if(theMessage.equals("Stage Complete!"))
		    		   {
		    			   LevelImageActivity.this.onBackPressed();
		    		   }
		    		   
		    	   }
		       });
		
		
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private boolean validateAnswer(String input)
	{
		//AnswerObject asshole = sharedData.getAnswers().
		
		for(AnswerObject ans : sharedData.getAnswers())
		{
			
			if(ans.getCategoryNumber() == sharedData.getCategory() 
					&& ans.getLevelNumber() == sharedData.getLevel() 
					&& ans.getStageNumber() == sharedData.getStage())
			{
				if((Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(ans.getAnswer1()).find()
						|| ((ans.getAnswer2() != null)?Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(ans.getAnswer2()).find(): false)
						|| ((ans.getAnswer3() != null)?Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(ans.getAnswer3()).find(): false)
						|| ((ans.getAnswer4() != null)?Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(ans.getAnswer4()).find(): false)) 
						&& (input.length() == ans.getAnswer1().length()
								|| ((ans.getAnswer2() != null) ? input.length() == ans.getAnswer2().length(): false)
								|| ((ans.getAnswer3() != null) ? input.length() == ans.getAnswer3().length(): false)
								|| ((ans.getAnswer4() != null) ? input.length() == ans.getAnswer4().length(): false)))
				{
					
					sharedData.completeCurrentStage();
					return true;
				}
				else
				{
					// shows a toast alert
					Toast.makeText(getApplicationContext(), "Not even close!",
							   Toast.LENGTH_SHORT).show();
					return false;
					
				}
				
			}
		}
		return false;
	}

}
