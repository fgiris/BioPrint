package com.fizyoteknoterapi.muhendis.bioprint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by muhendis on 16.05.2017.
 */

public final class FeedReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "userInfo";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_BIRTH_DAY = "birthDay";
        public static final String COLUMN_NAME_BIRTH_MONTH = "birthMonth";
        public static final String COLUMN_NAME_BIRTH_YEAR = "birthYear";
        public static final String COLUMN_NAME_TEST_DATE = "testDate";
        public static final String COLUMN_NAME_TEST_COMPLETED = "testCompleted";
        public static final String COLUMN_NAME_TEST_RESULT = "testResult";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        FeedEntry.COLUMN_NAME_NAME + " TEXT," +
                        FeedEntry.COLUMN_NAME_SURNAME + " TEXT," +
                        FeedEntry.COLUMN_NAME_GENDER + " TEXT," +
                        FeedEntry.COLUMN_NAME_AGE+ " TEXT," +
                        FeedEntry.COLUMN_NAME_HEIGHT + " TEXT," +
                        FeedEntry.COLUMN_NAME_WEIGHT + " TEXT," +
                        FeedEntry.COLUMN_NAME_BIRTH_DAY + " TEXT," +
                        FeedEntry.COLUMN_NAME_BIRTH_MONTH + " TEXT," +
                        FeedEntry.COLUMN_NAME_BIRTH_YEAR + " TEXT," +
                        FeedEntry.COLUMN_NAME_TEST_DATE + " TEXT," +
                        FeedEntry.COLUMN_NAME_TEST_COMPLETED + " INTEGER DEFAULT 0," +
                        FeedEntry.COLUMN_NAME_TEST_RESULT + " TEXT)";


        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

        public static class FeedReaderDbHelper extends SQLiteOpenHelper {
            // If you change the database schema, you must increment the database version.
            public static final int DATABASE_VERSION = 4;
            public static final String DATABASE_NAME = "FeedReader.db";

            public FeedReaderDbHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_ENTRIES);
            }
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // This database is only a cache for online data, so its upgrade policy is
                // to simply to discard the data and start over
                db.execSQL(SQL_DELETE_ENTRIES);
                onCreate(db);
            }
            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                onUpgrade(db, oldVersion, newVersion);
            }
        }

    }


}
