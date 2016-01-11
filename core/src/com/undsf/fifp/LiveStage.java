package com.undsf.fifp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.undsf.freerhythm.ImdFile;
import com.undsf.freerhythm.TapInfo;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Arathi on 2016-01-04.
 */
public class LiveStage extends Stage {
    private Image tapStartPoint;
    private Image background;
    private Image scorebar;
    private IdolAvatar[] avatars;
    private List<LinkedList<Tap>> taps;
    private double perfectTimeDelay;
    private double fullActionTime;
    private Music music;
    private Sound sePerfect;
    private Sound seGreat;
    private Sound seGood;
    private Sound seBad;
    private Sound seMiss;
    private List<Buff> buffs;
    private long liveStartTime;
    private Image imgPerfect;
    private Image imgGreat;
    private Image imgGood;
    private Image imgBad;
    private Image imgMiss;
    private int combo;

    private static final int KEY_CODE_A = 29;
    private static final int KEY_CODE_S = 47;
    private static final int KEY_CODE_D = 32;
    private static final int KEY_CODE_F = 34;
    private static final int KEY_CODE_G = 35;
    private static final int KEY_CODE_H = 36;
    private static final int KEY_CODE_J = 38;
    private static final int KEY_CODE_K = 39;
    private static final int KEY_CODE_L = 40;

    public LiveStage() {
        super(new ScalingViewport(
                Scaling.none,
                960,
                640,
                new OrthographicCamera()
        ), new SpriteBatch());

        Texture backgroundTexture = new Texture("backgrounds/06.png");
        Texture scorebarTexture = new Texture("ui/scorebar_ng.png");
        Texture tapStartTexture = new Texture("ui/tapstart_ng.png");
        background = new Image(backgroundTexture);
        scorebar = new Image(scorebarTexture);
        tapStartPoint = new Image(tapStartTexture);
        //TODO 加载图片资源

        sePerfect = Gdx.audio.newSound(Gdx.files.internal("se/perfect.mp3"));
        seGreat = Gdx.audio.newSound(Gdx.files.internal("se/great.mp3"));
        seGood = Gdx.audio.newSound(Gdx.files.internal("se/good.mp3"));

        perfectTimeDelay = 1.0; //Easy 2秒、Normal 1.5秒、Hard 1秒
        fullActionTime = perfectTimeDelay * Constants.FULL_ACTION_RATE;

        background.setPosition(0, 0);

        tapStartPoint.setOrigin(tapStartTexture.getWidth()/2, tapStartTexture.getHeight()/2);
        tapStartPoint.setPosition(480-tapStartTexture.getWidth()/2, 480-tapStartTexture.getHeight()/2);
        tapStartPoint.setScale(0.5f);
        Action heartbeatOnce = Actions.sequence(
                Actions.scaleTo(0.75f, 0.75f, 0.15f),
                Actions.scaleTo(0.5f,0.5f,0.55f),
                Actions.delay(0.25f)
        );
        Action heartbeats = Actions.forever(heartbeatOnce);
        tapStartPoint.addAction(heartbeats);

        scorebar.setPosition(0, Constants.CANVAS_HEIGHT - scorebar.getHeight());
        this.addActor(background);
        this.addActor(scorebar);
        this.addActor(tapStartPoint);

        int[] idolIDs = {21001001, 21002001, 21005001, 21004001, 21003001, 21006001, 21007001, 21008001, 21009001};
        avatars = new IdolAvatar[Constants.IDOL_AMOUNT];
        for (int i=0; i<Constants.IDOL_AMOUNT; i++){
            avatars[i] = new IdolAvatar(idolIDs[i], true);
            avatars[i].setPosition(i);
            this.addActor(avatars[i]);
        }

        background.addAction(
                Actions.alpha(0.5f, 0.75f)
        );

        taps = new ArrayList<LinkedList<Tap>>();
        for (int i=0; i<Constants.IDOL_AMOUNT; i++) {
            LinkedList<Tap> q = new LinkedList<Tap>();
            taps.add( q );
        }

        //TODO 测试数据
        String songName = "sakura_iro_no_yume";
        int keys = 9;
        String difficulty = "ez";
        FileHandle musicFileHandle = Gdx.files.internal("songs/" + songName + "/" + songName + ".mp3");
        FileHandle musicSheetFileHandle = Gdx.files.internal("songs/" + songName + "/" + songName + "_" + keys + "k_" + difficulty + ".imd");
        music = Gdx.audio.newMusic( musicFileHandle );
        File musicSheet = musicSheetFileHandle.file();
        ImdFile imd = new ImdFile(musicSheet);
        imd.read();
        List<TapInfo> tapInfos = imd.getTaps();

        combo = 0;

        for (TapInfo tapInfo : tapInfos) {
            int type = 0;
            int length = 0;
            if (tapInfo.getMode()==TapInfo.MODE_CLICK || tapInfo.getMode()==TapInfo.MODE_SLIDE) {
                type = Tap.TAP_TYPE_RING;
            }
            else if (tapInfo.getMode()==TapInfo.MODE_HOLD) {
                type = Tap.TAP_TYPE_CANAL;
                length = tapInfo.getParam();
            }
            else {
                continue;
            }
            int lane = tapInfo.getKey();
            int startTime = tapInfo.getTimestamp();
            addTap( lane, type, startTime, length, false );
        }

        liveStartTime = System.currentTimeMillis() + Constants.INTRO_FREEZE_TIME; //开始于1秒后
    }

    public void addTap(int target, int type, int time, float length, boolean star) {
        Tap tap = new Tap(target, type, time, length, star, false);
        this.taps.get(target).add(tap);
    }

    public int judging(long touchTime, long nextPerfectTime) {
        //TODO 具体判定还要结合buff
        double delta = (nextPerfectTime - touchTime) / 1000.0;
        //过于提前，触击无效
        if (delta >= Constants.BAD_THRESHOLD){
            return Constants.JUDGING_NONE;
        }
        //过于提前，触及有效，根据时差判定BAD和GOOD
        if (delta >= Constants.GOOD_THRESHOLD && delta < Constants.BAD_THRESHOLD){
            System.out.println("BAD");
            return Constants.JUDGING_BAD;
        }
        if (delta >= Constants.GREAT_THRESHOLD && delta < Constants.GOOD_THRESHOLD){
            System.out.println("GOOD");
            return Constants.JUDGING_GOOD;
        }
        if (delta >= Constants.PERFECT_THRESHOLD && delta < Constants.GREAT_THRESHOLD){
            System.out.println("GREAT");
            return Constants.JUDGING_GREAT;
        }
        //在perfect范围内，判定为perfect
        if (delta >= -Constants.PERFECT_THRESHOLD && delta < Constants.PERFECT_THRESHOLD){
            System.out.println("PERFECT");
            return Constants.JUDGING_PERFECT;
        }
        //过于滞后
        if (delta >= -Constants.GREAT_THRESHOLD && delta < -Constants.PERFECT_THRESHOLD){
            System.out.println("GREAT");
            return Constants.JUDGING_GREAT;
        }
        if (delta >= -Constants.GOOD_THRESHOLD && delta < -Constants.GREAT_THRESHOLD){
            System.out.println("GOOD");
            return Constants.JUDGING_GOOD;
        }
        if (delta >= -Constants.BAD_THRESHOLD && delta < -Constants.GOOD_THRESHOLD ){
            System.out.println("BAD");
            return Constants.JUDGING_BAD;
        }
        return Constants.JUDGING_NONE;
    }

    public void playTouchFeedbackEffect(Tap tap, int result) {
        if (tap != null) {
            //TODO 停止之前的动画
            tap.addScaleAndFadeAction();
        }
        if (result == Constants.JUDGING_PERFECT) {
            sePerfect.play(1.0f);
        }
        else if (result == Constants.JUDGING_GREAT) {
            seGreat.play(1.0f);
        }
        else if (result == Constants.JUDGING_GOOD) {
            seGood.play(1.0f);
        }
        else if (result == Constants.JUDGING_BAD) {
            //扣分
        }
        else if (result == Constants.JUDGING_MISS) {
            //扣分
        }
        else {
            //点击无效
        }
    }

    public void removeExpiredTaps() {
        //在这里移除失效的tap，如果超时，就标记为MISS
        if (taps == null) return;
        for (LinkedList<Tap> q : taps){
            for (Tap tap : q){
                if (tap.isCreated()==false){
                    break;
                }
                if (tap.isKilled()){
                    continue;
                }
                long liveElapseTime = System.currentTimeMillis() - liveStartTime;
                //TODO 检查MISS超时，扣HP，Combo清零
                if (liveElapseTime > tap.getPerfectTime() + tap.getLength() + Constants.BAD_THRESHOLD*1000){
                    System.out.println("MISS");
                    tap.setKilled();
                    combo = 0;
                }
            }
        }
    }

    /**
     * 通过触击坐标，获取触击的头像的ID
     * @param x
     * @param y
     * @return 触击的头像的ID（0-8），如果触击的不是头像，返回-1
     */
    public int getAvatarPositionID(int x, int y) {
        for (int id=0; id<Constants.IDOL_AMOUNT; id++){
            float leftX, rightX, bottomY, topY;
            leftX = Constants.AVATAR_COORDINATE[id][Constants.INDEX_X];
            rightX = Constants.AVATAR_COORDINATE[id][Constants.INDEX_X] + Constants.AVATAR_WIDTH;
            bottomY = Constants.AVATAR_COORDINATE[id][Constants.INDEX_Y];
            topY = Constants.AVATAR_COORDINATE[id][Constants.INDEX_Y] + Constants.AVATAR_HEIGHT;
            if (x>=leftX && x<=rightX && y>=bottomY && y<=topY){
                return id;
            }
        }
        return -1;
    }

    /**
     * 选取正在某路径上运动的离终点最近的tap
     * @param path
     * @return 如果这条路径上没有符合条件的tap，返回null
     */
    public Tap getTapByPath(int path) {
        if (path<0 || path> Constants.IDOL_AMOUNT) return null;
        LinkedList<Tap> q = taps.get(path);
        if (q==null || q.size()<=0) {
            System.err.println(path+"号路径无法访问或者没有tap");
            return null;
        }
        for (Tap tap : q){
            //TODO 筛选tap
            if (tap.isKilled()) continue;
            if (!tap.isCreated()) break;
            return tap;
        }
        //Tap tap = q.element();
        //TODO 如果取到了失效的tap，重新取
        //if (tap == null) return null;
        return null;
    }

    public void update() {
        long liveElapseTime = System.currentTimeMillis() - liveStartTime;
        if (liveElapseTime >= 0 && !music.isPlaying()){
            music.play();
        }
        //定时更新
        removeExpiredTaps(); //先更新
        for (LinkedList<Tap> q : taps){
            for (Tap tap : q){
                //if (tap.isKilled()) continue;
                if (tap.isCreated()) continue;
                liveElapseTime = System.currentTimeMillis() - liveStartTime;
                if (liveElapseTime < 0) continue;

                if (liveElapseTime + perfectTimeDelay*1000 >= tap.getPerfectTime()) {
                    this.addActor(tap);
                    tap.addMoveAndScaleAction((float) fullActionTime);
                    tap.setCreated();
                }
            }
        }
    }

    @Override
    public void draw() {
        super.draw();
        //辅助线
        //ShapeRenderer sr = new ShapeRenderer();
        //sr.setAutoShapeType(true);
        //sr.begin();
        //for (int i=0; i<Constants.IDOL_AMOUNT; i++){
        //    sr.rect(Constants.AVATAR_COORDINATE[i][0], Constants.AVATAR_COORDINATE[i][1], Constants.AVATAR_WIDTH, Constants.AVATAR_HEIGHT);
        //    sr.line(Constants.TAP_START_X, Constants.TAP_START_Y, Constants.AVATAR_COORDINATE[i][0]+Constants.AVATAR_WIDTH/2, Constants.AVATAR_COORDINATE[i][1]+Constants.AVATAR_WIDTH/2);
        //}
        //sr.end();
    }

    @Override
    public void act() {
        super.act();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        int canvasX = screenX;
        int canvasY = Gdx.graphics.getHeight() - screenY;
        //System.out.println("touchDown: " + canvasX + "," + canvasY);
        int avatarID = getAvatarPositionID(canvasX, canvasY);
        if (avatarID<0 || avatarID>=Constants.IDOL_AMOUNT) {
            return false;
        }
        //检测是否有音符开始
        Tap nextTap = getTapByPath(avatarID);
        if (nextTap == null) return false;

        long liveElapseTime = System.currentTimeMillis() - liveStartTime;
        int result = judging(liveElapseTime, nextTap.getPerfectTime());
        playTouchFeedbackEffect(nextTap, result);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        int canvasX = screenX;
        int canvasY = Gdx.graphics.getHeight() - screenY;
        //System.out.println("touchUp: " + canvasX + "," + canvasY);
        int avatarID = getAvatarPositionID(canvasX, canvasY);
        if (avatarID==-1){
            return false;
        }
        //检测是否有长音符结束
        //检测是否有音符开始
        //Tap nextTap =
        //int result = judging(1000, 1000);
        playTouchFeedbackEffect(null, 0);
        return false;
    }

    @Override
    public boolean keyDown(int keyCode) {
        super.keyDown(keyCode);
        //System.out.println(keyCode);
        int lane = -1;
        switch (keyCode){
        case KEY_CODE_A:
            lane = 0;
            break;
        case KEY_CODE_S:
            lane = 1;
            break;
        case KEY_CODE_D:
            lane = 2;
            break;
        case KEY_CODE_F:
            lane = 3;
            break;
        case KEY_CODE_G:
            lane = 4;
            break;
        case KEY_CODE_H:
            lane = 5;
            break;
        case KEY_CODE_J:
            lane = 6;
            break;
        case KEY_CODE_K:
            lane = 7;
            break;
        case KEY_CODE_L:
            lane = 8;
            break;
        default:
            lane = -1;
        }

        Tap nextTap = getTapByPath(lane);
        if (nextTap == null) return false;
        long liveElapseTime = System.currentTimeMillis() - liveStartTime;
        int result = judging(liveElapseTime, nextTap.getPerfectTime());
        playTouchFeedbackEffect(nextTap, result);
        return false;
    }
}
