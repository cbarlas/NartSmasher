package com.technarts.nartsmasher;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

public class Intern extends Sprite {

	private boolean isSmashed = false;
	
	private boolean controlled = false;
	
	private Sound sound;
	
	private Scene scene;
	
	private ITextureRegion smashedTextureRegion;
	
	public Intern(float pX, float pY, ITextureRegion pTextureRegion,
			final ITextureRegion smashedTextureRegion, final Sound sound, 
			final Scene scene) {
		super(pX, pY, pTextureRegion, GameActivity.getInstance().getVertexBufferObjectManager());
		this.scene = scene;
		this.sound = sound;
		this.smashedTextureRegion = smashedTextureRegion;
	}
	
	public boolean isSmashed() {
		return isSmashed;
	}
	
	public void control() {
		controlled = true;
	}
	
	public boolean isControlled() {
		return controlled;
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (this.isVisible() && GamePlayScene.life > 0) {
			this.setVisible(false);
			this.detachSelf();
			isSmashed = true;
			sound.play();
			GamePlayScene.score += GamePlayScene.scorePoint;
			if (GamePlayScene.score >= GamePlayScene.highScore) {
				GamePlayScene.highScore = GamePlayScene.score;
				GamePlayScene.hiScoreText.setText("High Score " + GamePlayScene.highScore);
				GameActivity.getInstance().getEditor().putInt("hiScore", GamePlayScene.highScore);
				GameActivity.getInstance().getEditor().commit();
			}
			GamePlayScene.scoreText.setText("Score " + GamePlayScene.score);
			final Sprite smashedIntern = new Sprite(this.getX(), this.getY(),
					smashedTextureRegion, GameActivity.getInstance().getVertexBufferObjectManager());
			scene.attachChild(smashedIntern);
			TimerHandler time = new TimerHandler(1f, true, new ITimerCallback() {
				public void onTimePassed(TimerHandler pTimerHandler) {
					smashedIntern.setVisible(false);
					smashedIntern.detachSelf();
				};
			});
			scene.registerUpdateHandler(time);
		}
		return true;
		
		
	}
}
