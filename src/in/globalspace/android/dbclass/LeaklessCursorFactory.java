package in.globalspace.android.dbclass;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class LeaklessCursorFactory implements CursorFactory {
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery,
        String editTable, SQLiteQuery query) {
        return new LeaklessCursor(db,masterQuery,editTable,query);
    }
}	
