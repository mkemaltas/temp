package com.example.mkemaltas.yapiyoz.notpaylas.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shaishavgandhi.loginbuttons.FacebookButton;
import com.shaishavgandhi.loginbuttons.GooglePlusButton;

/**
 * Created by mkemaltas on 25.08.2016.
 */

public class SignupFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;

    private Button signUpButton;

    private TextView signInTextView;

    private ProgressBar progressBar;

    private static SignupFragment instance;

    public static SignupFragment getInstance(){
        if(instance == null){
            instance = new SignupFragment();
            return instance;
        }
        else return instance;
    }

    public SignupFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        emailEditText = (EditText) view.findViewById(R.id.signup_fragment_email_et);
        passwordEditText = (EditText) view.findViewById(R.id.signup_fragment_password_et);

        signInTextView = (TextView) view.findViewById(R.id.signup_fragment_already_member_tv);

        progressBar = (ProgressBar) view.findViewById(R.id.signup_fragment_progressBar);

        signUpButton = (Button) view.findViewById(R.id.signup_fragment_signup_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Snackbar snackbar = Snackbar.make(view, "Lütfen e-mail adresinizi girin.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Snackbar snackbar = Snackbar.make(view, "Lütfen şifrenizi girin.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                if (!isEmailValid(email)) {
                    Snackbar snackbar = Snackbar.make(view, "Lütfen geçerli bir e-mail adresi girin.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                FirebaseAuthentication.getInstance().getAuth().createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if(!task.isSuccessful()){
                                    Snackbar snackbar = Snackbar.make(view, "Üye olamadınız. Lütfen bilgilerinizi kontrol edip tekrar deneyin.", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                                else {
                                    LoginFragment loginFragment = LoginFragment.getInstance();
                                    loginFragment.setArguments(getActivity().getIntent().getExtras());


                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);


                                    transaction.replace(R.id.home_fragment_container,loginFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            }
                        });

            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = LoginFragment.getInstance();
               // loginFragment.setArguments(getActivity().getIntent().getExtras());



                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);


                transaction.replace(R.id.home_fragment_container,loginFragment);
                transaction.commit();
            }
        });
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void goBackToPreviousFragment() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            getChildFragmentManager().popBackStack();
        }
    }

}
