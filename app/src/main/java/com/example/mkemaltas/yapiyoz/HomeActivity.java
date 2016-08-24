package com.example.mkemaltas.yapiyoz;

import android.os.Bundle;

import com.example.mkemaltas.yapiyoz.notpaylas.login.LoginFragment;

/**
 * Created by mkemaltas on 24.08.2016.
 */

public class HomeActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(findViewById(R.id.home_fragment_container)!=null){
            if (savedInstanceState != null) {
                return;
            }
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, loginFragment).commit();
        }
    }
}
