package com.undsf.fifp;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Arathi on 2016-01-05.
 */
public class Tap extends Actor {
    public static final int TAP_TYPE_RING = 1; //环
    public static final int TAP_TYPE_CANAL = 2; //管

    private int type;
    private int target;
    private long perfectTime;
    private float length;
    private boolean star;
    private boolean item;

    private boolean created;
    private boolean killed;

    private TextureRegion ringTexture;
    private TextureRegion terminalTexture;
    private TextureRegion canalTexture;



    @Deprecated
    public Tap() {
        init(0, TAP_TYPE_RING, 0, 0, false, false);
    }

    @Deprecated
    public Tap(int target){
        init(target, TAP_TYPE_RING, 0, 0, false, false);
    }

    @Deprecated
    public Tap(int target, int type, float length, boolean star) {
        init(target, type, 0, length, star, false);
    }

    public Tap(int target, int type, long perfectTime, float length, boolean star, boolean item){
        init(target, type, perfectTime, length, star, item);
    }

    public void init(int target, int type, long perfectTime, float length, boolean star, boolean item) {
        this.target = target;
        this.type = type;
        this.perfectTime = perfectTime;
        this.length = length;
        if (type == TAP_TYPE_RING) this.length = 0;
        this.star = star;
        this.item = item;
        created = false;
        killed = false;

        Texture texture = new Texture("ui/ring.png");
        ringTexture = new TextureRegion(texture);

        setScale(0.0f, 0.0f);
        setPosition(Constants.TAP_START_X-Constants.AVATAR_WIDTH/2, Constants.TAP_START_Y-Constants.AVATAR_HEIGHT/2);
        setOrigin(ringTexture.getRegionWidth()/2, ringTexture.getRegionHeight()/2);
        this.setVisible(false);
    }

    public long getPerfectTime() {
        return perfectTime;
    }

    public int getTarget() {
        return target;
    }

    public float getLength() {
        return length;
    }

    public void addMoveAndScaleAction(float fullActionTime) {
        Action actions = Actions.parallel(
                Actions.delay(0.1f, Actions.show()),
                Actions.moveTo(Constants.TAP_END_COORDINATE[target][0], Constants.TAP_END_COORDINATE[target][1], fullActionTime),
                Actions.scaleTo((float)Constants.FULL_ACTION_RATE, (float)Constants.FULL_ACTION_RATE, fullActionTime)
        );
        this.addAction(actions);
        setCreated();
    }

    public void addScaleAndFadeAction() {
        //停止之前的动作
        Array<Action> actions = this.getActions();
        for (Action action : actions){
            this.removeAction(action);
        }
        //开始放大渐隐
        Action action = Actions.parallel(
                Actions.fadeOut(0.15f),
                Actions.scaleTo(2, 2, 0.15f)
        );
        this.addAction(action);
        setKilled();
    }

    public void setKilled() {
        killed = true;
    }
    public void setCreated() {
        created = true;
    }
    public boolean isKilled() {
        return killed;
    }
    public boolean isCreated() {
        return created;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        batch.draw(ringTexture, getX(), getY(), getOriginX(), getOriginY(), ringTexture.getRegionWidth(), ringTexture.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
        if ( type == TAP_TYPE_CANAL ){
            //测距
            float deltaX = Math.abs(getX() - (Constants.TAP_START_X - Constants.AVATAR_WIDTH/2));
            float deltaY = Math.abs(getY() - (Constants.TAP_START_Y - Constants.AVATAR_WIDTH/2));
            float distance = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY);
            if (distance > length) {
                //TODO 绘制末端
                //TODO 绘制中间部分
            }
            else {
                //TODO 绘制中间部分
                Image img = null;
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
