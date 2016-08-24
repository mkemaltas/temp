package com.example.mkemaltas.yapiyoz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class BaseActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
