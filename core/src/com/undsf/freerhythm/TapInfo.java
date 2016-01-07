package com.undsf.freerhythm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.undsf.util.Convert;

public class TapInfo {
	public static final int DATA_LENGTH = 11;
	
	public static final short MODE_CLICK                = 0x0000;
	public static final short MODE_SLIDE                = 0x0001;
	public static final short MODE_HOLD                 = 0x0002;
	public static final short MODE_POLYLINE_MID_POINT   = 0x0021;
	public static final short MODE_POLYLINE_MID_LINE    = 0x0022;
	public static final short MODE_POLYLINE_START_POINT = 0x0061;
	public static final short MODE_POLYLINE_START_LINE  = 0x0062;
	public static final short MODE_POLYLINE_END_POINT   = 0x00A1;
	public static final short MODE_POLYLINE_END_LINE    = 0x00A2;
	
	//来自文件
	protected short mode;
	protected int timestamp;
	protected byte key;
	protected int param;

	public TapInfo(byte[] buffer) {
		try {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer));
			mode = Convert.ShortToLittleEndian(dis.readShort());
			timestamp = Convert.IntToLittleEndian(dis.readInt());
			key = dis.readByte();
			param = Convert.IntToLittleEndian(dis.readInt());
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		String info = "模式"+mode+"\n";
		info += "时间"+timestamp+"\n";
		info += "键位"+key+"\n";
		info += "属性"+param+"\n";
		return info;
	}
	
	public int getKey(){
		return (int)key;
	}
	
	public int getTimestamp(){
		return timestamp;
	}

	public short getMode() {
		return mode;
	}
}
