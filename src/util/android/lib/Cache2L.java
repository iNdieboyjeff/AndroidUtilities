package util.android.lib;

import android.content.Context;
import android.graphics.Bitmap;

public class Cache2L extends CacheUnitImpl {
	
	public static Cache2L INSTANCE;
	private static LruMemoryCacheUnit L1;
//	private static SoftRefMemoryCacheUnit L1;
	private static ImageDiskCacheUnit L2;
	
	public Cache2L(Context context) {
		INSTANCE = this;
		L1 = new LruMemoryCacheUnit(context);
//		L1 = new SoftRefMemoryCacheUnit(context);
		L2 = new ImageDiskCacheUnit(context);
	}
	
	public static boolean isInitialised() {
		return INSTANCE != null;
	}
	
	@Override
	public boolean allocate(String key, Bitmap bitmap) {
		return 	L1.allocate(key, bitmap) ||
				L2.allocate(key, bitmap);
	}

	@Override
	public Bitmap fetch(String key) {
		Bitmap bitmap = L1.fetch(key);
		if(bitmap == null) {
			bitmap = L2.fetch(key);
			if(bitmap != null) {
				L1.allocate(key, bitmap);
			}
		}
		return bitmap;
	}

	@Override
	public void clear() {
		L1.clear();
		L2.clear();
	}
	
	public void clearL1() {
		L1.clear();
	}
	
	public void clearL2() {
		L2.clear();
	}
}