package com.undsf.fifp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	LiveStage liveStage;
	//Texture background;
	//Texture scorebar;
	//Texture[] avatars;
	//float[][] avatarCoords;

	//public static final int TAP_ORIGIN_X = 480;
	//public static final int TAP_ORIGIN_Y = 480;
	//public static final int INDEX_X = 0;
	//public static final int INDEX_Y = 1;

	//public static final int IDOL_AMOUNT = 9;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		liveStage = new LiveStage();
		//background = new Texture("backgrounds/06.png");
		//scorebar = new Texture("ui/scorebar.png");
		//avatars = new Texture[IDOL_AMOUNT];
		//avatarCoords = new float[IDOL_AMOUNT][2];
		//for (int i=0; i<IDOL_AMOUNT; i++){
		//	String filename = "avatars/u_2100" + (i+1) + "001_normal_icon.png";
		//	avatars[i] = new Texture(filename);
		//	double angle = i * Math.PI / (IDOL_AMOUNT - 1);
		//	avatarCoords[i][INDEX_X] = (float)(TAP_ORIGIN_X - 400*Math.cos(angle)) - 64;
		//	avatarCoords[i][INDEX_Y] = (float)(TAP_ORIGIN_Y - 400*Math.sin(angle)) - 64;
		//	System.out.println(avatarCoords[i][INDEX_X]+","+avatarCoords[i][INDEX_Y]);
		//}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		int canvasWidth = Gdx.app.getGraphics().getWidth();
		int canvasHeight = Gdx.app.getGraphics().getHeight();

		batch.begin();
		liveStage.act();
		liveStage.draw();
		//batch.draw(background, 0, 0);
		//batch.draw(scorebar, 0, canvasHeight - 97);
		//for (int i=0; i<IDOL_AMOUNT; i++) {
		//	batch.draw(avatars[i], avatarCoords[i][INDEX_X], avatarCoords[i][INDEX_Y]);
		//}
		batch.end();
	}
}
