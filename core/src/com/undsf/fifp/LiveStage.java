package com.undsf.fifp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

    public LiveStage() {
        Texture backgroundTexture = new Texture("backgrounds/06.png");
        Texture scorebarTexture = new Texture("ui/scorebar_ng.png");
        Texture tapStartTexture = new Texture("ui/tapstart_ng.png");
        background = new Image(backgroundTexture);
        scorebar = new Image(scorebarTexture);
        tapStartPoint = new Image(tapStartTexture);

        perfectTime = 1.0; //Hard级，暂定1秒
        double fullActionTime = perfectTime * Constants.FULL_ACTION_RATE;
        double fullActionLength = Constants.PERFECT_LENGTH * Constants.FULL_ACTION_RATE;

        background.setPosition(0, 0);
        tapStartPoint.setOrigin(tapStartTexture.getWidth()/2, tapStartTexture.getHeight()/2);
        tapStartPoint.setPosition(480-tapStartTexture.getWidth()/2, 480-tapStartTexture.getHeight()/2);
        tapStartPoint.setScale(0.5f);
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

        Tap tap = new Tap(6);
        tap.addMoveAndScaleAction((float) fullActionTime);
        this.addActor(tap);
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void act() {
        super.act();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        System.out.println("touchDown: " + screenX + "," + screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        System.out.println("touchUp: " + screenX + "," + screenY);
        return false;
    }
}
