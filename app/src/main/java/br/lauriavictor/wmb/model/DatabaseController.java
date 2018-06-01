package br.lauriavictor.wmb.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseController {

    private SQLiteDatabase mSqLiteDatabase;
    private DatabaseHelper mDataBaseHelper;

    public DatabaseController(Context context) {
        mDataBaseHelper = new DatabaseHelper(context);
    }

    public boolean insertUser(User user) {
        mSqLiteDatabase = mDataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDataBaseHelper.COLUMN_NAME, user.getName());
        contentValues.put(mDataBaseHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(mDataBaseHelper.COLUMN_PASSWORD, user.getPassword());
        contentValues.put(mDataBaseHelper.COLUMN_PHOTO, user.getPhoto());

        mSqLiteDatabase.insert(mDataBaseHelper.TB_NAME, null, contentValues);
        return true;
    }

    public boolean updateUser(User user) {
        mSqLiteDatabase = mDataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDataBaseHelper.COLUMN_NAME, user.getName());
        contentValues.put(mDataBaseHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(mDataBaseHelper.COLUMN_PASSWORD, user.getPassword());
        contentValues.put(mDataBaseHelper.COLUMN_PHOTO, user.getPhoto());

        mSqLiteDatabase.update(mDataBaseHelper.TB_NAME, contentValues, mDataBaseHelper.COLUMN_ID
                + " = ? ", new String[]{Integer.toString(user.getId())});
        return true;
    }

    public Integer removeUser(User user) {
        mSqLiteDatabase = mDataBaseHelper.getWritableDatabase();
        return mSqLiteDatabase.delete(mDataBaseHelper.TB_NAME, mDataBaseHelper.COLUMN_ID + " = ? ",
                new String[]{Integer.toString(user.getId())});
    }

    public User getUser(int id) {
        mSqLiteDatabase = mDataBaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = ? ", mDataBaseHelper.TB_NAME,
                                                                 mDataBaseHelper.COLUMN_ID), new String[]{Integer.toString(id)});
        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                user.setId(cursor.getInt(cursor.getColumnIndex(mDataBaseHelper.COLUMN_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_EMAIL)));
                user.setName(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_PASSWORD)));
                user.setPhoto(cursor.getBlob(cursor.getColumnIndex(mDataBaseHelper.COLUMN_PHOTO)));


            } while (cursor.moveToNext());
        }
        return user;
    }

    public ArrayList<User> listAll() {
        ArrayList<User> users = new ArrayList<>();
        mSqLiteDatabase = mDataBaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery("SELECT * FROM " + mDataBaseHelper.TB_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(mDataBaseHelper.COLUMN_ID)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_EMAIL)));
                user.setName(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_PASSWORD)));
                user.setPhoto(cursor.getBlob(cursor.getColumnIndex(mDataBaseHelper.COLUMN_PHOTO)));

                users.add(user);
            } while (cursor.moveToNext());
        }
        return users;
    }
}
