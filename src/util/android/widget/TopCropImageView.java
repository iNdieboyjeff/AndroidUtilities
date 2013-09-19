package util.android.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView to display top-crop scale of an image view.
 * 
 * @author Chris Arriola
 */
public class TopCropImageView extends ImageView {

	public TopCropImageView(Context context) {
		super(context);
		setScaleType(ScaleType.MATRIX);
	}

	public TopCropImageView(Context context, AttributeSet attrib) {
		super(context, attrib);
		setScaleType(ScaleType.MATRIX);
	}

	@Override
	protected boolean setFrame(int l, int t, int r, int b) {
		Matrix matrix = getImageMatrix();

		float scale;
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		try {
			int drawableWidth = getDrawable().getIntrinsicWidth();
			int drawableHeight = getDrawable().getIntrinsicHeight();

			if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
				scale = (float) viewHeight / (float) drawableHeight;
			} else {
				scale = (float) viewWidth / (float) drawableWidth;
			}

			matrix.setScale(scale, scale);
			setImageMatrix(matrix);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return super.setFrame(l, t, r, b);
	}
}
