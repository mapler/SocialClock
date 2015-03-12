package cn.socialclock.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * @author mapler
 * Manage DB
 */
public class AlarmEventDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "social_clock_db";

    /**
     * Constructor
     */
    public AlarmEventDatabaseHelper(
            Context context,
            CursorFactory factory,
            int version) {
        super(context, DB_NAME, factory, version);
    }

    /**
     * create tables
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String createTableSql: DbConstants.CREATE_TABLE_QUERIES) {
            db.execSQL(createTableSql);
        }
    }

    /**
     * recreate tables with new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String dropTableSql: DbConstants.DROP_TABLE_QUERIES) {
            db.execSQL(dropTableSql);
        }
        for (String createTableSql: DbConstants.CREATE_TABLE_QUERIES) {
            db.execSQL(createTableSql);
        }
    }
}
