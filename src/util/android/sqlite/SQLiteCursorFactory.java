package util.android.sqlite;

import util.android.util.AndroidUtil;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.os.Build;
import android.util.Log;

/**
 * Implement the cursor factory in order to log the queries before returning the cursor
 * 
 * @author Vincent @ MarvinLabs
 */
public class SQLiteCursorFactory implements CursorFactory {

	private boolean	debugQueries	= false;

	public SQLiteCursorFactory() {
		this.debugQueries = false;
	}

	public SQLiteCursorFactory(boolean debugQueries) {
		this.debugQueries = debugQueries;
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
		if (debugQueries) {
			Log.d("SQL", query.toString());
		}

		if (AndroidUtil.getAndroidVersion() >= Build.VERSION_CODES.HONEYCOMB) {
			return new SQLiteCursor(masterQuery, editTable, query);
		} else {
			return new SQLiteCursor(db, masterQuery, editTable, query);
		}
	}
}
