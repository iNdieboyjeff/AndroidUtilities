package util.android.widget;

import util.android.lib.Cache2L;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class Image extends ImageView {
	
	AsyncImageDownloader aid;
	String url;
	
	public Image(Context context) {
		super(context);
		initialise();
	}
	
	public Image(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialise();
	}
	
	public Image(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialise();
	}
	
	public void initialise() {
	}
	
	public void load(String url) {
		if(url != null) {
			this.url = url;
			downloadImageBitmap(url);
		}
	}
	
	public void loadInlineCached(String url) {
		if(url != null) {
			this.url = url;
			Bitmap bm = fetchCachedBitmap(url);
			setImageBitmap(bm);
		}
	}
	
	/*
	 * GETTER & SETTER
	 */
	public Bitmap fetchCachedBitmap(String url) {
		if(Cache2L.isInitialised()) return Cache2L.INSTANCE.fetch(url);
		return null;
	}
	
	public boolean isRequested(String url) {
		//TODO Not yet implemented
		return false;
	}
	
	public void downloadImageBitmap(String url) {
		if(aid != null) aid.cancel(true);
		aid = new AsyncImageDownloader();
		aid.execute(url);
	}
	
	/*
	 * ASYNC FETCH
	 */
	private class AsyncImageDownloader extends AsyncTask<String, Integer, Boolean> {
		Bitmap bm;
		@Override
		protected Boolean doInBackground(String... urls) {
			String url = urls[0];
			try{
				bm = fetchCachedBitmap(url);
				if(bm == null) {
					bm = ((BitmapDrawable) getDrawableFromUrl(url)).getBitmap();
					if(bm != null) {
						Cache2L.INSTANCE.allocate(url, bm);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			publishProgress(0);
			return true;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			if(!isCancelled()) {
				setImageBitmap(bm);
//				requestLayout();
			}
		}
		
		@Override
		protected void onPostExecute(Boolean success) {
		}
	}
	
	public boolean isThreadLatest(String url) {
		return this.url.equals(url);
	}
	
	public void setImageBitmapIfLatest(Bitmap bm, String url) {
		if(isThreadLatest(url)) setImageBitmap(bm);
	}
	
	/*
	 * NETWORK
	 */
	private static Drawable getDrawableFromUrl(final String url) {
		Log.d("Image:getDrawableFromUrl", "url:"+url);
		try {
			Drawable d = Drawable.createFromStream(((java.io.InputStream) new java.net.URL(url).getContent()), "name");
			return d;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
