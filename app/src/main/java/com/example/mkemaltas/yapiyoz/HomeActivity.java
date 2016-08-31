package com.example.mkemaltas.yapiyoz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import com.example.mkemaltas.yapiyoz.notpaylas.login.LoginFragment;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by mkemaltas on 24.08.2016.
 */

public class HomeActivity extends BaseActivity{

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private LoginFragment loginFragment;

    private CallbackManager mCallbackManager;
   private AccessTokenTracker mAccessTokenTracker;
   // private AccessToken mAccessToken;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_home);
        if(findViewById(R.id.home_fragment_container)!=null){
            if (savedInstanceState != null) {
                return;
            }
            loginFragment = new LoginFragment();
            loginFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, loginFragment).commit();
        }

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                onFacebookAccessTokenChanged(currentAccessToken);
            }
        };


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    loginFragment.loggedIn(user.getDisplayName(),user.getEmail());
                }
                else{
                    loginFragment.loggedOut();
                }
            }
        };

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
       // mAccessTokenTracker.stopTracking();

    }
    private void onFacebookAccessTokenChanged(AccessToken token){
        if(token==null) {
            FirebaseAuth.getInstance().signOut();
        }

    }


}
