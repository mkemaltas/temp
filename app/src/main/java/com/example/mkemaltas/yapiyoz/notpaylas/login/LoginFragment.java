package com.example.mkemaltas.yapiyoz.notpaylas.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mkemaltas.yapiyoz.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by mkemaltas on 23.08.2016.
 */

public class LoginFragment  extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void setUI(){

    }
}
