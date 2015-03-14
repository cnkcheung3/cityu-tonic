package com.cityu.tonic.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.cityu.tonic.R;
import com.cityu.tonic.request.GetUserListRequest;
import com.cityu.tonic.request.LikeRequest;
import com.cityu.tonic.request.GetUserListRequest.ResponseUserItem;
import com.cityu.tonic.utils.NetworkCircleImageView;
import com.cityu.tonic.utils.VolleySingleton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

public class UserListFrag extends Fragment{

	public static final String KEY_SID = "key_sid";
	public static final String KEY_TYPE = "key_type";
	String sid;
	int mType;
	ListView listView;
	Activity mActivity;
	UserListAdapter mAdapter;
	ImageLoader mImageLoader;
	ArrayList<ResponseUserItem> mUserListitems = new ArrayList<ResponseUserItem>();
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.frag_user_list, container, false);
		
		Bundle bundle = getArguments();
		sid = bundle.getString(KEY_SID);
		mType = bundle.getInt(KEY_TYPE);
		
		mActivity = this.getActivity();
		listView = (ListView) rootView.findViewById(R.id.listView1);
		mAdapter = new UserListAdapter(mActivity, 0, 0, mUserListitems);
		listView.setAdapter(mAdapter);
		
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
		
		new GetUserListRequest(sid, mType, mActivity){
			@Override
			protected void onGetUserLists(
					ArrayList<ResponseUserItem> lists) {
				for(ResponseUserItem item:lists){
					mUserListitems.add(item);
				}
				mAdapter.notifyDataSetChanged();
			}
		}.makeRequest();
		
		return rootView;
	}
	
	
	 private class UserListAdapter extends ArrayAdapter<ResponseUserItem> {

	    NetworkCircleImageView user_icon_iv;
	    TextView username_tv;
	    TextView user_des_tv;
		public UserListAdapter(Context context, int resource,
				int textViewResourceId, List<ResponseUserItem> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
			     LayoutInflater vi = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			     v = vi.inflate(R.layout.user_list_item, null);
			}
			
			ResponseUserItem user = this.getItem(position);
			    
			user_icon_iv = (NetworkCircleImageView)v.findViewById(R.id.user_icon_iv);
			username_tv = (TextView)v.findViewById(R.id.user_name_tv);
			user_des_tv = (TextView)v.findViewById(R.id.user_des_tv);
		
			String full_path = getResources().getString(R.string.development_url) + "user_pic/" + user.pic;
			user_icon_iv.setImageUrl(full_path, mImageLoader);
			username_tv.setText(user.ac);
			user_des_tv.setText(user.des);
			
			final String uid = user.uid;
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Fragment userProfileFrag = UserProfileFrag.instantiate(mActivity, UserProfileFrag.class.getName());
					Bundle bundle = new Bundle();
					bundle.putString(UserProfileFrag.KEY_USER_ID, uid);
					userProfileFrag.setArguments(bundle);
					
					FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			    	
			    	// Replace whatever is in the fragment_container view with this fragment,
			    	// and add the transaction to the back stack
			    	transaction.replace(R.id.fragment_container, userProfileFrag);
			        //transaction.addToBackStack(null);
			    	
			    	// Commit the transaction
			    	transaction.commit();
					
				}
			});
			    
			return v;
		}
			
			
	  }
	
}
