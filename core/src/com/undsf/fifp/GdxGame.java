package com.undsf.fifp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	LiveStage liveStage;

	@Override
	public void create () {
		batch = new SpriteBatch();
		liveStage = new LiveStage();
		Gdx.input.setInputProcessor(liveStage);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		int canvasWidth = Gdx.app.getGraphics().getWidth();
		int canvasHeight = Gdx.app.getGraphics().getHeight();
		batch.begin();
		liveStage.act();
		liveStage.update();
		liveStage.draw();
		batch.end();
	}
}
