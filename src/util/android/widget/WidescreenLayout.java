package util.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WidescreenLayout extends RelativeLayout {

	public WidescreenLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WidescreenLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public WidescreenLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int size = width > height ? width : width;
		setMeasuredDimension(size, (int) (size / 1.777));
	}

}
