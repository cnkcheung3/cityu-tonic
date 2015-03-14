package com.cityu.tonic.request;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cityu.tonic.LoginActivity;
import com.cityu.tonic.R;
import com.cityu.tonic.request.GetUserProfileRequest.RequestBody;
import com.cityu.tonic.request.GetUserProfileRequest.ResponseUserProfile;
import com.cityu.tonic.utils.VolleySingleton;
import com.google.gson.Gson;

public class GetUserListRequest {
	
	public final static int TYPE_GET_FOLLOWER = 0;
	public final static int TYPE_GET_FOLLOWING = 1;
	public final static int TYPE_GET_LIKES = 2;
	private int mType;
	Context mContext;
	RequestBody requestBody;
	
	/**
	 * 
	 * @param sid - sid should be either user_id or feed_id
	 * @param type 
	 * @param context
	 */
	public GetUserListRequest(String sid, int type, Context context)
	{
		requestBody = new RequestBody();
		requestBody.id = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.PREF_USER_ID, "");
		requestBody.token = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.PREF_TOKEN, "");
		requestBody.sid = sid;
		mType = type;
		mContext = context;
	}
	
	public void makeRequest(){
		final String requestJson = (new Gson()).toJson(requestBody);
		Log.v("ken", "request Json: "+requestJson);
		
		String url = null;
		if(mType == TYPE_GET_FOLLOWER)
			url = mContext.getString(R.string.development_url)+"index.php?type=getFollower";
		else if(mType == TYPE_GET_FOLLOWING)
			url = mContext.getString(R.string.development_url)+"index.php?type=getFollowing";
		else if(mType == TYPE_GET_LIKES)
			url = mContext.getString(R.string.development_url)+"index.php?type=getLikes";
		
		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.v("ken", "getUserLists response: "+response);
				ResponseUserItem[] responseUserItem = (new Gson()).fromJson(response, ResponseUserItem[].class);
				onGetUserLists(new ArrayList<ResponseUserItem>(Arrays.asList(responseUserItem)));
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
		String id;
		String token;
		String sid;
	}
	
	public class ResponseUserItem{
		public String uid;
		public String ac;
		public String pic;
		public String des;
		    
	}
	
	protected void onGetUserLists(ArrayList<ResponseUserItem> lists){}
	
	
}
