package com.undsf.freerhythm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.undsf.util.Convert;

public class TResHeadAll {
	public static final int DATA_LENGTH = 136;
	
	public int magic;
	public int version;
	public int unit;
	public int count;
	public String metalibHash;
	public long resVersion;
	public long createTime;
	public String resEncording;
	public String contentHash;
	public int dataOffset;
	
	public TResHeadAll(byte[] data){
		readFromBinary(data);
	}
	
	public TResHeadAll(String xmlContent){
		readFromXml(xmlContent);
	}
	
	public int getUnit() {
		return unit;
	}

	public int getCount() {
		return count;
	}
	
	public String byte2str(byte[] data){
		String str = "";
		return str;
	}

	public void readFromBinary(byte[] data){
		try {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
			int readCounter = 0;
			magic = Convert.IntToLittleEndian(dis.readInt());
			version = Convert.IntToLittleEndian(dis.readInt());
			unit = Convert.IntToLittleEndian(dis.readInt());
			count = Convert.IntToLittleEndian(dis.readInt());
			byte[] buffer = new byte[32];
			readCounter = dis.read(buffer, 0x00, 0x20);
			if (readCounter<0x20) throw new IOException("MetalibHash长度不足"); 
			metalibHash = Convert.BytesToString(buffer); //new String(buffer, 0, 0x20, "UTF-8");
			dis.skipBytes(0x10);
			readCounter = dis.read(buffer, 0x00, 0x20);
			if (readCounter<0x20) throw new IOException("ResEncording长度不足");
			resEncording = Convert.BytesToString(buffer); //, 0, 0x20, "UTF-8");
			readCounter = dis.read(buffer, 0x00, 0x20);
			if (readCounter<0x20) throw new IOException("ContentHash长度不足");
			contentHash = Convert.BytesToString(buffer); //new String(buffer, 0, 0x20, "UTF-8");
			dis.skipBytes(0x04);
			dataOffset = Convert.IntToLittleEndian(dis.readInt());
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readFromXml(String xmlContent){
		
	}
	
	public String toString(){
		String headerInfo = "";
		char[] magicText = new char[4];
		magicText[0] = (char) ((magic & 0xFF000000) >>> 24);
		magicText[1] = (char) ((magic & 0xFF0000) >>> 16);
		magicText[2] = (char) ((magic & 0xFF00) >>> 8);
		magicText[3] = (char) (magic & 0xFF);
		headerInfo += "文件类型：" + new String(magicText) + "(" + magic + ")" + "\n";
		headerInfo += "版本号：" + version + "\n";
		headerInfo += "单首歌曲数据长度：" + unit + "\n";
		headerInfo += "歌曲数量：" + count + "\n";
		headerInfo += "系统哈希：" + metalibHash + "\n";
		headerInfo += "资源编码：" + resEncording + "\n";
		headerInfo += "内容哈希：" + contentHash + "\n";
		headerInfo += "文件头长度：" + dataOffset;
		return headerInfo;
	}
}
