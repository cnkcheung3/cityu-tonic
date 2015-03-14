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
import com.cityu.tonic.fragment.MyHomeFrag;
import com.cityu.tonic.fragment.UserListFrag;
import com.cityu.tonic.fragment.UserProfileFrag;
import com.cityu.tonic.model.ResponseFeed;
import com.cityu.tonic.request.GetUserListRequest;
import com.cityu.tonic.request.LikeRequest;
import com.cityu.tonic.request.LikeRequest.ResponseBody;
import com.cityu.tonic.utils.NetworkCircleImageView;
import com.cityu.tonic.utils.VolleySingleton;


public class FeedAdapter extends BaseFeedAdapter {

	public FeedAdapter(Activity activity, Context context, int resource,
			int textViewResourceId, List<ResponseFeed> objects) {
		super(activity, context, resource, textViewResourceId, objects);
	}
	
	
	protected void onClickUserNameIv(View v, String uid){
		super.onClickUserNameIv(v, uid);
		Fragment userProfileFrag = UserProfileFrag.instantiate(mActivity, UserProfileFrag.class.getName());
		Bundle bundle = new Bundle();
		bundle.putString(UserProfileFrag.KEY_USER_ID, uid);
		userProfileFrag.setArguments(bundle);
		
		FragmentTransaction transaction = ((FragmentActivity)mActivity).getSupportFragmentManager().beginTransaction();
    	
    	// Replace whatever is in the fragment_container view with this fragment,
    	// and add the transaction to the back stack
    	transaction.replace(R.id.fragment_container, userProfileFrag);
        //transaction.addToBackStack(null);
    	
    	// Commit the transaction
    	transaction.commit();
	}
	
	
}
