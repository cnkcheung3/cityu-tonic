package com.cityu.tonic.request;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cityu.tonic.LoginActivity;
import com.cityu.tonic.R;
import com.cityu.tonic.contentProvider.AudioFeedProvider;
import com.cityu.tonic.contentProvider.UserProfileProvider;
import com.cityu.tonic.model.ResponseFeed;
import com.cityu.tonic.request.GetUserProfileRequest.ResponseUserProfile;
import com.cityu.tonic.utils.VolleySingleton;
import com.google.gson.Gson;

public class GetMyFeedRequest extends BaseUpdateFeedProviderRequest{
	
	Context mContext;
	RequestBody requestBody;
	
	public GetMyFeedRequest(Context context, String uid)
	{
		super(context);
		requestBody = new RequestBody();
		requestBody.id = uid;
		requestBody.token = PreferenceManager.getDefaultSharedPreferences(context).getString(LoginActivity.PREF_TOKEN, "");
		mContext = context;
	}
	
	public void makeRequest()
	{
		final String requestJson = (new Gson()).toJson(requestBody);
		Log.v("ken", "request Json: "+requestJson);
		
		String url = mContext.getString(R.string.development_url)+"index.php?type=getMyAudioFeed";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.v("ken", "getMyAudioFeed response: "+response);
				ResponseFeed[] responseFeeds = (new Gson()).fromJson(response, ResponseFeed[].class);
				
				ArrayList<ResponseFeed> lists = new ArrayList<ResponseFeed>(Arrays.asList(responseFeeds));
				
				onGetFeed(lists);
				
				updateContentProvider(lists);
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
	}
	
	
}
