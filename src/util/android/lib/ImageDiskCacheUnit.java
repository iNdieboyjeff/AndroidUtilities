package util.android.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageDiskCacheUnit extends CacheUnitImpl {

	public static int QUALITY = 80;
	
	public static ImageDiskCacheUnit INSTANCE;
	private static File cacheDir;
	
	public ImageDiskCacheUnit(Context context) {
		INSTANCE = this;
		cacheDir = context.getCacheDir();
	}
	
	@Override
	public boolean allocate(String key, Bitmap bitmap) {
		boolean success = true;
		FileOutputStream os = null;
		try {
			File file = getFile(key);
		    os = new FileOutputStream(file);
		    bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY, os);
		}catch (FileNotFoundException e) {
		    e.printStackTrace();
		    success = false;
		}finally {
		    if (os != null) {
		        try {
		            os.close();
		            purge();
		        } catch (IOException e) {
		            e.printStackTrace();
		            success = false;
		        }
		    }
		}
		return success;
	}
	
	//Returns null if MISS, Bitmap if HIT
	@Override
	public Bitmap fetch(String key) {
		try {
			File file = getFile(key);
			if(file.exists() && file.isFile()) {
				return BitmapFactory.decodeFile(file.getAbsolutePath());
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void clear() {
		File file = getCacheDir();
		deleteAllFiles(file);
	}
	
	/*
	 * INTERNAL METHODS
	 */
	private void purge() {
		//TODO Not yet implemented
	}
	
	private File getFile(String url) {
		String newFileName = getHashName(url);
		File cacheFile = getCacheDir();
		File newCacheFile = new File(cacheFile, newFileName);
		return newCacheFile;
	}
	
	private static File getCacheDir() {
		return cacheDir;
	}
	
	/*
	 * UTILS
	 */
	void deleteAllFiles(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory()) {
	        for (File child : fileOrDirectory.listFiles()) {
	        	deleteAllFiles(child);
	        }
	    }
	    Log.d("ImageDiskCacheUnit:deleteAllFiles", fileOrDirectory.getName());
	    fileOrDirectory.delete();
	}
}
