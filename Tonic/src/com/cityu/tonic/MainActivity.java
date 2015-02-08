package com.cityu.tonic;

import com.cityu.tonic.fragment.MyHomeFrag;
import com.cityu.tonic.fragment.UserProfileFrag;
import com.cityu.tonic.upload.AudioFileChooseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity implements OnClickListener{

	Fragment HomeFrag;
	Fragment UserProfileFrag;
	ImageView home_iv;
	ImageView profile_iv;
	ImageView audio_upload_iv;
	
	AudioFileChooseHandler fc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        home_iv = (ImageView) findViewById(R.id.btn_home);
        profile_iv = (ImageView) findViewById(R.id.btn_profile);
        audio_upload_iv = (ImageView) findViewById(R.id.btn_upload);
        
        home_iv.setOnClickListener(this);
        profile_iv.setOnClickListener(this);
        audio_upload_iv.setOnClickListener(this);
        
        HomeFrag = Fragment.instantiate(this, MyHomeFrag.class.getName());
        
        //initizie home page fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, HomeFrag);
        transaction.commit();
    }
    
    private void goToFragment(Fragment newFragment) {
    	
    	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    	
    	// Replace whatever is in the fragment_container view with this fragment,
    	// and add the transaction to the back stack
    	transaction.replace(R.id.fragment_container, newFragment);
        //transaction.addToBackStack(null);

    	// Commit the transaction
    	transaction.commit(); 
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_home:
			goToFragment(HomeFrag);
			break;
		case R.id.btn_profile:
			if(UserProfileFrag == null)
				UserProfileFrag = Fragment.instantiate(this, UserProfileFrag.class.getName());
			goToFragment(UserProfileFrag);
			break;
		case R.id.btn_upload:
			fc = new AudioFileChooseHandler(this);
			fc.choose();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(fc!=null)
			fc.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
}
