package util.android.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

public class LruMemoryCacheUnit extends CacheUnitImpl {

	public static LruMemoryCacheUnit INSTANCE;
	
	private static Context mContext;
	private static int MEMORY_CLASS;
	private static int CHACHE_SIZE;
	private static LruCache mMemoryCache;
	
	public LruMemoryCacheUnit(Context context) {
		INSTANCE = this;
		mContext = context;
		// Get memory class of this device, exceeding this amount will throw an OutOfMemory exception.
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    MEMORY_CLASS = am.getMemoryClass();
	    // Use 1/8th of the available memory for this memory cache
	    CHACHE_SIZE = 1024 * 1024 * MEMORY_CLASS / 8;
	    
	    mMemoryCache = new LruCache(CHACHE_SIZE) {
			@Override
	    	protected int sizeOf(Object key, Object value) {
	    		// The cache size will be measured in bytes rather than number of items.
				Bitmap bitmap = (Bitmap) value;
	            return bitmap.getRowBytes() * bitmap.getHeight();
	    	}
	    };
	}
	
	@Override
	public boolean allocate(String key, Bitmap bitmap) {
		if(mMemoryCache.put(key, bitmap) != null) return true;
		else return false;
	}
	
	//Returns null if MISS, Bitmap if HIT
	@Override
	public Bitmap fetch(String key) {
		return (Bitmap) mMemoryCache.get(key);
	}
	
	@Override
	public void clear() {
		mMemoryCache.evictAll();
	}
}
