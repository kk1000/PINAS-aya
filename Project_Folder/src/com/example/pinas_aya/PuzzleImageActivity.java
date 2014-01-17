package com.example.pinas_aya;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout;
import android.widget.TableRow;

//@SuppressLint("NewApi")
public class PuzzleImageActivity extends Activity {

	ImageView[] img_piece;
	Integer currentSelectedIndex;
	
	Bitmap[] finalBMaps1 = null;
	private int[] answer;
	
	private GameManager sharedData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle_image);
		
		sharedData = GameManager.getInstance();
		
		TableLayout tl = (TableLayout)findViewById(R.id.tbl_imgArea);
		TableRow row = new TableRow(this);
		String imgString = "img_"+sharedData.getCategory()+"_"+sharedData.getLevel()+"_"+sharedData.getStage();
		//String imgString = "ic_launcher";
		
		int resourceId = getResources().getIdentifier (imgString, "drawable", getPackageName().toString());
		//int img_resourceId = getResources().getIdentifier (imgString, "layout", getPackageName().toString());
		
		Bitmap bMap = BitmapFactory.decodeResource(getResources(),resourceId);
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
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
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
		     //matrix.postRotate(x);
		     // this will create image with new size
		     strechedBMap = Bitmap.createBitmap(croppedBMap, 0, 0,width, height, matrix, true);
		     // Image display factor
		     img_piece[i].setScaleType(ScaleType.FIT_XY);
			
		     // temp storage for later use
		     finalBMaps[i] = strechedBMap;
		     
		     img_piece[i].setTag(i);
		     img_piece[i].setDrawingCacheEnabled(false);
		     
		     //img_piece[i].setImageResource(resourceId); //test
		     final int ii = i;
		     
		     img_piece[i].setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) 
					{
						int currentIndex = ii;
						Log.v("CURRENT INDEX: ", ""+currentIndex);
						Log.v("LAST SELECTED INDEX: ", ""+currentSelectedIndex);
						v.refreshDrawableState();
						
						if(currentSelectedIndex != null)
						{
							// This will switch the imgaes
							img_piece[currentIndex].setImageBitmap(finalBMaps1[currentSelectedIndex]);
							img_piece[currentSelectedIndex].setImageBitmap(finalBMaps1[currentIndex]);
							
							Bitmap img = finalBMaps1[currentIndex];
							finalBMaps1[currentIndex] = finalBMaps1[currentSelectedIndex];
							finalBMaps1[currentSelectedIndex] = img;

							int temp = answer[currentIndex];
							answer[currentIndex] = answer[currentSelectedIndex];
							answer[currentSelectedIndex] = temp;
							if(checkAnswer()){
								Log.v("ANSWER:","TAPOS NA");
							}
							currentSelectedIndex = null;
						}
						else
						{
							currentSelectedIndex =  ii;
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
		finalBMaps1 = new Bitmap[img_piece.length];
		answer = new int[img_piece.length];
		for(int i = 0; i < img_piece.length; i++)
		{
			//int next = r.nextInt(finalBMaps.length-1);
			// to get rid of duplicates
			Bitmap img = finalBMaps[listOfRand.get(i)];
			img_piece[i].setImageBitmap(img);
			
			finalBMaps1[i] = img;
			answer[i] = listOfRand.get(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle_image, menu);
		return true;
	}
	
	
	private boolean checkAnswer(){
		try{
			for(int i = 0; i < answer.length; i ++){
				Log.v("ANSWER:",answer[i] + " " +answer[i+1]);
				if(answer[i]+1!=answer[i+1]) return false;
			}
		}catch(Exception e){
			Log.v("ANSWER:","TAPOS NA");
		}
		return true;
	}

}
