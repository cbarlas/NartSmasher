package com.technarts.nartsmasher;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import android.graphics.Color;

public class GamePlayScene extends Scene {

	public static Text hiScoreText;
	public static Text scoreText;
	public static int score = 0;
	public static int highScore;
	public static int life = 3;
	public static int scorePoint = 10;
	public static float level = 1;
	private Font scoreFont;
	private Font lifeFont;
	private Text lifeText;
	private float timePassed = 0;
	private GameOverScene nextScene;
	private PhysicsWorld physicsWorld;
	private Sound smashSound;
	private ITextureRegion[] internRegions;
	private ITextureRegion[] smashedInternRegions;
	
	public GamePlayScene() {
		try {
			scorePoint = 10;
			life = 3;
			score = 0;
			level = 1;
			nextScene = new GameOverScene();
			internRegions = new ITextureRegion[2];
			smashedInternRegions = new ITextureRegion[2];
			ITexture backgroundTexture = new BitmapTexture(GameActivity.getInstance().getTextureManager(), new InputStreamOpener(GameActivity.getInstance().getAssets(), "gfx/mountain.png"));
			ITexture ismailTexture = new BitmapTexture(GameActivity.getInstance().getTextureManager(), new InputStreamOpener(GameActivity.getInstance().getAssets(), "gfx/ismail.png"));
			ITexture smashedIsmailTexture = new BitmapTexture(GameActivity.getInstance().getTextureManager(), new InputStreamOpener(GameActivity.getInstance().getAssets(), "gfx/splashedIsmail.png"));
			ITexture fatihTexture = new BitmapTexture(GameActivity.getInstance().getTextureManager(), new InputStreamOpener(GameActivity.getInstance().getAssets(), "gfx/fatih.png"));
			ITexture smashedFatihTexture = new BitmapTexture(GameActivity.getInstance().getTextureManager(), new InputStreamOpener(GameActivity.getInstance().getAssets(), "gfx/splashedFatih.png"));
			backgroundTexture.load();
			ismailTexture.load();
			smashedIsmailTexture.load();
			fatihTexture.load();
			smashedFatihTexture.load();
			ITextureRegion backgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
			internRegions[0] = TextureRegionFactory.extractFromTexture(ismailTexture);
			internRegions[1] = TextureRegionFactory.extractFromTexture(fatihTexture);
			smashedInternRegions[0] = TextureRegionFactory.extractFromTexture(smashedIsmailTexture);
			smashedInternRegions[1] = TextureRegionFactory.extractFromTexture(smashedFatihTexture);
			physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
			Sprite backGroundSprite = new Sprite(0,0,backgroundTextureRegion, GameActivity.getInstance().getVertexBufferObjectManager());
			scoreFont = FontFactory.createStrokeFromAsset(GameActivity.getInstance().getFontManager(), new BitmapTextureAtlas(GameActivity.getInstance().getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA), GameActivity.getInstance().getAssets(), "beech.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
			lifeFont = FontFactory.createStrokeFromAsset(GameActivity.getInstance().getFontManager(), new BitmapTextureAtlas(GameActivity.getInstance().getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA), GameActivity.getInstance().getAssets(), "beech.ttf", 50, true, Color.RED, 2, Color.BLACK);
			scoreFont.load();
			lifeFont.load();
			scoreText = new Text(10, 30, scoreFont, "Score " + score, 100, GameActivity.getInstance().getVertexBufferObjectManager());
			lifeText = new Text(650, 430, lifeFont, "X X X", 100, GameActivity.getInstance().getVertexBufferObjectManager());
			hiScoreText = new Text(325,30, scoreFont, "High Score " + highScore,100, GameActivity.getInstance().getVertexBufferObjectManager());
			smashSound = SoundFactory.createSoundFromAsset(GameActivity.getInstance().getSoundManager(), GameActivity.getInstance().getApplicationContext(),
					"sounds/smash.mp3");
			attachChild(backGroundSprite);
			attachChild(scoreText);
			attachChild(lifeText);
			attachChild(hiScoreText);
			setTouchAreaBindingOnActionDownEnabled(true);
			loadTimeHandler();
			this.registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void onUpdate(float pSecondsElapsed) {
					physicsWorld.onUpdate(pSecondsElapsed);
					timePassed += pSecondsElapsed;
					if (timePassed >= 10) {
						level += level/10.0;
						scorePoint += 5;
						timePassed = 0;
					}
					if (life == 0) {
						unregisterUpdateHandler(this);
						GameActivity.getInstance().getEngine().registerUpdateHandler(new TimerHandler(2f, true, new ITimerCallback() {
							@Override
							public void onTimePassed(TimerHandler pTimerHandler) {
								GameActivity.getInstance().getEngine().unregisterUpdateHandler(pTimerHandler);
								GameActivity.getInstance().setCurrentScene(nextScene);
							}
						}));
					}
				}
				
				@Override
				public void reset() {}
			});
		} catch(IOException ex) {
			Debug.e(ex);
		}
		
	}
	
	private void loadTimeHandler() {
		registerUpdateHandler(new TimerHandler(0.6f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (life > 0) {
					int rand = (int) (Math.random() * 2);
					final Intern intern = InternFactory.produceIntern(internRegions[rand], smashedInternRegions[rand], smashSound, GamePlayScene.this);
						attachChild(intern);
						addMoveToIntern(intern);
						registerTouchArea(intern);
						registerUpdateHandler(new IUpdateHandler() {
							@Override
							public void onUpdate(float pSecondsElapsed) {
								if (intern.getY() >= 630 && !intern.isSmashed() && !intern.isControlled()) {
									life--;
									intern.control();
									intern.detachSelf();
									switch(life) {
										case 2: {
											lifeText.setText("    X X");
											break;
										}
										case 1: {
											lifeText.setText("        X");
											break;
										}
										default: {
											lifeText.setText("");
											break;
										}
									}
								}
							}
							@Override
							public void reset() {}
						});
					}
				}
		}));
	}
	
	private void addMoveToIntern(Intern intern) {
		FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		Body body = PhysicsFactory.createBoxBody(physicsWorld, intern, BodyType.DynamicBody, objectFixtureDef);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(intern, body));
		body.setLinearVelocity(0, (float)(Math.random()*5 + 3)*level);
	}
}
