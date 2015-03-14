package com.cityu.tonic.cache;

import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class CacheImageHandler {
	
	Context mContext;
	public CacheImageHandler(Context context){
		mContext = context;
	}
	
	public void updateCachedUserImage(String id, Bitmap bitmap)
	{
		
		String filename = id+".png";
		
		FileOutputStream out;

		try {
			out = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		  	out.flush();
	        out.close();
	        bitmap.recycle();
		} catch (Exception e) {
		  e.printStackTrace();
		}
		
	}
}
