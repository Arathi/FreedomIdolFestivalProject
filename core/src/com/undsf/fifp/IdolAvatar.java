package com.undsf.fifp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Created by Arathi on 2016-01-04.
 */
public class IdolAvatar extends Actor {
    public static final String FILE_NAME_FORMAT_1 = "avatars/u_%d_%s_icon.png";
    public static final String FILE_NAME_FORMAT_2 = "avatars/u_%s_icon_%d.png";

    private int id;
    private boolean awaken;
    private TextureRegion avatar;

    public IdolAvatar(int id, boolean awaken){
        super();
        this.id = id;
        this.awaken = awaken;

        String type = awaken ? "rankup" : "normal";
        this.setWidth(Constants.AVATAR_WIDTH);
        this.setHeight(Constants.AVATAR_HEIGHT);

        //检查文件是否存在
        String avatarFileName = String.format(FILE_NAME_FORMAT_2, type, id);
        FileHandle fileHandle = Gdx.files.internal(avatarFileName);
        if ( fileHandle.exists() == false ) {
            avatarFileName = String.format(FILE_NAME_FORMAT_1, id, type);
            fileHandle = Gdx.files.internal(avatarFileName);
            if ( fileHandle.exists() == false ){
                //TODO 报错！
                //载入一个内置的头像
                avatarFileName = "ui/empty.png";
            }
        }

        Texture avatarTexture = new Texture(avatarFileName);
        avatar = new TextureRegion(avatarTexture);
    }

    public void setPosition(int id){
        //TODO 获取预置的坐标，设置x和y
        setPosition( Constants.AVATAR_COORDINATE[id][Constants.INDEX_X], Constants.AVATAR_COORDINATE[id][Constants.INDEX_Y] );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(avatar, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
