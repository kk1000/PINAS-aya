package com.example.pinas_aya;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//@SuppressLint("NewApi")
public class PuzzleImageActivity extends Activity {

	private String imgString;
	private int stageImgResourceId;
	private ImageView[] img_piece;
	private Integer currentSelectedIndex;
	
	private Bitmap[] finalJumbledBMaps = null;
	private int[] answer;
	
	private TextView lbl_lvlImageCategory;
	private ImageButton btn_imgHint;
	private Button btn_viewDetails;
	// Generally for screen
	private Display display;
	private Point size;
	private int screenWidth;
	
	
	private GameManager sharedData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_image);
		
		sharedData = GameManager.getInstance();
		// get the screen size
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		
		imgString = "img_"+sharedData.getCategory()+"_"+sharedData.getLevel()+"_"+sharedData.getStage();
		//String imgString = "ic_launcher";
		// for later use
		stageImgResourceId = getResources().getIdentifier (imgString, "drawable", getPackageName().toString());
		//int img_resourceId = getResources().getIdentifier (imgString, "layout", getPackageName().toString());
		
		// Early instructions
		showDialogWithMessage("How to play?\nTap a piece once\nTap another to switch there posistions\nFix the image");
		
		
		lbl_lvlImageCategory= (TextView)findViewById(R.id.lbl_lvlImageCategory);
		lbl_lvlImageCategory.setText(sharedData.getCategoryName());
		
		
		btn_viewDetails = (Button)findViewById(R.id.btn_viewDetails);
		btn_viewDetails.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				showDialogWithMessage("Details here");
				
			}});
		
		
		setupImageHint();
		
		generateStageImage();
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_image, menu);
		return true;
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
	
	private void setupImageHint()
	{
		btn_imgHint = (ImageButton)findViewById(R.id.btn_imgHint);
		btn_imgHint.setImageResource(stageImgResourceId);
		btn_imgHint.setScaleType(ScaleType.FIT_XY);
		btn_imgHint.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(PuzzleImageActivity.this);
				
				final ImageView imgView = new ImageView(PuzzleImageActivity.this);
				
				Bitmap bMap = BitmapFactory.decodeResource(getResources(),stageImgResourceId);
				
				int width = bMap.getWidth();
			    int height = bMap.getHeight();
			    int newWidth = (int) (screenWidth- 15);
				int newHeight = newWidth;
			    
				float scaleWidth = ((float) newWidth) / width;
			    float scaleHeight = ((float) newHeight) / height;

			     Matrix matrix = new Matrix();

			     matrix.postScale(scaleWidth, scaleHeight);
			     
			     // this will create image with new size
			     Bitmap strechedBMap = Bitmap.createBitmap(bMap, 0, 0,width, height, matrix, true);
				
				imgView.setImageBitmap(strechedBMap);
				
				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setView(imgView).setNeutralButton("Dismiss", new DialogInterface.OnClickListener()
			       {
			    	   @Override
			    	   public void onClick(DialogInterface dialog, int which) { dialog.dismiss();}
			       });
							
				
				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
			
				
			}});
	}
	
	private void generateStageImage()
	{
		TableLayout tl = (TableLayout)findViewById(R.id.tbl_imgArea);
		TableRow row = new TableRow(this);
		
		Bitmap bMap = BitmapFactory.decodeResource(getResources(),stageImgResourceId);
		Bitmap croppedBMap, strechedBMap;
		Bitmap[] finalBMaps = null;
		int x = 0;
		int y = 0;
		int newWidth = 0;
	    int newHeight = 0;
	    float scaleWidth;
	    float scaleHeight;
	    Matrix matrix;
	    int originalWidth = bMap.getWidth();
	    int originalHeight = bMap.getHeight();
	    
		switch(sharedData.getLevel())
		{
		case 1:
			img_piece = new ImageView[25];
			break;
		case 2:
			img_piece = new ImageView[36];
			break;
		case 3:
			img_piece = new ImageView[49];
			break;
		default:
			img_piece = new ImageView[25]; // 25 pieces the default
			break;
		}
		
		finalBMaps = new Bitmap[img_piece.length];
		// here we set the size property for each piece
		
		newWidth = (int) (screenWidth / Math.sqrt(img_piece.length) - 15);
		newHeight = newWidth;	
		
		// CHOP THE SOURCE HERE
		for(int i = 0; i < img_piece.length; i++)
		{
			
			img_piece[i] = new ImageView(this.getApplicationContext());
			int reCalcPieceWidth = (int) (originalWidth/Math.sqrt(img_piece.length));
			int reCalcPieceHeight = (int) (originalHeight/Math.sqrt(img_piece.length));
			croppedBMap = Bitmap.createBitmap(bMap,x,y,reCalcPieceWidth,reCalcPieceHeight);
			//Log.v("imageString:",imgString);
			x+=reCalcPieceWidth;
			
			int width = croppedBMap.getWidth();
		    int height = croppedBMap.getHeight();
		    
		    // calculate the scale - in this case = 0.4f

		     scaleWidth = ((float) newWidth) / width;
		     scaleHeight = ((float) newHeight) / height;

		     matrix = new Matrix();

		     matrix.postScale(scaleWidth, scaleHeight);
		     
		     // this will create image with new size
		     strechedBMap = Bitmap.createBitmap(croppedBMap, 0, 0,width, height, matrix, true);
		     // Image display factor
		     img_piece[i].setScaleType(ScaleType.FIT_XY);
			
		     // temp storage for later use
		     finalBMaps[i] = strechedBMap;
		     
		     img_piece[i].setTag(i);
		     img_piece[i].setDrawingCacheEnabled(false);
		     
		     //img_piece[i].setImageResource(resourceId); //test
		     final int loopIndexHolder = i;
		     
		     
		     img_piece[i].setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) 
					{
						int currentIndex = loopIndexHolder;
						//Log.v("CURRENT INDEX: ", ""+currentIndex);
						//Log.v("LAST SELECTED INDEX: ", ""+currentSelectedIndex);
						
						
						
						if(currentSelectedIndex != null)
						{
							
							img_piece[currentSelectedIndex].setAlpha(1.0f);
							//img_piece[currentSelectedIndex].setBackgroundColor(Color.TRANSPARENT);
							// This will switch the imgaes
							img_piece[currentIndex].setImageBitmap(finalJumbledBMaps[currentSelectedIndex]);
							img_piece[currentSelectedIndex].setImageBitmap(finalJumbledBMaps[currentIndex]);
							
							Bitmap img = finalJumbledBMaps[currentIndex];
							finalJumbledBMaps[currentIndex] = finalJumbledBMaps[currentSelectedIndex];
							finalJumbledBMaps[currentSelectedIndex] = img;

							int temp = answer[currentIndex];
							answer[currentIndex] = answer[currentSelectedIndex];
							answer[currentSelectedIndex] = temp;
							
							if(checkAnswer())
							{
								// shows a toast alert
								//Toast.makeText(getApplicationContext(), "Stage complete!",Toast.LENGTH_SHORT).show();
								showDialogWithMessage("Stage Complete!");
								sharedData.completeCurrentStage();
								//Log.v("ANSWER:","TAPOS NA");
							}
							currentSelectedIndex = null;
							
						}
						else
						{
							currentSelectedIndex =  currentIndex;
							img_piece[currentSelectedIndex].setAlpha(0.4f);
							//img_piece[currentSelectedIndex].setBackgroundColor(Color.BLUE);
						}
						
					}
		     });
		     // 
		     img_piece[i].forceLayout();
		     // add to grid
		     row.addView(img_piece[i]);
		     
			if(((i+1)%(Math.sqrt(img_piece.length)))==0) {
				// grid/row add to table
				tl.addView(row);
				row = new TableRow(this);
				// reset x
				x = 0;
				// add y for new cropping area
				y += reCalcPieceHeight;
				
			}
		}
		
		//Random r = new Random();
		// Here we'll create a jumbled set of numbers
		ArrayList<Integer> listOfRand = new ArrayList<Integer>();
		int i_Num = 0;
		while(listOfRand.size() < img_piece.length)
		{
			listOfRand.add(i_Num);
			i_Num++;
		}
		Collections.shuffle(listOfRand);
		
		// Then here we assign them as indices
		finalJumbledBMaps = new Bitmap[img_piece.length];
		answer = new int[img_piece.length];
		for(int i = 0; i < img_piece.length; i++)
		{
			//int next = r.nextInt(finalBMaps.length-1);
			// to get rid of duplicates
			Bitmap img = finalBMaps[listOfRand.get(i)];
			img_piece[i].setImageBitmap(img);
			
			finalJumbledBMaps[i] = img;
			answer[i] = listOfRand.get(i);
		}
	}
	
	private boolean checkAnswer(){
		try{
			for(int i = 0; i < answer.length; i ++){
				//Log.v("ANSWER:",answer[i] + " " +answer[i+1]);

				if((answer[i]+1)!=answer[(i+1)]) return false;
			}
		}catch(Exception e){
			//Log.v("ANSWER:","TAPOS NA");
			// shows a toast alert
			//Toast.makeText(getApplicationContext(), "Stage complete!",Toast.LENGTH_SHORT).show();
		}
		return true;
	}

}
