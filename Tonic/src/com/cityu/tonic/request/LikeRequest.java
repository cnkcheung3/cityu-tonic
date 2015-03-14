package com.cityu.tonic.request;

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
import com.cityu.tonic.model.HttpResponse;
import com.cityu.tonic.request.GetUserProfileRequest.RequestBody;
import com.cityu.tonic.request.GetUserProfileRequest.ResponseUserProfile;
import com.cityu.tonic.utils.VolleySingleton;
import com.google.gson.Gson;

public class LikeRequest{
	
	Context mContext;
	RequestBody requestBody;
	public LikeRequest(String fid, Context context)
	{
		requestBody = new RequestBody();
		requestBody.id = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.PREF_USER_ID, "");
		requestBody.token = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.PREF_TOKEN, "");
		requestBody.fid = fid;
		mContext = context;
	}
	
	/**
	 * @param like
	 * true: like the feed
	 * false: unlike the feed
	 */
	public void makeRequest(boolean like){
		final String requestJson = (new Gson()).toJson(requestBody);
		Log.v("ken", "request Json: "+requestJson);
		
		String url;
		if(like)
			url = mContext.getString(R.string.development_url)+"index.php?type=like";
		else
			url = mContext.getString(R.string.development_url)+"index.php?type=unlike";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.v("ken", "like/unlike response: "+response);
				
				ResponseBody responseBody = (new Gson()).fromJson(response, ResponseBody.class);
				onGetResponse(responseBody);
				//ResponseUserProfile responseUserProfile = (new Gson()).fromJson(response, ResponseUserProfile.class);
				//updateContentProvider(responseUserProfile);
				//onGetUserProfile(responseUserProfile);
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
		String fid;
	}
	
	public class ResponseBody extends HttpResponse{
		public String like;
	}
	
	protected void onGetResponse(ResponseBody responseBody){};
}
