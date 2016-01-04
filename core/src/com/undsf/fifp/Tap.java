package com.undsf.fifp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Arathi on 2016-01-05.
 */
public class Tap extends Actor {
    private TextureRegion ringImage;

    public Tap(){
        Texture texture = new Texture("ui/ring.png");
        ringImage = new TextureRegion(texture);
        setScale(0.1f, 0.1f);
        setPosition(480-64, 480-64);
        setOrigin(ringImage.getRegionWidth()/2,ringImage.getRegionHeight()/2);
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
