package util.android.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SquareLayoutHalfSizePlusAB extends FrameLayout {

	private Context mContext;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SquareLayoutHalfSizePlusAB(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public SquareLayoutHalfSizePlusAB(Context context) {
		super(context);
		this.mContext = context;
	}

	public SquareLayoutHalfSizePlusAB(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	@SuppressLint("NewApi")
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		width = (int) (width * 0.5f);

		int height2 = 0;
		TypedValue typeValue = new TypedValue();
		mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, typeValue, true);
		height2 = TypedValue.complexToDimensionPixelSize(typeValue.data, getResources().getDisplayMetrics());
		width = width + height2;

		height = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

		setMeasuredDimension(width, height);
	}

}
