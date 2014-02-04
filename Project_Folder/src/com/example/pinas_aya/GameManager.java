package com.example.pinas_aya;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

public class GameManager{

	private static GameManager mInstance;
 
	private Context context;
	private Activity activity;
	private SharedPreferences prefs; // please don't f*ck with this
	
	// for the SFX
	private SoundPool soundPool;
	private int tadaSoundID, tickSoundID, wooshSoundID, kidSoundID;
	private boolean loaded = false;
	private AudioManager audioManager;
	private float volume;
	
	// for the BGM
	private MediaPlayer mediaPlayer;
	
	private int currentCategory;
	private int currentLevel;
	private int currentStage;
	
	private final int totalCategories = 5;
	private final int totalLevelsPerCategory = 3;
	private final int totalStagesPerLevel = 15;
	
	private Map<String, Boolean> levels;
	
	private ArrayList<AnswerObject> answers;
	
	private GameManager(){
       // Initialize all global game data here
    	
		levels = new HashMap<String, Boolean>();
    	
		setAnswers(new ArrayList<AnswerObject>());
    }
 
    public static GameManager getInstance(){
        
    	if(mInstance == null)
        {
            mInstance = new GameManager();
        }
    	
        return mInstance;
    }
	
    public void setContext(Context con)
    {
    	this.context = con;
    	// Once the context is set, we then init everything
    	initializeManager();
    	//initializeAudio();
    }
    
    public void initializeAudio(Activity act)
    {
    	this.activity = act;
    	// Set the hardware buttons to control the music
    	activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
          @Override
          public void onLoadComplete(SoundPool soundPool, int sampleId,
              int status) {
            loaded = true;
          }
        });
        
        
        // Getting the user sound settings
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager
            .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
            .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;
        
        tadaSoundID = soundPool.load(this.context, R.raw.tada, 1);
        tickSoundID = soundPool.load(this.context, R.raw.tick, 1);
        wooshSoundID = soundPool.load(this.context, R.raw.woosh, 1);
        kidSoundID = soundPool.load(this.context, R.raw.kid_laugh, 1);
        
        if(mediaPlayer == null)
        {
        	mediaPlayer = MediaPlayer.create(activity, R.raw.gonna_start);
        }
        
        if(isFirstTimeLaunch())
        {
        	setBGMEnabled(true);
    		setSFXEnabled(true);
        }
        
        
        
    }
    
    private void initializeManager()
    {
    	prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
    	
    	if(prefs.getBoolean("FirstTime", true))
    	{
    		prefs.edit().putBoolean("FirstTime", false).commit();
    		
    		// CategoryNumber-LevelNumber-StageNumber format
        	for(int c = 1; c <= totalCategories; c++)
        	{
        		for(int l = 1; l <= totalLevelsPerCategory; l++)
        		{
        			for(int s = 1; s <= totalStagesPerLevel; s++)
        			{
        				levels.put(""+c+"-"+l+"-"+s, false); // All is false at init
        				prefs.edit().putBoolean(""+c+"-"+l+"-"+s, false).commit();
        			}
        		}
        	}
        	
        	//prefs.edit().commit();
    	}
    	else
    	{
    		// CategoryNumber-LevelNumber-StageNumber format
        	for(int c = 1; c <= totalCategories; c++)
        	{
        		for(int l = 1; l <= totalLevelsPerCategory; l++)
        		{
        			for(int s = 1; s <= totalStagesPerLevel; s++)
        			{
        				// here we copy them from prefs to our runtime dictionary
        				levels.put(""+c+"-"+l+"-"+s, prefs.getBoolean(""+c+"-"+l+"-"+s, false));
        			}
        		}
        	}
        	
        	//System.out.println("ALL LEVELs " + levels.toString());
    	}
    	
    	
    	
    }
    
    public boolean isFirstTimeLaunch()
    {
    	prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
    	if(prefs.getBoolean("FirstTime", true)) return true;
    	return false;
    }
    
    public Map<String, Boolean> getAllLevels()
    {
		return levels;
    }
    
    public void setStageCompletion(String cls, boolean completed)
    {
    	this.levels.put(cls, completed);
    }
    
    public void completeCurrentStage()
    {
    	this.levels.put("" + currentCategory + "-" + currentLevel + "-" + currentStage, true);
    	
    	prefs.edit().putBoolean(""+currentCategory+"-"+currentLevel+"-"+currentStage, 
    			levels.get(""+currentCategory+"-"+currentLevel+"-"+currentStage)).commit();
    	
    }
    
    public int getTotalNumberOfCategories()
    {
		return totalCategories;
    }
    
    public int getTotalNumberOfLevelPerCategory()
    {
		return totalLevelsPerCategory;
    	
    }
    
    public int getTotalNumberOfStagePerLevel()
    {
		return totalStagesPerLevel;
    	
    }
    
    public void setCategory(int category)
    {
    	this.currentCategory = category;
    	System.out.println("Category set to " + category);
    }
    
    public int getCategory()
    {
		return currentCategory;
    	
    }
    
    public String getCategoryName()
    {
    	switch(this.currentCategory)
    	{
    	case 1: return "Famous People";
    	case 2: return "Animals";
    	case 3: return "Native Stuff";
    	case 4: return "Tourist Spots";
    	case 5: return "Historical Events";
    	default: System.out.println("Unknow Category"); return "";
    	}
    }
    
    public void setLevel(int level)
    {
    	this.currentLevel = level;
    	System.out.println("Level set to " + level);
    }
    
    public int getLevel()
    {
		return currentLevel;
    	
    }
    
    public void setStage(int stage)
    {
    	this.currentStage = stage;
    	System.out.println("Stage set to " + stage);
    }
    
    public int getStage()
    {
		return currentStage;
    	
    }
    
    public ArrayList<AnswerObject> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<AnswerObject> answers) {
		if(this.answers == null)
		{
			this.answers = new ArrayList<AnswerObject>();
		}
		this.answers = answers;
	}
	
	public void addAnswer(AnswerObject ans)
	{
		if(this.answers == null)
		{
			this.answers = new ArrayList<AnswerObject>();
		}
		
		this.answers.add(ans);
	}

	public boolean isSFXEnabled() 
	{
		prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
		
		return prefs.getBoolean("sfx", false);
	}

	public void setSFXEnabled(boolean isSFXEnabled) {
		prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
		prefs.edit().putBoolean("sfx", isSFXEnabled).commit();
	}

	public boolean isBGMEnabled() {
		prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
		return prefs.getBoolean("bgm", false);
	}

	public void setBGMEnabled(boolean isBGMEnabled) {
		prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
		prefs.edit().putBoolean("bgm", isBGMEnabled).commit();
	}

	public boolean isLevelLocked(int catNum, int lvlNum)
    {
		int numberOfCompletedStageForLevel = 0;
		
		prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
		for(int i = 1; i <= this.totalStagesPerLevel; i++)
		{
			if(prefs.getBoolean(""+catNum+"-"+(lvlNum-1)+"-"+i, false))
			{
				numberOfCompletedStageForLevel++;
			}
		}
    	
		if(numberOfCompletedStageForLevel < this.totalStagesPerLevel) return true;
		
		return false;
    	
    }
    
    public boolean isStageLocked(int catNum, int lvlNum, int stageNum)
    {
    	prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
    	
    	// -1 because we should check if the previous stage is already complete
    	if(!prefs.getBoolean(""+catNum+"-"+lvlNum+"-"+(stageNum-1), false))
		{
			return true;
		}
    	
		return false;
    	
    }
    
    public boolean isCurrentStageLocked()
    {
    	prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
    	
    	if(!prefs.getBoolean(""+this.currentCategory+"-"+this.currentLevel+"-"+(this.currentStage-1), false))
    	{
    		return true;
    	}
		return false;
    }
    
    
    public void playTick()
    {
        // Is the sound loaded already?
    	if (loaded && isSFXEnabled()) {
          soundPool.play(tickSoundID, volume, volume, 1, 0, 1f);
          Log.e("Test", "Played sound");
        }
    }
    
    public void playTada()
    {
    	// Is the sound loaded already?
    	if (loaded && isSFXEnabled()) {
          soundPool.play(tadaSoundID, volume, volume, 1, 0, 1f);
          //Log.e("Test", "Played sound");
        }
    }
    
    public void playKidLaugh()
    {
    	// Is the sound loaded already?
    	if (loaded && isSFXEnabled()) {
          soundPool.play(kidSoundID, volume, volume, 1, 0, 1f);
          //Log.e("Test", "Played sound");
        }
    }
    
    public void playWoosh()
    {
    	// Is the sound loaded already?
        if (loaded && isSFXEnabled()) {
          soundPool.play(wooshSoundID, volume, volume, 1, 0, 1f);
          //Log.e("Test", "Played sound");
        }
    }
    
    public void playMainBGM()
    {
    	//int currentSongTime = mediaPlayer.getCurrentPosition();
    	
    	if(mediaPlayer.isPlaying())
    	{
    		mediaPlayer.pause();
    	}
    	else
    	{
    		if(isBGMEnabled())
    		{
    			mediaPlayer.start();
    			mediaPlayer.setLooping(true);
    		}
    		
    	}
    	
    	/*
    	new Thread(new Runnable() {
            public void run() {
            	
            }
        }).start();
    	*/
    	
    }
    
    public String readTextFile(InputStream inputStream) {
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
