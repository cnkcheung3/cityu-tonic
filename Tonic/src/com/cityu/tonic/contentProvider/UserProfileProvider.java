package com.cityu.tonic.contentProvider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class UserProfileProvider extends ContentProvider{

	public static final String PROVIDER_NAME = "com.cityu.tonic.contentProvider.UserProfile";
	public static final String URL = "content://" + PROVIDER_NAME + "/friends";
	public static final Uri CONTENT_URI = Uri.parse(URL);

	public static final String ID = "id";
	public static final String USER_ID = "user_id";
	public static final String USERNAME = "username";
	public static final String AUDIO_COUNT = "audio_count";
	public static final String FOLLOWING_COUNT = "following_count";
	public static final String FOLLOWER_COUNT = "follower_count";
	public static final String PROPIC_URL = "propic_url";
	public static final String DES = "des";
	public static final String RELATION = "relation";

	private SQLiteDatabase database;
	public static final String DATABASE_NAME = "UserProfile";
	public static final String TABLE_NAME = "userProfile";
	public static final int DATABASE_VERSION = 1;
	public static final String CREATE_TABLE =
			" CREATE TABLE " + TABLE_NAME +
			" (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			" user_id TEXT NOT NULL, " +
			" username TEXT NOT NULL, " +
			" audio_count TEXT NOT NULL, " +
			" following_count TEXT NOT NULL, " +
			" follower_count TEXT NOT NULL, " +
			" propic_url TEXT, " +
			" relation TEXT, " +
			" des TEXT); ";
	
	DBHelper dbHelper;
	private static HashMap<String, String> BirthMap;

	private static class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
		}
		@Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(DBHelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ". Old data will be destroyed");
			db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
			onCreate(db);
		}
	}

	
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		count = database.delete(TABLE_NAME, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;

	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long row = database.insert(TABLE_NAME, "", values);
		if(row > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Fail to add a new record into " + uri);

	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		if(database == null)
			return false;
		else
			return true;   
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TABLE_NAME);
		queryBuilder.setProjectionMap(BirthMap);
		Cursor cursor = queryBuilder.query(database, projection, selection,
				selectionArgs, null, null, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		 int count = 0;
		 count = database.update(TABLE_NAME, values, selection, selectionArgs);
		 getContext().getContentResolver().notifyChange(uri, null);
		 return count;
	}

}
