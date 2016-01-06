package com.undsf.fifp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arathi on 2016-01-04.
 */
public class LiveStage extends Stage {
    private Image tapStartPoint;
    private Image background;
    private Image scorebar;
    private IdolAvatar[] avatars;
    private List<Tap> taps;
    private double tempo; //速率
    private double perfectTime;
    private Music music;
    private Sound sePerfect;
    private Sound seGreat;
    private Sound seGood;
    private Sound seBad;
    private Sound seMiss;
    private List<Buff> buffs;

    public LiveStage() {
        Texture backgroundTexture = new Texture("backgrounds/06.png");
        Texture scorebarTexture = new Texture("ui/scorebar_ng.png");
        Texture tapStartTexture = new Texture("ui/tapstart_ng.png");
        background = new Image(backgroundTexture);
        scorebar = new Image(scorebarTexture);
        tapStartPoint = new Image(tapStartTexture);

        sePerfect = Gdx.audio.newSound(Gdx.files.internal("se/perfect.ogg"));
        seGreat = Gdx.audio.newSound(Gdx.files.internal("se/great.ogg"));
        seGood = Gdx.audio.newSound(Gdx.files.internal("se/good.ogg"));

        perfectTime = 1.0; //Hard级，暂定1秒
        double fullActionTime = perfectTime * Constants.FULL_ACTION_RATE;
        double fullActionLength = Constants.PERFECT_LENGTH * Constants.FULL_ACTION_RATE;

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

        taps = new ArrayList<Tap>();

        Tap tapRing = new Tap(2, Tap.TAP_TYPE_RING, 0, false);
        tapRing.addMoveAndScaleAction((float) fullActionTime);
        this.addActor(tapRing);

        Tap tapCanal = new Tap(6, Tap.TAP_TYPE_CANAL, 100, false);
        tapCanal.addMoveAndScaleAction((float) fullActionTime);
        this.addActor(tapCanal);
    }

    public int judging(int touchTime, int nextPerfectTime) {
        //TODO 具体判定还要结合buff
        double delta = (nextPerfectTime - touchTime) / 1000;
        //过于提前，触击无效
        if (delta >= Constants.BAD_THRESHOLD){
            return Constants.JUDGING_NONE;
        }
        //过于提前，触及有效，根据时差判定BAD和GOOD
        if (delta >= Constants.GOOD_THRESHOLD && delta < Constants.BAD_THRESHOLD){
            return Constants.JUDGING_BAD;
        }
        if (delta >= Constants.GREAT_THRESHOLD && delta < Constants.GOOD_THRESHOLD){
            return Constants.JUDGING_GOOD;
        }
        if (delta >= Constants.PERFECT_THRESHOLD && delta < Constants.GREAT_THRESHOLD){
            return Constants.JUDGING_GOOD;
        }
        //在perfect范围内，判定为perfect
        if (delta >= -Constants.PERFECT_THRESHOLD && delta < Constants.PERFECT_THRESHOLD){
            return Constants.JUDGING_PERFECT;
        }
        //过于滞后
        if (delta >= -Constants.GREAT_THRESHOLD && delta < -Constants.PERFECT_THRESHOLD){
            return Constants.JUDGING_GREAT;
        }
        if (delta >= -Constants.GOOD_THRESHOLD && delta < -Constants.GREAT_THRESHOLD){
            return Constants.JUDGING_GOOD;
        }
        if (delta >= -Constants.BAD_THRESHOLD && delta < -Constants.GOOD_THRESHOLD ){
            return Constants.JUDGING_BAD;
        }
        return Constants.JUDGING_NONE;
    }

    public void playTouchFeedbackEffect(int result) {
        if (result == Constants.JUDGING_PERFECT) {
            sePerfect.play();
        }
        else if (result == Constants.JUDGING_GREAT) {
            seGreat.play();
        }
        else if (result == Constants.JUDGING_GOOD) {
            seGood.play();
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
        for (Tap tap : taps){
            //
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
                //System.out.println("[INFO]  ("+x+","+y+")在Avatar"+id+"("+leftX+"->"+rightX+","+bottomY+"->"+topY+")范围内");
                return id;
            }
            //System.err.println("[ERROR] ("+x+","+y+")不在Avatar"+id+"("+leftX+"->"+rightX+","+bottomY+"->"+topY+")范围内");
        }
        return -1;
    }

    /**
     * 选取正在某路径上运动的离终点最近的tap
     * @param avatarID
     * @return 如果这条路径上没有符合条件的tap，返回null
     */
    public Tap getTapByPath(int avatarID) {
        return null;
    }

    public void update() {
        //定时更新
    }

    @Override
    public void draw() {
        super.draw();
        ////辅助线
        //ShapeRenderer sr = new ShapeRenderer();
        //sr.setAutoShapeType(true);
        //sr.begin();
        //for (int i=0; i<Constants.IDOL_AMOUNT; i++){
        //    sr.rect(Constants.AVATAR_COORDINATE[i][0], Constants.AVATAR_COORDINATE[i][1], Constants.AVATAR_WIDTH, Constants.AVATAR_HEIGHT);
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
        System.out.println("touchDown: " + canvasX + "," + canvasY);
        int avatarID = getAvatarPositionID(canvasX, canvasY);
        if (avatarID==-1){
            return false;
        }
        //检测是否有音符开始
        int result = judging(1000, 1000);
        playTouchFeedbackEffect(result);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        int canvasX = screenX;
        int canvasY = Gdx.graphics.getHeight() - screenY;
        System.out.println("touchUp: " + canvasX + "," + canvasY);
        int avatarID = getAvatarPositionID(canvasX, canvasY);
        if (avatarID==-1){
            return false;
        }
        //检测是否有长音符结束
        //检测是否有音符开始
        //Tap nextTap =
        int result = judging(1000, 1000);
        playTouchFeedbackEffect(0);
        return false;
    }
}
