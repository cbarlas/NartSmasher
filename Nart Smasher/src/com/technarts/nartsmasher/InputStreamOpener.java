package com.technarts.nartsmasher;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.util.adt.io.in.IInputStreamOpener;

import android.content.res.AssetManager;

public class InputStreamOpener implements IInputStreamOpener {

	private String path;
	private AssetManager manager;
	
	public InputStreamOpener(AssetManager manager, String path) {
		this.manager = manager;
		this.path = path;
	}
	
	@Override
	public InputStream open() throws IOException {
		return manager.open(path);
	}
}
