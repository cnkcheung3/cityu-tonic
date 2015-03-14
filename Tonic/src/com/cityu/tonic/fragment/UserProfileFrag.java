package com.cityu.tonic.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.cityu.tonic.LoginActivity;
import com.cityu.tonic.R;
import com.cityu.tonic.R.layout;
import com.cityu.tonic.adapter.BaseFeedAdapter;
import com.cityu.tonic.adapter.FeedAdapter;
import com.cityu.tonic.contentProvider.AudioFeedProvider;
import com.cityu.tonic.contentProvider.UserProfileProvider;
import com.cityu.tonic.model.HttpResponse;
import com.cityu.tonic.model.ResponseFeed;
import com.cityu.tonic.request.FollowRequest;
import com.cityu.tonic.request.GetFollowingFeedRequest;
import com.cityu.tonic.request.FollowRequest.ResponseBody;
import com.cityu.tonic.request.GetMyFeedRequest;
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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserProfileFrag extends Fragment{
	
	List<ResponseFeed> mFeedItems;
	NetworkCircleImageView user_icon_iv;
	TextView audio_count_tv;
	TextView follower_count_tv;
	TextView following_count_tv;
	TextView user_name_tv;
	TextView user_des_tv;
	TextView relation_tv;
	ImageChooseHandler imageChooser;
	ImageUploadHandler imageUploadHandler;
	ImageLoader mImageLoader;
	String uid;
	BaseFeedAdapter mAdapter;
	Activity mActivity;
	ListView mlistView;
	public final static String KEY_USER_ID = "key_user_id";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.frag_user_profile, container, false);
        mActivity = this.getActivity();
        try{
        	uid = getArguments().getString(KEY_USER_ID);
        }catch(Exception e){
        	e.printStackTrace();
        }
        audio_count_tv = (TextView) rootView.findViewById(R.id.audio_num_tv);
        follower_count_tv = (TextView) rootView.findViewById(R.id.follower_num_tv);
        following_count_tv = (TextView) rootView.findViewById(R.id.following_num_tv);
        user_name_tv = (TextView) rootView.findViewById(R.id.user_name_tv);
        relation_tv = (TextView) rootView.findViewById(R.id.relation_tv);
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
        
        uid = (uid == null? PreferenceManager.getDefaultSharedPreferences(UserProfileFrag.this.getActivity()).getString(LoginActivity.PREF_USER_ID, "") : uid);
       
        retrieveUserProfileFromContentProvider(uid);
        
        GetUserProfileRequest request = new GetUserProfileRequest(
        		uid!=null?uid:PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(LoginActivity.PREF_USER_ID, ""),
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
        
        mlistView = (ListView) rootView.findViewById(R.id.listView);
    	mFeedItems = new ArrayList<ResponseFeed>();
    	mAdapter = new BaseFeedAdapter(mActivity, mActivity, 0, 0, mFeedItems);
    	mlistView.setAdapter(mAdapter);
        
        retrieveMyFeedFromContentProvider(uid);
        
        GetMyFeedRequest feedRequest = new GetMyFeedRequest(this.getActivity(), uid){

			@Override
			protected void onGetFeed(ArrayList<ResponseFeed> responseFeeds) {
				mFeedItems.clear();
				for(ResponseFeed feed: responseFeeds)
					mFeedItems.add(feed);
				
				mAdapter.notifyDataSetChanged();
			}
    		
    	};
    	feedRequest.makeRequest();
        
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
		
		updateUI(userProfile.name, 
				userProfile.audio, 
				userProfile.follower, 
				userProfile.following, 
				userProfile.des, 
				userProfile.pic_url, 
				userProfile.relation);
	}
	
	private void retrieveUserProfileFromContentProvider(String user_id)
	{
		ContentResolver cr = this.getActivity().getContentResolver();
		Cursor cursor = cr.query(Uri.parse(UserProfileProvider.URL), null, UserProfileProvider.USER_ID+"=?", new String[]{user_id}, null);
		String username, audio_count, follower_count, following_count, des, pic_url, relation, relation_txt;
		
		while(cursor.moveToNext())
		{
			username = cursor.getString(cursor.getColumnIndex(UserProfileProvider.USERNAME));
			audio_count = cursor.getString(cursor.getColumnIndex(UserProfileProvider.AUDIO_COUNT));
			follower_count = cursor.getString(cursor.getColumnIndex(UserProfileProvider.FOLLOWER_COUNT));
			following_count = cursor.getString(cursor.getColumnIndex(UserProfileProvider.FOLLOWING_COUNT));
			des = cursor.getString(cursor.getColumnIndex(UserProfileProvider.DES));
			pic_url = cursor.getString(cursor.getColumnIndex(UserProfileProvider.PROPIC_URL));
			relation = cursor.getString(cursor.getColumnIndex(UserProfileProvider.RELATION));
			
			updateUI(username, 
					audio_count, 
					follower_count, 
					following_count, 
					des, 
					pic_url, 
					relation);
		}
	}
	
	private void retrieveMyFeedFromContentProvider(String uid)
	{
		ContentResolver cr = this.getActivity().getContentResolver();
		Cursor cursor = cr.query(Uri.parse(AudioFeedProvider.URL), null, AudioFeedProvider.USER_ID+"=?", new String[]{uid}, null);
		mFeedItems.clear();
		while(cursor.moveToNext())
		{
			ResponseFeed feed = new ResponseFeed();
			feed.ac = cursor.getString(cursor.getColumnIndex(AudioFeedProvider.USERNAME));
			feed.ct = cursor.getString(cursor.getColumnIndex(AudioFeedProvider.CREATED_TIME));
			feed.fid = cursor.getString(cursor.getColumnIndex(AudioFeedProvider.FEED_ID));
			feed.like = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AudioFeedProvider.LIKES)));
			feed.liked = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(AudioFeedProvider.LIKED)));
			feed.loc = cursor.getString(cursor.getColumnIndex(AudioFeedProvider.LOCATION));
			feed.uid = cursor.getString(cursor.getColumnIndex(AudioFeedProvider.USER_ID));
			feed.tit = cursor.getString(cursor.getColumnIndex(AudioFeedProvider.TITLE));
			feed.url = cursor.getString(cursor.getColumnIndex(AudioFeedProvider.AUDIO_URL));
			feed.ut = cursor.getString(cursor.getColumnIndex(AudioFeedProvider.UPDATED_TIME));
			
			mFeedItems.add(feed);
		}
		mAdapter.notifyDataSetChanged();
	}
	
	private void updateUI(String username, String audio_count, String follower_count, String following_count, String des, String pic_url, String relation){
		
		pic_url = getResources().getString(R.string.development_url) + "user_pic/" + pic_url;
		
		this.audio_count_tv.setText(audio_count);
		this.follower_count_tv.setText(follower_count);
		this.following_count_tv.setText(following_count);
		this.user_name_tv.setText(username);
		this.user_des_tv.setText(des);
		this.user_icon_iv.setImageUrl(pic_url, mImageLoader);
		this.relation_tv.setOnClickListener(new ClickRelationAdapter());
		updateRelation(relation);
		
	}
	
	private void updateRelation(String relation){
		String relation_txt;
		if(relation.equals("me"))
			relation_txt = getResources().getString(R.string.user_profile_relation_me);
		else if(relation.equals("following"))
			relation_txt = getResources().getString(R.string.user_profile_relation_follow);
		else if(relation.equals("none"))
			relation_txt = getResources().getString(R.string.user_profile_relation_none);
		else if(relation.equals("request"))
			relation_txt = getResources().getString(R.string.user_profile_relation_request);
		else
			relation_txt = getResources().getString(R.string.user_profile_relation_default);
		
		this.relation_tv.setText(relation_txt);
		this.relation_tv.setTag(relation);
		
	}
	
	public class ClickRelationAdapter implements OnClickListener{

		@Override
		public void onClick(View v) {
			String rel = (String)v.getTag();
			if(rel.equals("me")){
				
			}else if(rel.equals("none")){
				// follow request
				new FollowRequest(uid, UserProfileFrag.this.getActivity()){
					@Override
					protected void onGetResponse(ResponseBody responseBody) {
						updateRelation(responseBody.relation);
						follower_count_tv.setText(responseBody.follower);
					}
				}.makeRequest(true);
			}else{
				// unfollow request
				new FollowRequest(uid, UserProfileFrag.this.getActivity()){
					@Override
					protected void onGetResponse(ResponseBody responseBody) {
						updateRelation(responseBody.relation);
						follower_count_tv.setText(responseBody.follower);
					}
				}.makeRequest(false);
			}
		}
		
	}
}