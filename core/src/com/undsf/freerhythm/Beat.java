package com.undsf.freerhythm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.undsf.util.Convert;

public class Beat {
	public static final int DATA_LENGTH = 12;
	
	int timestamp; //毫秒时间戳
	byte[] unknown6Bytes; //未知功能的6个字节，全是0x00
	short bpm; //4000+bpm
	
	public Beat(byte[] buffer) {
		try {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer));
			timestamp = Convert.IntToLittleEndian(dis.readInt());
			dis.skipBytes(0x06); //6个字节功能未知
			bpm = Convert.ShortToLittleEndian(dis.readShort());
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
