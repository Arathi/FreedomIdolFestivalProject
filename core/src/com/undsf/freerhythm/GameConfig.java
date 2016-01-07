package com.undsf.freerhythm;

public class GameConfig {
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_NORMAL = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	public static final String DIFFICULTY_EASY_STR = "ez";
	public static final String DIFFICULTY_NORMAL_STR = "nm";
	public static final String DIFFICULTY_HARD_STR = "hd";
	
	public static final String[] DIFFICULTY_STR = {
		DIFFICULTY_EASY_STR, 
		DIFFICULTY_NORMAL_STR, 
		DIFFICULTY_HARD_STR
	};
	
	//比赛信息
	protected int key; //键数
	protected int speed; //速度
	protected int songId; //歌曲编号
	protected int difficulty; //难度
	
	//游戏配置项
	double curve;
	String basePath;
	
	public GameConfig(){
		key = 4;
		speed = 4;
		songId = 1;
		difficulty = DIFFICULTY_EASY;
		
		curve = 1.0;
		basePath = "C:/Download/RM/res/";
	}
	
	public int getKey(){
		return key;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public int getSongId(){
		return songId;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
	
	public double getCurve(){
		return curve;
	}
	
	public String getBasePath(){
		return basePath;
	}
	
	public String getImdSuffix(){
		return "_"+key+"k_"+DIFFICULTY_STR[difficulty]+".imd";
	}
}
