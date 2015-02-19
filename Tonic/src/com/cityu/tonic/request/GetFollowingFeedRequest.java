package com.cityu.tonic.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.cityu.tonic.utils.VolleySingleton;
import com.google.gson.Gson;

public class GetFollowingFeedRequest {
	
	Context mContext;
	RequestBody requestBody;
	
	public GetFollowingFeedRequest(Context context)
	{
		requestBody = new RequestBody();
		requestBody.id = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.PREF_USER_ID, "");
		requestBody.token = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.PREF_TOKEN, "");
		mContext = context;
	}
	
	public GetFollowingFeedRequest(Context context, String time)
	{
		this(context);
		requestBody.time = time;
	}
	
	public void makeRequest()
	{
		final String requestJson = (new Gson()).toJson(requestBody);
		Log.v("ken", "request Json: "+requestJson);
		
		String url = mContext.getString(R.string.development_url)+"index.php?type=getAudioFeed";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.v("ken", "getAudioFeed response: "+response);
				ResponseFeed[] responseFeeds = (new Gson()).fromJson(response, ResponseFeed[].class);
				
				ArrayList<ResponseFeed> lists = new ArrayList<ResponseFeed>(Arrays.asList(responseFeeds));
				
				onGetFeed(lists);
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
	
	protected void onGetFeed(ArrayList<ResponseFeed> responseFeeds){}
	
	public class RequestBody{
		String id;
		String token;
		String time;
		ArrayList<Update> update = new ArrayList<Update>();
		public class Update{
			public String fid;
			public String time;
		}
	}
	
	public class ResponseFeed{
		public String fid;
		public String uid;
		public String ac;
		public String url;
		public String tit;
		public String loc;
		public String ct;
		public String ut;
		public String img;
		public ArrayList<Like> like = new ArrayList<Like>() ;
		public class Like{
			public String uid;
			public String ac;
		}
	}
	
}
