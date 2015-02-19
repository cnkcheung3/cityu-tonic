package com.cityu.tonic.upload;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cityu.tonic.LoginActivity;
import com.cityu.tonic.R;
import com.cityu.tonic.utils.VolleySingleton;
import com.google.gson.Gson;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

public class ImageUploadHandler {
	
	private String imgPath;
	private Context mContext;
	private Map<String, String> params = new HashMap<String, String>();
	
	public ImageUploadHandler(Context context, String imgPath){
		this.imgPath = imgPath;
		this.mContext = context;
	}
	
	public void uploadImage() {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            //prgDialog.setMessage("Converting Image to Binary Data");
            //prgDialog.show();
            // Convert image to String using Base64
            new EncodeImageToStringTask().execute();
        // When Image is not selected from Gallery
        } else {
            //Toast.makeText(
            //        getApplicationContext(),
            //        "You must select image from gallery before you try to upload",
            //        Toast.LENGTH_LONG).show();
        }
    }
	
	private class EncodeImageToStringTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

		};

		@Override
		protected String doInBackground(Void... params) {
			BitmapFactory.Options options = null;
			options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// Must compress the Image to reduce image size to make upload easy
			bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
			byte[] byte_arr = stream.toByteArray();
			// Encode Image to String
			String encodedString = Base64.encodeToString(byte_arr, 0);
			return encodedString;
		}

		@Override
		protected void onPostExecute(String encodedString) {
			params.put("image", encodedString);
			params.put("id", PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.PREF_USER_ID, ""));
			params.put("token", PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.PREF_TOKEN, ""));
			Log.v("ken", "id: "+ PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.PREF_USER_ID, ""));
			Log.v("ken", "token: " + PreferenceManager.getDefaultSharedPreferences(mContext).getString(LoginActivity.PREF_TOKEN, ""));
			
			makeUploadRequest(params);
		}
	}
	
	private void makeUploadRequest(final Map<String, String> params){
		
		//RequestQueue queue = Volley.newRequestQueue(mContext);
		String url = mContext.getString(R.string.development_url)+"index.php?type=uploadImage";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.v("ken", "Upload image response: "+response);
				if(mOnUploadSuccessListener != null)
					mOnUploadSuccessListener.onUploadSuccess(response);
			}
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	Log.v("ken", "error response after upload: ");
		    }
		}){

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}


		};
		
		// Add the request to the RequestQueue.
		VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
	
	}
	
	public interface OnUploadSuccessListener{
		public void onUploadSuccess(String response);
	}
	private OnUploadSuccessListener mOnUploadSuccessListener;
	public void setOnUploadSuccessListener(OnUploadSuccessListener listener){
		this.mOnUploadSuccessListener = listener;
	}
}

