package util.android.lib;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

public interface CacheUnitInterface {
	
	public abstract boolean allocate(String key, Bitmap bitmap);
	public abstract Bitmap fetch(String key);
	public abstract void clear();
	public abstract String getHashName(String key);
}
