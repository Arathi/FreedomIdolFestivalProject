package com.undsf.freerhythm;

import com.undsf.util.Convert;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Arathi on 2016/01/08.
 */
public class FrmsFile extends ImdFile {
    byte[] header; //文件头，暂定 FRMS
    int version; //FRMS版本，目前为1

    public FrmsFile(String path){
        super(path);
    }
    public FrmsFile(File file){
        super(file);
    }

    public void readHeader(DataInputStream dis) throws IOException {
        header = new byte[4];
        dis.read(header, 0, 4);
        version = Convert.IntToLittleEndian(dis.readInt());
    }
    public void readTailed(DataInputStream dis) throws IOException {
    }

    public void writeHeader(DataOutputStream dos) throws IOException {
        dos.write(header);
        dos.writeInt(Convert.IntToLittleEndian(version));
    }
    public void writeTailed(DataOutputStream dos) throws IOException {
    }
}
