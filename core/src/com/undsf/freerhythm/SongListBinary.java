package com.undsf.freerhythm;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class SongListBinary {
	//public String path;
	protected File file;
	protected TResHeadAll header;
	protected ArrayList<SongConfigClient> songs;
	protected Map<Integer, SongConfigClient> songIndex;
	
	public SongListBinary(String path){
		file = new File(path);
	}
	
	public SongListBinary(File file){
		this.file = file;
	}

	public void read() {
		if (file==null){
			System.err.println("数据文件未设置！");
			return;
		}
		if (file.exists() == false) {
			System.err.println("数据文件" + file.getAbsolutePath() + "不存在！");
			return;
		}
		try {
			FileInputStream in = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(in);
			byte[] headerData = new byte[TResHeadAll.DATA_LENGTH];
			int readCounter = dis.read(headerData, 0, TResHeadAll.DATA_LENGTH);
			header = new TResHeadAll(headerData);
			System.out.println(header);
			songs = new ArrayList<SongConfigClient>();
			for (int index=0; index<header.getCount(); index++){
				byte[] songData = new byte[header.getUnit()];
				readCounter = dis.read(songData, 0, header.getUnit());
				SongConfigClient song = new SongConfigClient(songData);
				//System.out.println(song);
				songs.add(song);
			}
			dis.close();
			in.close();
			createIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createIndex(){
		songIndex = new HashMap<Integer, SongConfigClient>();
		for (SongConfigClient song : songs){
			int songId = song.m_ushSongID;
			songIndex.put(songId, song);
		}
		System.out.println("歌曲索引已创建！");
	}

	public String getSongAsciiNameById(int songId) {
		if (songIndex.containsKey(songId)==false){
			System.err.println("歌曲编号No."+songId+"不存在！");
			return null;
		}
		SongConfigClient song = songIndex.get(songId);
		if (song == null){
			System.err.println("No."+songId+"对应的歌曲数据不存在！");
			return null;
		}
		return song.m_szPath;
	}
	
}
