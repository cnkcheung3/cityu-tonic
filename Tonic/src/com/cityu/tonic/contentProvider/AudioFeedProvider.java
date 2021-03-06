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

public class AudioFeedProvider extends ContentProvider {

	public static final String PROVIDER_NAME = "com.cityu.tonic.contentProvider.Audio";
	public static final String URL = "content://" + PROVIDER_NAME + "/friends";
	public static final Uri CONTENT_URI = Uri.parse(URL);

	public static final String ID = "id";
	public static final String FEED_ID = "feed_id";
	public static final String USER_ID = "user_id";
	public static final String USERNAME = "username";
	public static final String UPDATED_TIME = "updated_time";
	public static final String CREATED_TIME = "created_time";
	public static final String AUDIO_URL = "audio_url";
	public static final String LOCATION = "location";
	public static final String TITLE = "title";
	public static final String LIKES = "likes";
	public static final String LIKED = "liked";
	public static final String IS_PLAYLIST = "is_playlist";

	private SQLiteDatabase database;
	static final String DATABASE_NAME = "AudioFeed";
	static final String TABLE_NAME = "Feed";
    static final int DATABASE_VERSION = 1;
	static final String CREATE_TABLE =
			" CREATE TABLE " + TABLE_NAME +
			" (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			" feed_id TEXT NOT NULL, " +
			" user_id TEXT NOT NULL, " +
			" username TEXT NOT NULL, " +
			" updated_time TEXT NOT NULL, " +
			" created_time TEXT NOT NULL, " +
			" audio_url TEXT NOT NULL, " +
			" location TEXT, " +
			" likes TEXT, " +
			" liked TEXT, " +
			" title TEXT, " +
			" is_playlist TEXT);";
	
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
