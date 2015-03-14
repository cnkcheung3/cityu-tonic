package com.cityu.tonic.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.cityu.tonic.R;
import com.cityu.tonic.fragment.UserListFrag;
import com.cityu.tonic.fragment.UserProfileFrag;
import com.cityu.tonic.model.ResponseFeed;
import com.cityu.tonic.request.GetUserListRequest;
import com.cityu.tonic.request.LikeRequest;
import com.cityu.tonic.request.LikeRequest.ResponseBody;
import com.cityu.tonic.utils.NetworkCircleImageView;
import com.cityu.tonic.utils.VolleySingleton;

public class BaseFeedAdapter extends ArrayAdapter<ResponseFeed>{
	protected Activity mActivity;
	protected VideoView myVideoView;
	protected NetworkCircleImageView user_icon_iv;
	protected TextView username_tv;
	protected TextView time_tv;
	protected TextView like_tv;
	protected TextView title_username_tv;
	protected TextView title_content_tv;
	protected ImageView like_iv;
	public BaseFeedAdapter(Activity activity, Context context, int resource,
			int textViewResourceId, List<ResponseFeed> objects) {
		super(context, resource, textViewResourceId, objects);
		mActivity = activity;
		initImageLoader();
	}
	
	ImageLoader mImageLoader;
	private void initImageLoader(){
		mImageLoader = new ImageLoader(VolleySingleton.getInstance(mActivity).getRequestQueue(), 
        		new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
	    if (v == null) {
	        LayoutInflater vi = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        v = vi.inflate(R.layout.feed_list_item, null);
	    }
	    
	    ResponseFeed feed = this.getItem(position);
	    username_tv = (TextView)v.findViewById(R.id.username_tv);
	    time_tv = (TextView)v.findViewById(R.id.time_tv);
	    like_tv = (TextView)v.findViewById(R.id.like_num_tv);
	    user_icon_iv = (NetworkCircleImageView)v.findViewById(R.id.user_icon_iv);
    	title_username_tv = (TextView) v.findViewById(R.id.title_username_tv);
    	title_content_tv = (TextView) v.findViewById(R.id.title_content_tv);
    	like_iv = (ImageView) v.findViewById(R.id.like_iv);
    	
    	final String feed_id = feed.fid;
	    
	    username_tv.setText(feed.ac);
	    time_tv.setText(feed.ct);
	    like_tv.setText(String.format("%s likes", feed.like));
	    title_username_tv.setText(feed.ac);
	    title_content_tv.setText(feed.tit);
	    String full_path = mActivity.getString(R.string.development_url) + "user_pic/" + feed.img;
	    user_icon_iv.setImageUrl(full_path, mImageLoader);
	    like_iv.setImageResource(feed.liked? R.drawable.ic_liked:R.drawable.ic_like);
	    like_iv.setTag(feed.liked? R.drawable.ic_liked:R.drawable.ic_like);
	    
	    final TextView like_tv_clone = like_tv; 
	    like_iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				final ImageView iv = (ImageView) view;
				switch((Integer)iv.getTag()){
				
				case R.drawable.ic_liked:
					new LikeRequest(feed_id, mActivity){
						@Override
						protected void onGetResponse(ResponseBody body) {
							if(body.status.equals("200")){
								iv.setImageResource(R.drawable.ic_like);
								iv.setTag(R.drawable.ic_like);
								like_tv_clone.setText(String.format("%s likes",body.like));
							}
						}
					}.makeRequest(false);
					break;
					
				case R.drawable.ic_like:
					new LikeRequest(feed_id, mActivity){
						@Override
						protected void onGetResponse(ResponseBody body) {
							if(body.status.equals("200")){
								iv.setImageResource(R.drawable.ic_liked);
								iv.setTag(R.drawable.ic_liked);
								like_tv_clone.setText(String.format("%s likes", body.like));
							}
						}
					}.makeRequest(true);
					break;
				}
			}
		});
	    
	    like_tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Fragment userListFrag = UserListFrag.instantiate(mActivity, UserListFrag.class.getName());
				Bundle bundle = new Bundle();
				bundle.putString(UserListFrag.KEY_SID, feed_id);
				bundle.putInt(UserListFrag.KEY_TYPE, GetUserListRequest.TYPE_GET_LIKES);
				userListFrag.setArguments(bundle);
				
				FragmentTransaction transaction = ((FragmentActivity)mActivity).getSupportFragmentManager().beginTransaction();
		    	
		    	// Replace whatever is in the fragment_container view with this fragment,
		    	// and add the transaction to the back stack
		    	transaction.replace(R.id.fragment_container, userListFrag);
		        //transaction.addToBackStack(null);

		    	// Commit the transaction
		    	transaction.commit(); 
			}
		});
	    
	    final String uid = feed.uid;
	    username_tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickUserNameIv(v, uid);
			}
		});
	    
	    
		return v;
	}
	
	protected void onClickUserNameIv(View v, String uid){}
	
}
