package com.technarts.nartsmasher;

import java.io.IOException;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
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

public class GameOverScene extends Scene implements IOnMenuItemClickListener {
	
	private Font font;
	
	private static final int RESTART_ID = 0;
	
	private static final int QUIT_ID = 1;
	
	private MenuScene childScene;
	
	private MainMenuScene nextScene;
	
	private Text text;
	
	public GameOverScene() {
		try {
			childScene = new MenuScene(GameActivity.getInstance().getCamera());
			setChildScene(childScene);
			childScene.setBackground(new Background(0,0,Color.BLACK));
			font = FontFactory.createStrokeFromAsset(GameActivity.getInstance().getFontManager(),new BitmapTextureAtlas(GameActivity.getInstance().getTextureManager(),256,256, TextureOptions.BILINEAR_PREMULTIPLYALPHA), GameActivity.getInstance().getAssets(), "beech.ttf", 50, true, Color.parseColor("#FFD700"), 2, Color.BLACK);
			font.load();
			text = new Text(250, 155, font, "Game Over!", 100, GameActivity.getInstance().getVertexBufferObjectManager());
			childScene.attachChild(text);
			ITexture restartTexture = new BitmapTexture(GameActivity.getInstance().getTextureManager(), new InputStreamOpener(GameActivity.getInstance().getAssets(), "gfx/restart.png"));
			restartTexture.load();
			ITextureRegion restartTextureRegion = TextureRegionFactory.extractFromTexture(restartTexture);
			IMenuItem restartMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(RESTART_ID, restartTextureRegion, GameActivity.getInstance().getVertexBufferObjectManager()), 1.2f, 1);
			ITexture quitTexture = new BitmapTexture(GameActivity.getInstance().getTextureManager(), new InputStreamOpener(GameActivity.getInstance().getAssets(), "gfx/quit.png"));
			quitTexture.load();
			ITextureRegion quitTextureRegion = TextureRegionFactory.extractFromTexture(quitTexture);
			IMenuItem quitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT_ID, quitTextureRegion, GameActivity.getInstance().getVertexBufferObjectManager()), 1.2f, 1);
			restartMenuItem.setPosition(150, 150);
			quitMenuItem.setPosition(350, 152);
			childScene.addMenuItem(restartMenuItem);
			childScene.addMenuItem(quitMenuItem);
			childScene.setOnMenuItemClickListener(this);
		} catch (IOException e) {
			Debug.e(e);
		}
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			case RESTART_ID:
				nextScene = new MainMenuScene();
				GameActivity.getInstance().setCurrentScene(nextScene);
				return true;
			case QUIT_ID:
				System.exit(0);
				return true;
			default:
				return false;
		}
	}
}
