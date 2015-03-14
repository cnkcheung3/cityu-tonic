package com.cityu.tonic.fragment;

import java.io.IOException;

import android.support.v4.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.cityu.tonic.R;
import com.cityu.tonic.R.layout;
import com.cityu.tonic.adapter.FeedAdapter;
import com.cityu.tonic.cache.CacheImageHandler;
import com.cityu.tonic.model.FeedItem;
import com.cityu.tonic.model.ResponseFeed;
import com.cityu.tonic.request.GetFollowingFeedRequest;
import com.cityu.tonic.request.GetUserListRequest;
import com.cityu.tonic.request.GetUserListRequest.ResponseUserItem;
import com.cityu.tonic.request.LikeRequest;
import com.cityu.tonic.utils.CusMediaController;
import com.cityu.tonic.utils.NetworkCircleImageView;
import com.cityu.tonic.utils.VolleySingleton;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class MyHomeFrag extends Fragment{
	
	List<ResponseFeed> mFeedItems;
	FeedAdapter mAdapter;
	Activity mActivity;
	ListView mlistView;
	MediaPlayer mMediaPlayer;
	MediaController mMediaController;
	
	ImageView playPauseBtn_iv;
	TextView curTime_tv;
	TextView totalTime_tv;
	SeekBar seekbar;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View rootView = inflater.inflate(R.layout.frag_home, container, false);
    	mActivity = this.getActivity();
    	mlistView = (ListView) rootView.findViewById(R.id.listView1);
    	
    	mFeedItems = new ArrayList<ResponseFeed>();
    	mAdapter = new FeedAdapter(mActivity, mActivity, 0, 0, mFeedItems);
    	mlistView.setAdapter(mAdapter);
    	
    	playPauseBtn_iv = (ImageView) rootView.findViewById(R.id.play_pause_btn);
    	curTime_tv = (TextView) rootView.findViewById(R.id.cur_time_tv);
    	totalTime_tv = (TextView) rootView.findViewById(R.id.total_time_tv);
    	seekbar = (SeekBar) rootView.findViewById(R.id.seekBar1);
    	
    	prepareMediaPlayer();
    	CusMediaController mc = new CusMediaController(playPauseBtn_iv, curTime_tv, totalTime_tv, seekbar);
    	mc.setMediaPlayer(mMediaPlayer);
    	
    	GetFollowingFeedRequest request = new GetFollowingFeedRequest(this.getActivity()){

			@Override
			protected void onGetFeed(ArrayList<ResponseFeed> responseFeeds) {
				for(ResponseFeed feed: responseFeeds)
					mFeedItems.add(feed);
				
				mAdapter.notifyDataSetChanged();
			}
    		
    	};
    	request.makeRequest();
    	
        return rootView;
    }
    


	private void prepareMediaPlayer() {
		//mMediaController = new MediaController(MyHomeFrag.this.getActivity());
		//mMediaController = new MediaController(MyHomeFrag.this.getActivity());
		mMediaPlayer = new MediaPlayer();
        try {
			mMediaPlayer.setDataSource("http://www.nasa.gov/multimedia/nasatv/NTV-Public-IPS.m3u8");
        	//mMediaPlayer.setDataSource("http://www.mfiles.co.uk/mp3-downloads/moonlight-movement1.mp3");
        } catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       // mMediaPlayer.setDisplay(holder);
       // mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.prepareAsync();
        //mMediaPlayer.setOnBufferingUpdateListener(this);
        //mMediaPlayer.setOnCompletionListener(this);
        //mMediaPlayer.setOnPreparedListener(this);
        //mMediaPlayer.setScreenOnWhilePlaying(true);
       // mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

	}
    
}