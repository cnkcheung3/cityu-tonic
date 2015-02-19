package com.cityu.tonic.request;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cityu.tonic.R;
import com.cityu.tonic.utils.VolleySingleton;
import com.google.gson.Gson;

public class LoginRequest {
	
	String body;

	public void makeRequest(Crediential crediential, Context context){
		
		body = (new Gson()).toJson(crediential);
		
		//RequestQueue queue = Volley.newRequestQueue(context);
		String url = context.getString(R.string.development_url)+"index.php?type=login";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.v("ken", "login response: "+response);
				ResponseBody responseBody = (new Gson()).fromJson(response, ResponseBody.class);
				if(responseBody.status.equals("200"))
					onLoginSuccess(responseBody.user.id, responseBody.user.token);
				else
					onLoginFail(response);
			}
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    }
		}){

			@Override
			public byte[] getBody() throws AuthFailureError {
				// Todo Auto-generated method stub
				return body.getBytes();
			}

		};
		
		// Add the request to the RequestQueue.
		//queue.add(stringRequest);
		VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
	}
	
	protected void onLoginSuccess(String id, String token){}
	protected void onLoginFail(String response){}
	
	public static class Crediential{
		public String ac;
		public String pw;
	}
	
	private class ResponseBody{
		User user; 
		public String msg;
		public String status;
	}
	private class User{
		public String id;
		public String token;
	}
}
