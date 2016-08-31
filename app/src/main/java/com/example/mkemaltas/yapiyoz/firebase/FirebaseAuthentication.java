package com.example.mkemaltas.yapiyoz.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by mkemaltas on 25.08.2016.
 */

public class FirebaseAuthentication {

    private final FirebaseAuth mAuth;
    private static FirebaseAuthentication mInstance;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static FirebaseAuthentication getInstance(){
        if(mInstance==null){
           mInstance = new FirebaseAuthentication();
            return mInstance;
        }
        else
            return mInstance;
    }

    private FirebaseAuthentication(){
        mAuth = FirebaseAuth.getInstance();
    }



    public FirebaseAuth getAuth(){
        return mAuth;
    }
    public FirebaseAuth.AuthStateListener getAuthListener(){return mAuthListener;}
}
