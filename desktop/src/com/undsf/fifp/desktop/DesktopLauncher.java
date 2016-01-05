package com.undsf.fifp.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.undsf.fifp.Constants;
import com.undsf.fifp.GdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.CANVAS_WIDTH;
		config.height = Constants.CANVAS_HEIGHT;
		config.title = "自由偶像祭 Demo2";
		new LwjglApplication(new GdxGame(), config);
	}
}
