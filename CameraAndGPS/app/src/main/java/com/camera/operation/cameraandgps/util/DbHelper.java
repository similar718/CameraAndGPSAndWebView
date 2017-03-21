package com.camera.operation.cameraandgps.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.camera.operation.cameraandgps.base.LocalEntity;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

	private static final String TABLE_NAME = "cameragps"; // database name
	private static final int TABLE_VERSION = 1;// database version
	private SQLiteDatabase db = null;
	private Cursor cursor = null;

	public DbHelper(Context context) {
		super(context, Constants.LOCAL_DBNAME, null, TABLE_VERSION);
	}

	// create table
	@Override 
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL("create table "
				+ TABLE_NAME
				+ " (_id integer primary key autoincrement,"
				+ "mControlUrl varchar(20),mMessageUrl vachar(20),"
				+ "mMeUrl varchar(20))");
	}

	// add   test
	public void insert(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		Log.e("ooooooooooo","values" + values.get("mControlUrl") + values.get("mMessageUrl"));
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	// add   test
	public void insert1(LocalEntity entity) {
		ContentValues values = new ContentValues();
		values.put("mControlUrl", entity.mControlUrl);
		values.put("mMessageUrl", entity.mMessageUrl);
		values.put("mMeUrl", entity.mMeUrl);
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	// delete a line
	public void delete(String imageName) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "mControlUrl=?", new String[] { String.valueOf(imageName) });
	}
	// according to imageName query
	public boolean query(String imageName) {
		boolean flag = false;
		SQLiteDatabase db = getWritableDatabase();
		cursor = db.query(TABLE_NAME, null, "mControlUrl=?",
				new String[] { String.valueOf(imageName) }, null, null, null);
		if (cursor.moveToFirst()) {
			flag = true;
		}
		return flag;
	}

	public List<LocalEntity> query() {
		List<LocalEntity> list = new ArrayList<LocalEntity>();
		SQLiteDatabase db = getWritableDatabase();
		cursor = db.query(TABLE_NAME, null, null, null, "_id", null, null);
		if (cursor.moveToFirst()) {
			do {
				LocalEntity info = new LocalEntity();
				info.mControlUrl = cursor.getString(cursor.getColumnIndex("mControlUrl"));
				info.mMessageUrl = cursor.getString(cursor.getColumnIndex("mMessageUrl"));
				info.mMeUrl = cursor.getString(cursor.getColumnIndex("mMeUrl"));
				list.add(info);
			} while (cursor.moveToNext());
		}
		return list;
	}

	// operate the database by the SQL statement
	public void handleBySql(String sql) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
	}

	// close data base
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
