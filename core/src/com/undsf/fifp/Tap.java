package com.undsf.fifp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Created by Arathi on 2016-01-05.
 */
public class Tap extends Actor {
    public static final int TAP_TYPE_RING = 1; //环
    public static final int TAP_TYPE_CANAL = 2; //管

    private int target;
    private TextureRegion ringImage;

    public Tap() {
        init(0, TAP_TYPE_RING, 0, false, false);
    }

    public Tap(int target){
        init(target, TAP_TYPE_RING, 0, false, false);
    }

    public Tap(int target, int type, float length, boolean star) {
        init(target, type, length, star, false);
    }

    public Tap(int target, int type, float length, boolean star, boolean item){
        init(target, type, length, star, item);
    }

    public void init(int target, int type, float length, boolean star, boolean item) {
        this.target = target;
        Texture texture = new Texture("ui/ring.png");
        ringImage = new TextureRegion(texture);
        setScale(0.0f, 0.0f);
        setPosition(Constants.TAP_START_X-Constants.AVATAR_WIDTH/2, Constants.TAP_START_Y-Constants.AVATAR_HEIGHT/2);
        setOrigin(ringImage.getRegionWidth()/2,ringImage.getRegionHeight()/2);
        this.setVisible(false);
    }

    public int getTarget() {
        return target;
    }

    public void addMoveAndScaleAction(float fullActionTime) {
        Action actions = Actions.parallel(
                Actions.delay(0.1f, Actions.show()),
                Actions.moveTo(Constants.TAP_END_COORDINATE[6][0], Constants.TAP_END_COORDINATE[6][1], fullActionTime),
                Actions.scaleTo((float)Constants.FULL_ACTION_RATE, (float)Constants.FULL_ACTION_RATE, fullActionTime)
        );
        this.addAction(actions);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(ringImage, getX(), getY(), getOriginX(), getOriginY(), ringImage.getRegionWidth(), ringImage.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
