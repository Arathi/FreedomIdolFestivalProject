package com.undsf.util;

import java.io.UnsupportedEncodingException;

public class Convert {
	public static int IntToLittleEndian(int bigEndian){
		return IntSwapEndian(bigEndian);
	}
	
	public static int IntToBigEndian(int littleEndian){
		return IntSwapEndian(littleEndian);
	}
	
	public static short ShortToLittleEndian(short bigEndian){
		return ShortSwapEndian(bigEndian);
	}
	
	protected static int IntSwapEndian(int origin) {
        return (origin & 0xFF) << 24
                | (0xFF & origin >> 8) << 16
                | (0xFF & origin >> 16) << 8
                | (0xFF & origin >> 24);
	}
	
	protected static short ShortSwapEndian(short origin) {
        return (short) ((origin & 0xFF) << 8
                | (0xFF & origin >> 8));
	}

	public static String BytesToString(byte[] bytes){
		String str = "";
		//查找第一个'\0'，获取字符串长度
		int length;
		for (length=0; length<bytes.length; length++){
			if (bytes[length] == 0) break;
		}
		//转换
		try {
			str = new String(bytes, 0, length, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
