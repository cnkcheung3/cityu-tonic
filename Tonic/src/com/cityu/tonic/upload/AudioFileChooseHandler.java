package com.cityu.tonic.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AudioFileChooseHandler {
	
	private Activity mActivity;
	public AudioFileChooseHandler(Activity activity){
		this.mActivity = activity;
	};
	
	public void choose(){
		Intent intent_upload = new Intent();
		intent_upload.setType("audio/*");
		intent_upload.setAction(Intent.ACTION_GET_CONTENT);
		mActivity.startActivityForResult(intent_upload,1);
	}
	
	public void onActivityResult(int requestCode,int resultCode,Intent data){
	  if(requestCode == 1){
	    if(resultCode == Activity.RESULT_OK){
	        //the selected audio.
	        Uri uri = data.getData(); 
	        (new AudioFileUploadHandler(mActivity)).uploadAudioFile(uri);
	        Log.v("ken", "selected audio url: "+ uri);
	    }
	  }
	}
}
