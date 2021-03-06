package br.lauriavictor.wmb.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final int VERSION = 1;


    public static final String DB_NAME = "WHERESMYBEER.DB";
    public static final String TB_NAME = "USER";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_PHOTO = "PHOTO";

    public static final String TB_NAME_PLACE = "PLACE";
    public static final String COLUMN_NAME_PLACE = "NAMETEXT";
    public static final String COLUMN_ID_PLACE = "ID";
    public static final String COLUMN_ADDRESS = "ADDRESS";
    public static final String COLUMN_PHONE = "PHONE";
    public static final String COLUMN_RATING = "RATINGFLOAT";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +
                TB_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_PHOTO + " BLOB)";
        db.execSQL(sql);

        String sqlPlace = "CREATE TABLE " +
                TB_NAME_PLACE + " ( " +
                COLUMN_ID_PLACE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_PLACE + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_RATING + " TEXT, " +
                COLUMN_PHONE + " TEXT);";
        db.execSQL(sqlPlace);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_PLACE);
        onCreate(db);
    }
}