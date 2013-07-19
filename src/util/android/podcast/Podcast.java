package util.android.podcast;

import android.provider.BaseColumns;

public class Podcast implements BaseColumns {

	public static final String	TABLE_NAME	= "podcasts";

	public static final String	TITLE		= "title";
	public static final String	CHANNEL_URL	= "channel";
	public static final String	DESCRIPTION	= "description";
	public static final String	IMAGE_URL	= "image";
	public static final String	SUBSCRIBED	= "subscribed";
	public static final String	STORAGE_DIR	= "storage";

	public static final String	CREATE_SQL	= "CREATE TABLE " + TABLE_NAME + "();";
}
