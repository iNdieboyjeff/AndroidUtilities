package util.android.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSlideViewPager extends ViewPager {

	 public NonSlideViewPager(Context context) {
	        super(context);
	    }

	    public NonSlideViewPager(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	    
	
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // Never allow swiping to switch between pages
        return false;
    }
}
