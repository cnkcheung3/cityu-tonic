package com.cityu.tonic.fragment;

import com.android.volley.toolbox.ImageLoader;
import com.cityu.tonic.LoginActivity;
import com.cityu.tonic.R;
import com.cityu.tonic.R.layout;
import com.cityu.tonic.model.HttpResponse;
import com.cityu.tonic.request.GetUserProfileRequest;
import com.cityu.tonic.request.GetUserProfileRequest.ResponseUserProfile;
import com.cityu.tonic.upload.ImageChooseHandler;
import com.cityu.tonic.upload.ImageUploadHandler;
import com.cityu.tonic.upload.ImageUploadHandler.OnUploadSuccessListener;
import com.cityu.tonic.utils.NetworkCircleImageView;
import com.cityu.tonic.utils.VolleySingleton;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileFrag extends Fragment {
	
	NetworkCircleImageView user_icon_iv;
	TextView audio_count_tv;
	TextView follower_count_tv;
	TextView following_count_tv;
	TextView user_name_tv;
	TextView user_des_tv;
	ImageChooseHandler imageChooser;
	ImageUploadHandler imageUploadHandler;
	ImageLoader mImageLoader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.frag_user_profile, container, false);
        
        audio_count_tv = (TextView) rootView.findViewById(R.id.audio_num_tv);
        follower_count_tv = (TextView) rootView.findViewById(R.id.follower_num_tv);
        following_count_tv = (TextView) rootView.findViewById(R.id.following_num_tv);
        user_name_tv = (TextView) rootView.findViewById(R.id.user_name_tv);
        user_des_tv =  (TextView) rootView.findViewById(R.id.user_desc_tv);
        user_icon_iv = ((NetworkCircleImageView)rootView.findViewById(R.id.user_icon_iv));
        user_icon_iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseAndUploadImage();
			}
		});
        mImageLoader = new ImageLoader(VolleySingleton.getInstance(this.getActivity()).getRequestQueue(), 
        		new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
        
        GetUserProfileRequest request = new GetUserProfileRequest(
        		PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(LoginActivity.PREF_USER_ID, ""),
        		this.getActivity()
        		)
        {

			@Override
			protected void onGetUserProfile(ResponseUserProfile userProfile) {
				// TODO Auto-generated method stub
				super.onGetUserProfile(userProfile);
				try{
					handleUserProfile(userProfile);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
        	
        };
        
        request.makeRequest();
        
        
        
        return rootView;
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		Log.v("ken", "requestCode: "+requestCode);
		imageChooser.onActivityResult(requestCode, resultCode, data);
	}
    
	private void chooseAndUploadImage(){
		imageChooser = new ImageChooseHandler(UserProfileFrag.this){
			@Override
			protected void onGetImagePath(Context context,
					String imgPath) {
				
				imageUploadHandler = new ImageUploadHandler(context, imgPath);
				imageUploadHandler.setOnUploadSuccessListener(new OnUploadSuccessListener() {
					
					@Override
					public void onUploadSuccess(String response) {
						ImagePathHttpResponse image = (new Gson()).fromJson(response, ImagePathHttpResponse.class);
						Log.v("ken", "get image url: "+image.url);
						String full_path = getResources().getString(R.string.development_url) + "user_pic/" + image.url;
						user_icon_iv.setImageUrl(full_path, mImageLoader);
					}
				});
				imageUploadHandler.uploadImage();
			}
			
		};
		imageChooser.loadImagefromGallery();
	}
	
	private class ImagePathHttpResponse extends HttpResponse{
		public String url;
	}
	
	private void handleUserProfile(ResponseUserProfile userProfile) throws Exception{
		String full_path = getResources().getString(R.string.development_url) + "user_pic/" + userProfile.pic_url;
		this.user_icon_iv.setImageUrl(full_path, mImageLoader);
		this.audio_count_tv.setText(userProfile.audio);
		this.follower_count_tv.setText(userProfile.follower);
		this.following_count_tv.setText(userProfile.following);
		this.user_name_tv.setText(userProfile.name);
		this.user_des_tv.setText(userProfile.des);
	}
    
}