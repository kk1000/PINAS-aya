package com.example.pinas_aya;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class GameManager{

	private static GameManager mInstance;
 
	private Context context;
	private SharedPreferences prefs; // please don't f*ck with this
	
	
	private int currentCategory;
	private int currentLevel;
	private int currentStage;
	
	private int totalCategories;
	private int totalLevelsPerCategory;
	private int totalStagesPerLevel;
	
	private Map<String, Boolean> levels;
	
	private ArrayList<AnswerObject> answers;
	
	private GameManager(){
       // Initialize all global game data here
    	
		levels = new HashMap<String, Boolean>();
    	
		setAnswers(new ArrayList<AnswerObject>());
		
		totalCategories = 5;
		totalLevelsPerCategory = 3;
		totalStagesPerLevel = 15;
    	
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

	public boolean isLevelLocked(int catNum, int lvlNum)
    {
    	int numberOfCompletedStageForLevel = 0;
		
		
		prefs = this.context.getSharedPreferences("com.example.pinas-aya", Context.MODE_PRIVATE);
		
		for(int i = 1; i <= this.totalStagesPerLevel; i++)
		{
			if(prefs.getBoolean(""+catNum+"-"+lvlNum+"-"+i, false))
			{
				numberOfCompletedStageForLevel++;
			}
			
		}
    	
		if(numberOfCompletedStageForLevel < this.totalStagesPerLevel)
		{
			return true;
		}
		
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
    
}
