package com.undsf.fifp;

/**
 * Created by Arathi on 2016-01-04.
 */
public class Constants {
    public static final int CANVAS_WIDTH = 960;
    public static final int CANVAS_HEIGHT = 640;

    public static final int TAP_START_X = 480;
    public static final int TAP_START_Y = 480;

    public static final int AVATAR_WIDTH = 128;
    public static final int AVATAR_HEIGHT = 128;

    public static final int INDEX_X = 0;
    public static final int INDEX_Y = 1;

    public static final int IDOL_AMOUNT = 9;

    public static float[][] AVATAR_COORDINATE;

    static{
        AVATAR_COORDINATE = new float[IDOL_AMOUNT][2];
        for (int i=0; i<IDOL_AMOUNT; i++){
            double angle = i * Math.PI / 8;
            AVATAR_COORDINATE[i][INDEX_X] = (float)(TAP_START_X - 400*Math.cos(angle)) - AVATAR_WIDTH / 2;
            AVATAR_COORDINATE[i][INDEX_Y] = (float)(TAP_START_Y - 400*Math.sin(angle)) - AVATAR_HEIGHT / 2;
        }
    }
}
