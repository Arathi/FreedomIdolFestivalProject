package com.undsf.freerhythm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.undsf.util.Convert;

public class SongConfigClient {
	public static final int DATA_LENGTH = 830;
	
	public short m_ushSongID;
	public int m_iVersion;
	public String m_szSongName;
	public String m_szPath;
	public String m_szArtist;
	public String m_szComposer;
	public String m_szSongTime;
	public int m_iGameTime;
	public int m_iRegion;
	public int m_iStyle;
	public short m_ucIsNew;
	public short m_ucIsHot;
	public short m_ucIsRecommend;
	public String m_szBPM;
	public short m_ucIsOpen;
	public boolean m_ucCanBuy;
	public int m_iOrderIndex;
	public boolean m_bIsFree;
	public boolean m_bSongPkg;
	public String m_szFreeBeginTime;
	public String m_szFreeEndTime;
	public short m_ush4KeyEasy;
	public short m_ush4KeyNormal;
	public short m_ush4KeyHard;
	public short m_ush5KeyEasy;
	public short m_ush5KeyNormal;
	public short m_ush5KeyHard;
	public short m_ush6KeyEasy;
	public short m_ush6KeyNormal;
	public short m_ush6KeyHard;
	public short m_iPrice;
	public String m_szNoteNumber;
	public String m_szProductID;
	public int m_iVipFlag;
	public boolean m_bIsHide;
	public boolean m_bIsReward;
	public boolean m_bIsLevelReward;
	
	public SongConfigClient(byte[] data){
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		try {
			int readCounter = 0;
			byte[] buffer = new byte[128];
			m_ushSongID = Convert.ShortToLittleEndian(dis.readShort());
			m_iVersion = Convert.IntToLittleEndian(dis.readInt());
			readCounter = dis.read(buffer, 0x00, 0x40);
			m_szSongName = Convert.BytesToString(buffer);
			readCounter = dis.read(buffer, 0x00, 0x40);
			m_szPath = Convert.BytesToString(buffer);
			readCounter = dis.read(buffer, 0x00, 0x40);
			m_szArtist = Convert.BytesToString(buffer);
			readCounter = dis.read(buffer, 0x00, 0x40);
			m_szComposer = Convert.BytesToString(buffer);
			readCounter = dis.read(buffer, 0x00, 0x40);
			m_szSongTime = Convert.BytesToString(buffer);
			m_iGameTime = Convert.IntToLittleEndian(dis.readInt());
			m_iRegion = Convert.IntToLittleEndian(dis.readInt());
			m_iStyle = Convert.IntToLittleEndian(dis.readInt());
			m_ucIsNew = Convert.ShortToLittleEndian(dis.readShort());
			m_ucIsHot = Convert.ShortToLittleEndian(dis.readShort());
			m_ucIsRecommend = Convert.ShortToLittleEndian(dis.readShort());
			readCounter = dis.read(buffer, 0x00, 0x40);
			m_szBPM = Convert.BytesToString(buffer);
			m_ucIsOpen = Convert.ShortToLittleEndian(dis.readShort());
			m_ucCanBuy = dis.readBoolean();
			readCounter = dis.read(buffer, 0x00, 0x40);
			m_szFreeBeginTime = Convert.BytesToString(buffer);
			readCounter = dis.read(buffer, 0x00, 0x40);
			m_szFreeEndTime = Convert.BytesToString(buffer);
			m_ush4KeyEasy   = Convert.ShortToLittleEndian(dis.readShort());
			m_ush4KeyNormal = Convert.ShortToLittleEndian(dis.readShort());
			m_ush4KeyHard   = Convert.ShortToLittleEndian(dis.readShort());
			m_ush5KeyEasy   = Convert.ShortToLittleEndian(dis.readShort());
			m_ush5KeyNormal = Convert.ShortToLittleEndian(dis.readShort());
			m_ush5KeyHard   = Convert.ShortToLittleEndian(dis.readShort());
			m_ush6KeyEasy   = Convert.ShortToLittleEndian(dis.readShort());
			m_ush6KeyNormal = Convert.ShortToLittleEndian(dis.readShort());
			m_ush6KeyHard   = Convert.ShortToLittleEndian(dis.readShort());
			m_iPrice = Convert.ShortToLittleEndian(dis.readShort());
			readCounter = dis.read(buffer, 0x00, 0x80);
			m_szNoteNumber = Convert.BytesToString(buffer);
			readCounter = dis.read(buffer, 0x00, 0x80);
			m_szProductID = Convert.BytesToString(buffer);
			m_iVipFlag = Convert.IntToLittleEndian(dis.readInt());
			m_bIsHide  = dis.readBoolean();
			m_bIsReward  = dis.readBoolean();
			m_bIsLevelReward  = dis.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		String info = "=================\n";
		info += "歌曲ID：" + m_ushSongID + "\n";
		info += "歌曲名称：" + m_szSongName + "\n";
		info += "歌曲标识：" + m_szPath + "\n";
		info += "歌手：" + m_szArtist + "\n";
		info += "作谱人：" + m_szComposer + "";
		//info += "游戏时长：" + m_iGameTime; //TODO增加
		return info;
	}
}
