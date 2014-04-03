package util.android.util;

import util.android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ImageDialogBuilder extends AlertDialog.Builder {

	ImageView image;
	Context context;
	int button_background;
	
	public ImageDialogBuilder(Context context) {
		super(context);
		this.context = context;

		image = (ImageView) View
				.inflate(context, R.layout.fragment_file, null);
		setView(image);
		
	}
	
	public ImageDialogBuilder setImage(String url) {
//		UrlImageViewHelper.setUrlDrawable(image, url);
		return this;
	}
	
	public ImageDialogBuilder setImageResource(int resId) {
		image.setImageResource(resId);
		return this;
	}
	
	public ImageDialogBuilder setButtonColor(int buttonResId) {
		button_background = buttonResId;
		return this;
	}

	@Override
	public AlertDialog show() {
		AlertDialog alert = super.show();

		if (button_background != 0) {
			try {
				((Button) alert.findViewById(android.R.id.button1))
						.setBackgroundResource(button_background);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				((Button) alert.findViewById(android.R.id.button2))
						.setBackgroundResource(button_background);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				((Button) alert.findViewById(android.R.id.button3))
						.setBackgroundResource(button_background);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return alert;
	}
}