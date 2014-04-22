package util.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Switch;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class EditSwitch extends android.widget.Switch {

	public EditSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public EditSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public EditSwitch(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void requestLayout() {
	    String TAG = "Switch";
		Log.i(TAG , "requestLayout");
	    try {
	        java.lang.reflect.Field mOnLayout = Switch.class.getDeclaredField("mOnLayout");
	        mOnLayout.setAccessible(true);
	        mOnLayout.set(this, null);
	        java.lang.reflect.Field mOffLayout = Switch.class.getDeclaredField("mOffLayout");
	        mOffLayout.setAccessible(true);
	        mOffLayout.set(this, null);
	    } catch (Exception x) {
	        Log.e(TAG, x.getMessage(), x);
	    }
	    super.requestLayout();
	}
	
}
