package br.lauriavictor.wmb.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.location.places.Place;

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

    /**
     * Métodos referentes a PlaceInfo
     */

    public boolean insertPlace(PlaceInfo placeInfo) {
        mSqLiteDatabase = mDataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDataBaseHelper.COLUMN_NAME_PLACE, placeInfo.getName());
        contentValues.put(mDataBaseHelper.COLUMN_ADDRESS, placeInfo.getAddress());
        contentValues.put(mDataBaseHelper.COLUMN_PHONE, placeInfo.getPhoneNumber());
        contentValues.put(mDataBaseHelper.COLUMN_RATING, placeInfo.getRating());

        mSqLiteDatabase.insert(mDataBaseHelper.TB_NAME_PLACE, null, contentValues);
        return true;
    }

    public ArrayList<PlaceInfo> listAllPlaces() {
        ArrayList<PlaceInfo> placeInfos = new ArrayList<>();
        mSqLiteDatabase = mDataBaseHelper.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.rawQuery("SELECT * FROM " + mDataBaseHelper.TB_NAME_PLACE, null);
        if (cursor.moveToFirst()) {
            do {
                PlaceInfo placeInfo = new PlaceInfo();
                placeInfo.setName(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_NAME_PLACE)));
                placeInfo.setAddress(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_ADDRESS)));
                placeInfo.setPhoneNumber(cursor.getString(cursor.getColumnIndex(mDataBaseHelper.COLUMN_PHONE)));
                placeInfo.setRating(cursor.getFloat(cursor.getColumnIndex(mDataBaseHelper.COLUMN_RATING)));

                placeInfos.add(placeInfo);
            } while (cursor.moveToNext());
        }
        return placeInfos;
    }
}
