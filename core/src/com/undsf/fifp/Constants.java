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

    public static final int PERFECT_LENGTH = 400;

    public static final int INDEX_X = 0;
    public static final int INDEX_Y = 1;

    public static final int IDOL_AMOUNT = 9;

    public static final double FULL_ACTION_RATE = 2.05;

    //TODO thresholds
    public static final double BAD_THRESHOLD = 0.20;
    public static final double GOOD_THRESHOLD = 0.15;
    public static final double GREAT_THRESHOLD = 0.10;
    public static final double PERFECT_THRESHOLD = 0.05;

    //判定
    public static final int JUDGING_NONE = 0;
    public static final int JUDGING_PERFECT = 1;
    public static final int JUDGING_GREAT = 2;
    public static final int JUDGING_GOOD = 3;
    public static final int JUDGING_BAD = 4;
    public static final int JUDGING_MISS = 5;

    public static float[][] AVATAR_COORDINATE;
    public static float[][] TAP_END_COORDINATE;
    public static double[] TAP_PATH_ANGLE;

    static{
        TAP_PATH_ANGLE = new double[IDOL_AMOUNT];
        AVATAR_COORDINATE = new float[IDOL_AMOUNT][2];
        TAP_END_COORDINATE = new float[IDOL_AMOUNT][2];
        for (int i=0; i<IDOL_AMOUNT; i++){
            double angle = i * Math.PI / (IDOL_AMOUNT-1);
            TAP_PATH_ANGLE[i] = angle;
            AVATAR_COORDINATE[i][INDEX_X] = (float)(TAP_START_X - PERFECT_LENGTH * Math.cos(angle)) - AVATAR_WIDTH / 2;
            AVATAR_COORDINATE[i][INDEX_Y] = (float)(TAP_START_Y - PERFECT_LENGTH * Math.sin(angle)) - AVATAR_HEIGHT / 2;
            TAP_END_COORDINATE[i][INDEX_X] = (float)(TAP_START_X - PERFECT_LENGTH * FULL_ACTION_RATE * Math.cos(angle)) - AVATAR_HEIGHT / 2;
            TAP_END_COORDINATE[i][INDEX_Y] = (float)(TAP_START_X - PERFECT_LENGTH * FULL_ACTION_RATE * Math.sin(angle)) - AVATAR_HEIGHT / 2;
        }
    }
}
