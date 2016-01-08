package com.undsf.freerhythm.llp.json;

/**
 * Created by Arathi on 2016/01/08.
 */
public class Note implements Comparable<Note> {
    public double starttime;
    public double endtime;
    public boolean longnote;
    public boolean parallel;
    public int lane;
    public boolean hold;

    @Override
    public int compareTo(Note o) {
        if (starttime > o.starttime) return 1;
        if (starttime < o.starttime) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return "Note: " + starttime;
    }
}
