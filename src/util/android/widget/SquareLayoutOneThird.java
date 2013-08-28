package util.android.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SquareLayoutOneThird extends FrameLayout {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SquareLayoutOneThird(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public SquareLayoutOneThird(Context context) {
		super(context);
	}

	public SquareLayoutOneThird(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("NewApi")
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		width = (int) (width * 0.333f);
		height = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

		setMeasuredDimension(width, height);
	}

}
