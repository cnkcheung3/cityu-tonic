package com.cityu.tonic.request;

import java.util.ArrayList;
import java.util.Arrays;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cityu.tonic.LoginActivity;
import com.cityu.tonic.R;
import com.cityu.tonic.contentProvider.UserProfileProvider;
import com.cityu.tonic.utils.VolleySingleton;
import com.google.gson.Gson;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;


public class GetUserProfileRequest {
	
	Context mContext;
	RequestBody requestBody;
	public GetUserProfileRequest(String uid, Context context)
	{
		requestBody = new RequestBody();
		requestBody.mid = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.PREF_USER_ID, "");
		requestBody.uid = uid;
		mContext = context;
	}
	
	public void makeRequest(){
		final String requestJson = (new Gson()).toJson(requestBody);
		Log.v("ken", "request Json: "+requestJson);
		
		String url = mContext.getString(R.string.development_url)+"index.php?type=getUserProfile";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.v("ken", "getUserProfile response: "+response);
				ResponseUserProfile responseUserProfile = (new Gson()).fromJson(response, ResponseUserProfile.class);
				updateContentProvider(responseUserProfile);
				onGetUserProfile(responseUserProfile);
			}
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    }
		}){

			@Override
			public byte[] getBody() throws AuthFailureError {
				// Todo Auto-generated method stub
				return requestJson.getBytes();
			}

		};
		
		// Add the request to the RequestQueue.
		//queue.add(stringRequest);
		VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
	}
	
	public class RequestBody{
		String mid;
		String uid;
	}
	
	public class ResponseUserProfile{
		public String id;
		public String des;
		public String pic_url;
		public String follower;
		public String following;
		public String audio;
		public String relation;
		public String name;
		    
	}
	
	private void updateContentProvider(ResponseUserProfile user){
		
		ContentValues values = new ContentValues();
		values.put(UserProfileProvider.USER_ID, user.id);
		values.put(UserProfileProvider.AUDIO_COUNT, user.audio);
		values.put(UserProfileProvider.USERNAME, user.name);
		values.put(UserProfileProvider.FOLLOWER_COUNT, user.follower);
		values.put(UserProfileProvider.FOLLOWING_COUNT, user.following);
		values.put(UserProfileProvider.DES, user.des);
		values.put(UserProfileProvider.RELATION, user.relation);
		values.put(UserProfileProvider.PROPIC_URL, user.pic_url);
	
		int result = mContext.getContentResolver().update(Uri.parse(UserProfileProvider.URL), values, UserProfileProvider.USER_ID+"=?", new String[]{user.id});
		if(result <= 0){
			mContext.getContentResolver().insert(Uri.parse(UserProfileProvider.URL), values);
			Log.v("ken", "insert user profile db success, id: "+user.id);
		}else{
			Log.v("ken", "update user profile db success, id: "+user.id);
		}
		
		
	}
	
	protected void onGetUserProfile(ResponseUserProfile userProfile){};
}
