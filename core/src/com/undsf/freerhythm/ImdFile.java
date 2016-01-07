package com.undsf.freerhythm;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.undsf.util.Convert;

public class ImdFile {
	protected File file;
	
	int length;
	int beatAmount;
	ArrayList<Beat> beats; //12Bytes/beat
	byte unknownByte1;
	byte unknownByte2;
	int tapAmount;
	ArrayList<TapInfo> taps; //11Bytes/tap
	
	public ImdFile(String path){
		file = new File(path);
	}
	
	public ImdFile(File file){
		this.file = file;
	}
	
	public void read() {
		if (file.exists() == false) {
			System.err.println("数据文件" + file.getAbsolutePath() + "不存在！");
			return;
		}
		try {
			FileInputStream in = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(in);
			int i, readCounter;
			length = Convert.IntToLittleEndian(dis.readInt());
			beatAmount = Convert.IntToLittleEndian(dis.readInt());
			beats = new ArrayList<Beat>();
			for (i=0; i<beatAmount; i++){
				byte[] buffer = new byte[Beat.DATA_LENGTH];
				readCounter = dis.read(buffer, 0, Beat.DATA_LENGTH);
				Beat beat = new Beat(buffer);
				beats.add(beat);
			}
			unknownByte1 = dis.readByte();
			unknownByte2 = dis.readByte();
			tapAmount = Convert.IntToLittleEndian(dis.readInt());
			taps = new ArrayList<TapInfo>();
			for (i=0; i<tapAmount; i++){
				byte[] buffer = new byte[TapInfo.DATA_LENGTH];
				readCounter = dis.read(buffer, 0, TapInfo.DATA_LENGTH);
				TapInfo tap = new TapInfo(buffer);
				taps.add(tap);
			}
			dis.close();
			in.close();
			System.out.println(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getSongLength(){
		return length;
	}

	public ArrayList<Beat> getBeats(){
		return beats;
	}
	
	public ArrayList<TapInfo> getTaps() {
		return taps;
	}
	
	@Override
	public String toString(){
		String info = "歌曲长度："+length+"ms\n";
		info += "节拍数量："+beatAmount + "\n";
		info += "Tap数量："+tapAmount;
		return info;
	}
}
