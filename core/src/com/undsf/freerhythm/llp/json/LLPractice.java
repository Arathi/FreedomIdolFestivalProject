package com.undsf.freerhythm.llp.json;

import com.undsf.freerhythm.ImdFile;
import com.undsf.freerhythm.TapInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Arathi on 2016/01/08.
 */
public class LLPractice {
    protected int speed;
    protected String audiofile;
    protected List<List<Note>> lane;

    @Override
    public String toString() {
        int noteAmount = 0;
        for (List<Note> l : lane){
            noteAmount += l.size();
        }
        return "速度：" + speed + "bpm" + "\n" +
                "音频文件名：" + audiofile + "\n" +
                "音符数量：" + noteAmount;
    }

    public ImdFile toImd(){
        ImdFile imd = new ImdFile();
        List<Note> notes = new ArrayList<Note>();
        for (List<Note> l : lane) {
            notes.addAll(l);
        }
        //sort by starttime
        Collections.sort(notes);
        for (Note n : notes) {
            //System.out.println(n);
            TapInfo tap = new TapInfo(TapInfo.MODE_CLICK, (int)n.starttime, (byte)(n.lane & 0xFF), 0);
            imd.addTap(tap);
        }
        imd.setLength();
        return imd;
    }
}
