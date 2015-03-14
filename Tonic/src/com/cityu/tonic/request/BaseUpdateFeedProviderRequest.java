package com.cityu.tonic.request;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cityu.tonic.contentProvider.AudioFeedProvider;
import com.cityu.tonic.model.ResponseFeed;

public class BaseUpdateFeedProviderRequest {
	Context mContext;
	public BaseUpdateFeedProviderRequest(Context context){
		mContext = context;
	}
	protected void updateContentProvider(ArrayList<ResponseFeed> lists){
		
		for(ResponseFeed feed:lists){
			ContentValues values = new ContentValues();
			values.put(AudioFeedProvider.AUDIO_URL, feed.url);
			values.put(AudioFeedProvider.CREATED_TIME, feed.ct);
			values.put(AudioFeedProvider.FEED_ID, feed.fid);
			values.put(AudioFeedProvider.LIKES, feed.like);
			values.put(AudioFeedProvider.LIKED, String.valueOf(feed.liked));
			values.put(AudioFeedProvider.LOCATION, feed.loc);
			values.put(AudioFeedProvider.TITLE, feed.tit);
			values.put(AudioFeedProvider.UPDATED_TIME, feed.ut);
			values.put(AudioFeedProvider.CREATED_TIME, feed.ct);
			values.put(AudioFeedProvider.USER_ID, feed.uid);
			values.put(AudioFeedProvider.USERNAME, feed.ac);
			
			int result = mContext.getContentResolver().update(Uri.parse(AudioFeedProvider.URL), values, AudioFeedProvider.FEED_ID+"=?", new String[]{feed.fid});
			if(result <= 0){
				mContext.getContentResolver().insert(Uri.parse(AudioFeedProvider.URL), values);
				Log.v("ken", "insert audio feed db success, id: "+feed.fid);
			}else{
				Log.v("ken", "update audio feed db success, id: "+feed.fid);
			}
		}
		
	}
}
