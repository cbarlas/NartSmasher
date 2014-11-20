package com.technarts.nartsmasher;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.region.ITextureRegion;

public class InternFactory {

	public static Intern produceIntern(ITextureRegion textureRegion,
			ITextureRegion smashedTextureRegion, Sound sound, Scene scene) {
		Intern toReturn = new Intern((float)Math.random()*700,-100, textureRegion, smashedTextureRegion, sound, scene);
		return toReturn;
	}
}
