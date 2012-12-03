package in.globalspace.android.dbclass;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public class LeaklessCursor extends SQLiteCursor {
	static final String TAG = "LeaklessCursor";

	public LeaklessCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	@Override
	public void close() {
		final SQLiteDatabase db = getDatabase();
		super.close();
		if (db != null) {
			Log.d(TAG, "Closing LeaklessCursor: " + db.getPath());
			db.close();
		}
	}
}
