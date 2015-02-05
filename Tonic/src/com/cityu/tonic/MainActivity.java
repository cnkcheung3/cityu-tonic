package com.cityu.tonic;

import com.cityu.tonic.fragment.MyHomeFrag;
import com.cityu.tonic.fragment.UserProfileFrag;

import android.app.Activity;
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

	
	ImageView home_iv;
	ImageView profile_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        home_iv = (ImageView) findViewById(R.id.btn_home);
        profile_iv = (ImageView) findViewById(R.id.btn_profile);
        
        home_iv.setOnClickListener(this);
        profile_iv.setOnClickListener(this);
        
        //initizie home page fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, Fragment.instantiate(this, MyHomeFrag.class.getName()));
        transaction.commit();
    }
    
    private void goToFragment(Fragment newFragment) {
    	
    	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    	
    	// Replace whatever is in the fragment_container view with this fragment,
    	// and add the transaction to the back stack
    	transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

    	// Commit the transaction
    	transaction.commit();
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_home:
			goToFragment(Fragment.instantiate(this, MyHomeFrag.class.getName()));
			break;
		case R.id.btn_profile:
			goToFragment(Fragment.instantiate(this, UserProfileFrag.class.getName()));
			break;
		
		}
	}
}
