package com.cityu.tonic;

import java.util.regex.Pattern;

import com.cityu.tonic.request.LoginRequest;
import com.cityu.tonic.request.LoginRequest.Crediential;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	public static final String PREF_TOKEN = "pref_token";
	public static final String PREF_USER_ID = "pref_id";
	public static final String PREF_ACCOUNT = "pref_account";

	EditText pw_editText;
	EditText ac_editText;
	
	Handler handler = new Handler();
	Runnable runnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		this.getActionBar().hide();
		this.setContentView(R.layout.activity_login_splash);
		
		runnable = new Runnable(){

			@Override
			public void run() {
				
				if(!PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getString(PREF_TOKEN, "").equals("") 
					&& !PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getString(PREF_USER_ID, "").equals("")
					&& !PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getString(PREF_ACCOUNT, "").equals("")){
					
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
					
				}else{
				
					getActionBar().show();
					setContentView(R.layout.activity_login);
				
					ActionBar bar = getActionBar();
					bar.setBackgroundDrawable(new ColorDrawable(0xFF000000));
					bar.setDisplayShowHomeEnabled(false);
				
					ac_editText = (EditText)findViewById(R.id.ac_editText);
					pw_editText =  (EditText)findViewById(R.id.pw_editText);
					
				}
			}
			
		};
		
		handler.postDelayed(runnable, 2000);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_login_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
		case R.id.action_bar_done:
			Log.v("ken", "login");
			String ac = ac_editText.getText().toString();
			String pw = pw_editText.getText().toString();
			login(ac, pw);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private boolean validate(String ac, String pw){
		Pattern p = Pattern.compile("^[a-zA-Z0-9_-]$");
		for(int i=0; i<ac.length(); i++){
			if(!p.matcher(String.valueOf(ac.charAt(i))).find())
				return false;
		}
		for(int i=0; i<pw.length(); i++){
			if(!p.matcher(String.valueOf(pw.charAt(i))).find())
				return false;
		}
		return true;
	}
	
	private void login(final String ac, final String pw){
		
		this.setProgressBarIndeterminateVisibility(true);
		
		if(validate(ac, pw)){
			LoginRequest request = new LoginRequest(){
				@Override
				protected void onLoginSuccess(String id, String token) {
					
					Log.v("ken", "id: "+id);
					Log.v("ken", "token: "+token);
					
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
					sp.edit().putString(PREF_USER_ID, id).commit();
					sp.edit().putString(PREF_TOKEN, token).commit();
					sp.edit().putString(PREF_ACCOUNT, ac).commit();
					
					setProgressBarIndeterminateVisibility(false);
					
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				}

				@Override
				protected void onLoginFail(String response) {
					
					Toast.makeText(LoginActivity.this, "login failed ", Toast.LENGTH_SHORT).show();
					setProgressBarIndeterminateVisibility(false);
					
				}
				
			};
			
			Crediential cred = new Crediential();
			cred.ac = ac;
			cred.pw = pw; 
			request.makeRequest(cred, this);
		}else{
			this.setProgressBarIndeterminateVisibility(false);
			Toast.makeText(this, "Contain some invalid character. ", Toast.LENGTH_SHORT).show();
		}
	}

	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		handler.removeCallbacks(runnable);
	}

	


	



	
	
	
}
