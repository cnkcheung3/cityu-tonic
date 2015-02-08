package com.cityu.tonic.utils;

import com.cityu.tonic.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class CusMediaController implements MediaPlayerControl, OnBufferingUpdateListener{
	
	ImageView mBtnView;
	TextView mCurTimeView;
	TextView mTotalTimeView;
	SeekBar mSeekBar;
	MediaPlayer mMediaPlayer;
	
	private final Handler handler = new Handler();
	
	public CusMediaController(ImageView btn, TextView curTime, TextView totalTime, SeekBar seekbar) {
		this.mBtnView = btn;
		this.mCurTimeView = curTime;
		this.mTotalTimeView = totalTime;
		this.mSeekBar = seekbar;
	}
	
	private void primarySeekBarProgressUpdater() {
		mSeekBar.setProgress((int)(((float)mMediaPlayer.getCurrentPosition()/mMediaPlayer.getDuration())*100)); // This math construction give a percentage of "was playing"/"song length"
		if (mMediaPlayer.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	primarySeekBarProgressUpdater();
				}
		    };
		    handler.postDelayed(notification,1000);
    	}
    }
	
	private void init(){
		mBtnView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPlaying()){
					mBtnView.setImageResource(R.drawable.ic_media_play);
					pause();
				}
				else{
					mBtnView.setImageResource(R.drawable.ic_media_pause);
					start();
				}
				
				primarySeekBarProgressUpdater();
			}
		});
		
		
	}
	
	public void setMediaPlayer(MediaPlayer mp) {
		this.mMediaPlayer = mp;
		init();
	}
	
	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return false;
	}

	@Override
	public int getAudioSessionId() {
		return mMediaPlayer.getAudioSessionId();
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		return mMediaPlayer.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		return mMediaPlayer.getDuration();
	}

	@Override
	public boolean isPlaying() {
		return mMediaPlayer.isPlaying();
	}

	@Override
	public void pause() {
		mMediaPlayer.pause();
	}

	@Override
	public void seekTo(int i) {
		mMediaPlayer.seekTo(i);
	}

	@Override
	public void start() {
		mMediaPlayer.start();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		// TODO Auto-generated method stub
		mSeekBar.setSecondaryProgress(percent);
	}

	
}
