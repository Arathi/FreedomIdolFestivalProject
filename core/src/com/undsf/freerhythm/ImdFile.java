package com.undsf.freerhythm;

import java.io.*;
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

	public ImdFile(){
		length = 0;
		beatAmount = 0;
		beats = new ArrayList<Beat>();
		tapAmount = 0;
		taps = new ArrayList<TapInfo>();
	}

	public ImdFile(String path){
		file = new File(path);
	}
	
	public ImdFile(File file){
		this.file = file;
	}

	public void readHeader(DataInputStream dis) throws IOException {
		//读取扩展的文件头
	}
	public void readTailed(DataInputStream dis) throws IOException {
		//读取扩展的文件尾
	}

	public void writeHeader(DataOutputStream dos) throws IOException {
		//写入扩展的文件头
	}
	public void writeTailed(DataOutputStream dos) throws IOException {
		//写入扩展的文件尾
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
			readHeader(dis);
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
			readTailed(dis);
			dis.close();
			in.close();
			System.out.println(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write() {
		write(this.file);
	}

	public void write(File file) {
		if (file == null) {
			System.err.println("数据文件路径未设置！");
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream dos = new DataOutputStream(fos);
			writeHeader(dos);
			dos.writeInt( Convert.IntToLittleEndian(length) );
			dos.writeInt( Convert.IntToLittleEndian(beatAmount) );
			for (int beatID=0; beatID<beatAmount; beatID++){
				Beat beat = beats.get(beatID);
				//TODO Beat类要实现getByteArray()
				byte[] beatByteArray = new byte[Beat.DATA_LENGTH];
				dos.write(beatByteArray);
			}
			dos.writeByte(unknownByte1);
			dos.writeByte(unknownByte2);
			dos.writeInt( Convert.IntToLittleEndian(tapAmount) );
			for (int tapID=0; tapID<tapAmount; tapID++){
				TapInfo tap = taps.get(tapID);
				byte[] tapByteArray = tap.toByteArray();
				dos.write(tapByteArray);
			}
			writeTailed(dos);
			dos.close();
			fos.close();
		}
		catch (IOException e){
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

	public void addTap(TapInfo tap){
		if (taps == null) taps = new ArrayList<>();
		taps.add(tap);
		tapAmount = taps.size();
	}

	public void setLength() {
		//TODO 通过最大tap结束时间的计算歌曲长度
		int lastTapEndTime = 0;
		for (TapInfo tap : taps){
			int tapEndTime = (tap.getMode()==TapInfo.MODE_HOLD) ? (tap.getTimestamp() + tap.getParam()) : tap.getTimestamp();
			if (tapEndTime > lastTapEndTime) lastTapEndTime = tapEndTime;
		}
		setLength(lastTapEndTime);
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	@Override
	public String toString(){
		String info = "歌曲长度："+length+"ms\n";
		info += "节拍数量："+beatAmount + "\n";
		info += "Tap数量："+tapAmount;
		return info;
	}
}
