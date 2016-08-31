package com.example.mkemaltas.yapiyoz.notpaylas.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mkemaltas.yapiyoz.LoggedInActivity;
import com.example.mkemaltas.yapiyoz.R;
import com.example.mkemaltas.yapiyoz.firebase.FirebaseAuthentication;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shaishavgandhi.loginbuttons.GooglePlusButton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mkemaltas on 23.08.2016.
 */

public class LoginFragment  extends Fragment {

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText emailEditText;
    private EditText passwordEditText;

    private TextView signUpTextView;
    private TextView forgotPasswordTextView;
    private TextView nameTextView;
    private TextView emailTextView;

    private Button signInWithEmailButton;
    private Button customButton;

    private LoginButton signInWithFacebookButton;
    private GooglePlusButton signInWithGoogleButton;

    private ProgressBar progressBar;

    private static LoginFragment instance;

    public static LoginFragment getInstance(){
        if(instance == null){
            instance = new LoginFragment();
            return instance;
        }
        else return instance;
    }

    public LoginFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        emailEditText            = (EditText) view.findViewById(R.id.login_fragment_email_et);
        passwordEditText         = (EditText) view.findViewById(R.id.login_fragment_password_et);

        signUpTextView           = (TextView) view.findViewById(R.id.login_fragment_not_a_member_tv);
        forgotPasswordTextView   = (TextView) view.findViewById(R.id.login_fragment_forgot_password_tv);
        nameTextView             = (TextView) view.findViewById(R.id.login_fragment_name_tv);
        emailTextView            = (TextView) view.findViewById(R.id.login_fragment_email_tv);

        progressBar              = (ProgressBar) view.findViewById(R.id.login_fragment_progressBar);

        signInWithEmailButton    = (Button)  view.findViewById(R.id.login_fragment_sign_in_button);

        //customButton             = (Button) view.findViewById(R.id.login_fragment_sign_out_button);

       /* customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    String message = user.getEmail() + " - " + user.getDisplayName();
                    Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    FirebaseAuth.getInstance().signOut();
                }

            }
        });*/


        //FACEBOOK
        signInWithFacebookButton = (LoginButton) view.findViewById(R.id.login_fragment_fb_button);
     //   signInWithFacebookButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_login_layouts_dark));



        signInWithFacebookButton.setReadPermissions("email", "public_profile");
        signInWithFacebookButton.setFragment(this);

        mCallbackManager = CallbackManager.Factory.create();

        signInWithFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Snackbar snackbar = Snackbar.make(getView(), "facebook:onSuccess:"+ loginResult, Snackbar.LENGTH_LONG);
                snackbar.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Snackbar snackbar = Snackbar.make(getView(), "facebook:onSuccess:", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onError(FacebookException error) {
                Snackbar snackbar = Snackbar.make(getView(), "facebook:onSuccess:"+ error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });



        signInWithGoogleButton = (GooglePlusButton) view.findViewById(R.id.login_fragment_google_button);


        signInWithEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String email    = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Snackbar snackbar = Snackbar.make(view, "Lütfen e-mail adresinizi girin.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return ;
                }
                if(TextUtils.isEmpty(password)){
                    Snackbar snackbar = Snackbar.make(view, "Lütfen şifrenizi girin.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return ;
                }
                if(!isEmailValid(email)){
                    Snackbar snackbar = Snackbar.make(view, "Lütfen geçerli bir e-mail adresi girin.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                FirebaseAuthentication.getInstance().getAuth().signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if(!task.isSuccessful()){
                                    Snackbar snackbar = Snackbar.make(view,"Giriş yapamadınız. Lütfen e-mail ve şifrenizi kontrol edip tekrar deneyin.",Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                                else{
                                    Intent intent = new Intent(getActivity(), LoggedInActivity.class);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        });

            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupFragment signupFragment = SignupFragment.getInstance();
                //signupFragment.setArguments(getActivity().getIntent().getExtras());


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);


                transaction.replace(R.id.home_fragment_container,signupFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.getInstance();
                //forgotPasswordFragment.setArguments(getActivity().getIntent().getExtras());



                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);


                transaction.replace(R.id.home_fragment_container,forgotPasswordFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void handleFacebookAccessToken(AccessToken token){
        final Snackbar snackbar = Snackbar.make(getView(), "handleAccessToken:" + token, Snackbar.LENGTH_LONG);
        snackbar.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Snackbar snackbar1 = Snackbar.make(getView(),"Authentication failed",Snackbar.LENGTH_LONG);
                            snackbar1.show();
                        }
                        else
                        {
                            Snackbar snackbar1 = Snackbar.make(getView(),"Huu beybi", Snackbar.LENGTH_LONG);
                            snackbar1.show();
                        }
                    }
                });
    }

    public void loggedIn(String name, String email){
        nameTextView.setText(name);
        nameTextView.setVisibility(View.VISIBLE);
        emailTextView.setText(email);
        emailTextView.setVisibility(View.VISIBLE);
    }

    public void loggedOut(){
        nameTextView.setVisibility(View.GONE);
        emailTextView.setVisibility(View.GONE);
    }
}

