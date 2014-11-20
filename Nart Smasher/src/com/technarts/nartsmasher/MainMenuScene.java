package com.technarts.nartsmasher;

import java.io.IOException;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.debug.Debug;
import android.graphics.Color;

public class MainMenuScene extends Scene {

	private Font font;
	
	private GamePlayScene nextScene;
	
	private Text text;
	
	private boolean blinked = false;
	
	private ITextureRegion backgroundTextureRegion;
	
	public MainMenuScene() {
		try {
			nextScene = new GamePlayScene();
			ITexture backgroundTexture = new BitmapTexture(GameActivity.getInstance().getTextureManager(), new InputStreamOpener(GameActivity.getInstance().getAssets(), "gfx/nart.png"));
			backgroundTexture.load();
			backgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
			Sprite backgroundSprite = new Sprite(0,0,backgroundTextureRegion, GameActivity.getInstance().getVertexBufferObjectManager());
			font = FontFactory.createStrokeFromAsset(GameActivity.getInstance().getFontManager(), new BitmapTextureAtlas(GameActivity.getInstance().getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA), GameActivity.getInstance().getAssets(), "beech.ttf", 40, true, Color.WHITE, 2, Color.BLACK);
			font.load();
			text = new Text(120,250, font, "Please touch the screen to start", 100, GameActivity.getInstance().getVertexBufferObjectManager());
			attachChild(backgroundSprite);
			attachChild(text);
			
			setTouchAreaBindingOnActionDownEnabled(true);
			registerUpdateHandler(new TimerHandler(0.5f, true, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					text.setVisible(blinked);
					blinked = !blinked;
				}
			}));
		} catch(IOException ex) {
			Debug.e(ex);
		}
	}
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		if (GameActivity.getInstance().getCurrentScene() != nextScene) {
			GameActivity.getInstance().setCurrentScene(nextScene);
		}
		return true;
	}
}
