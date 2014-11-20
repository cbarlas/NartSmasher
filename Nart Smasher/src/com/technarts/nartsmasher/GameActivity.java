package com.technarts.nartsmasher;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GameActivity extends SimpleBaseGameActivity {
	
	private Scene currentScene;
	
	private Camera camera;
	
	private static final int CAMERA_WIDTH = 800;
	
	private static final int CAMERA_HEIGHT = 480;
	
	private static GameActivity instance;
	
	private SharedPreferences pref;
	
	private Editor editor;
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		instance = this;
		Engine engine = new LimitedFPSEngine(pEngineOptions, 60);
		return engine;
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0,0,CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		return engineOptions;
	}
	
	@Override
	protected void onCreateResources() {
		pref = getSharedPreferences("hiScore", GameActivity.MODE_PRIVATE);
		editor = pref.edit();
		GamePlayScene.highScore = pref.getInt("hiScore", 0);
		FontFactory.setAssetBasePath("font/");
	}
	
	@Override
	protected Scene onCreateScene() {
		currentScene = new MainMenuScene();
		return currentScene;
	}

	public void setCurrentScene(Scene scene) {
		currentScene.dispose();
		currentScene.detachChildren();
		currentScene = scene;
		getEngine().setScene(currentScene);
	}
	
	public static GameActivity getInstance() {
		return instance;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public SharedPreferences getPreferences() {
		return pref;
	}
	
	public Editor getEditor() {
		return editor;
	}
}
